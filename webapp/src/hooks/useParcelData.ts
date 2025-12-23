import {useCallback, useEffect, useState} from 'react';
import {BackendRouteResponse, CreateParcelRequest, parcelService} from '../services/parcelService';
import {CorrespondenceType, Parcel, ParcelStatus, Route} from '../types/parcel';
import {APP_CONFIG} from '../utils/constants';

import { BackendRouteItem } from '../types/backendRouteItem';

export const useParcelData = () => {
  const [parcels, setParcels] = useState<Parcel[]>([]);
  const [routes, setRoutes] = useState<Route[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const mapBackendRouteToFrontendRoute = useCallback(( backendRouteResponse: BackendRouteResponse, parcelId: number): Route => {
    console.log('useParcelData: mapping back to front');
    const backendSequence: BackendRouteItem[] = backendRouteResponse.sequence || [];

    const path: string[] = [];

    backendSequence.forEach((r) => {
      if (r.type === 'stop') path.push((r as any).stopName); 
    });
    console.log('Path for Route id ', backendRouteResponse.id, 'is ', path);

    return {
      parcelId: parcelId,
      path: path,
      sequence: backendSequence,
      currentPosition: 0, // Reset to 0 since progress is not implemented
      status: backendRouteResponse.status,
    };
  }, []);

  // 💡 FIXED FILTERING LOGIC: Ensures active parcels with a valid routeId are targeted.
  const loadRoutesForActiveParcels = useCallback(async (currentParcels: Parcel[]) => {
    console.log('useParcelData: Starting route hydration...');
    
    // Filter for active parcels that have a route ID assigned (p.routeId > 0)
    const activeParcelsWithRoute = currentParcels.filter(
        (p: Parcel) => p.status === 'active' && p.routeId > 0
    );

    if (activeParcelsWithRoute.length === 0) {
      setRoutes([]);
      console.log('useParcelData: No active parcels with assigned route found. Routes state cleared.');
      return;
    }

    try {
      // Use Promise.all to fetch all routes concurrently
      const routePromises = activeParcelsWithRoute.map(async (parcel: Parcel) => {
        try {
            const routeId = parcel.routeId;
            
            // This condition is technically redundant if the filter works, but kept for type safety
            if (!routeId) return null; 

            // 1. Fetch the existing route object using the stored ID (maps to GET /api/routes/{id})
            const routeResponse = await parcelService.getRouteById(routeId);
            
            // 2. Map the backend response to the frontend Route object
            // currentPosition is set to 0 inside mapBackendRouteToFrontendRoute
            return mapBackendRouteToFrontendRoute(routeResponse, parcel.id);
            
        } catch (e) {
            console.warn(`Could not fetch route ID ${parcel.routeId} for parcel ${parcel.id}. (Backend 404/500)`);
            return null; // Return null on failure
        }
      });

      const fetchedRoutes = await Promise.all(routePromises);
      
      // Filter out any nulls (failed fetches)
      const validRoutes = fetchedRoutes.filter(r => r !== null) as Route[];
      
      setRoutes(validRoutes);
      console.log(`useParcelData: Successfully loaded ${validRoutes.length} active routes.`);

    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load routes during hydration');
      console.error('useParcelData: Fatal Error in loadRoutesForActiveParcels:', err);
    }
  }, [mapBackendRouteToFrontendRoute]);

  const loadParcels = useCallback(async () => {
    console.log('useParcelData: Executing loadParcels...');
    try {
      setLoading(true);
      setError(null);

      // 1. Fetch Parcels (including their routeId)
      const parcelsData = await parcelService.getAllParcels();
      setParcels(parcelsData);
      
      // 2. Hydrate Routes: Fetch associated RouteDTOs for active parcels
      await loadRoutesForActiveParcels(parcelsData);

    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load parcels');
      console.error('useParcelData: Error in loadParcels:', err);
    } finally {
      setLoading(false);
    }
  }, [loadRoutesForActiveParcels]);

  // Load initial data and set up refresh interval
  useEffect(() => {
    console.log( `useParcelData: Set up refresh interval for ${APP_CONFIG.REFRESH_INTERVAL / 1000} seconds.`);

    const interval = setInterval(loadParcels, APP_CONFIG.REFRESH_INTERVAL);

    return () => {
      clearInterval(interval); // Cleanup on unmount
      console.log('useParcelData: Cleanup - interval cleared.');
    };
  }, [loadParcels]);

  const addParcel = async (parcelData: {
    name: string;
    description: string;
    type: CorrespondenceType;
    origin: string;
    destination: string;
    maxTransfers: number;
    deadline: string;
  }) => {
    try {
      setError(null);

      const createRequest: CreateParcelRequest = {
        name: parcelData.name,
        description: parcelData.description,
        type: parcelData.type,
        origin: parcelData.origin,
        destination: parcelData.destination,
        maxTransfers: parcelData.maxTransfers,
        deadline: parcelData.deadline,
        status: 'created',
      };

      const newParcel = await parcelService.createParcel(createRequest);
      setParcels((prev) => [...prev, newParcel]);
      console.log('useParcelData: Added new parcel:', newParcel);
      return newParcel;
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to create parcel');
      console.error('useParcelData: Error adding parcel:', err);
      throw err;
    }
  };

  const updateParcelStatus = async (
    parcelId: number,
    newStatus: ParcelStatus,
    progress?: number,
    currentLocation?: string
  ) => {
    try {
      setError(null);

      const updatedParcel = await parcelService.updateParcelStatus(parcelId, newStatus);

      setParcels((prev) =>
        prev.map((parcel) => (parcel.id === parcelId ? updatedParcel : parcel))
      );

      setRoutes((prev) =>
        prev.map((route) =>
          route.parcelId === parcelId
            ? {
                ...route,
                // Removed progress calculation logic as requested
                status: newStatus === 'completed' ? 'completed' : 'active', 
              }
            : route
        )
      );

      return updatedParcel;
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to update parcel');
      console.error('useParcelData: Error updating parcel status:', err);
      throw err;
    }
  };

  const calculateAndSetRoute = async (parcelId: number) => {
    console.log(`useParcelData: Starting calculateAndSetRoute for parcel ID: ${parcelId}`);
    try {
      setError(null);

      let parcelToRoute = parcels.find((p) => p.id === parcelId);
      if (!parcelToRoute) {
        console.log(`useParcelData: Parcel ${parcelId} not found in current state, fetching from backend.`);

        const fetchedParcel = await parcelService.getParcelById(parcelId);
        if (!fetchedParcel) {
          throw new Error(`Parcel with ID ${parcelId} not found.`);
        }
        setParcels((prev) => {
          if (!prev.some((p) => p.id === fetchedParcel.id)) {
            return [...prev, fetchedParcel];
          }
          return prev;
        });
        parcelToRoute = fetchedParcel;
      }

      console.log('useParcelData: Calling parcelService.calculateOptimalRouteForEntity with entityId:', parcelToRoute.id);

      const calculatedRouteResponse = await parcelService.calculateOptimalRouteForEntity(parcelToRoute.id);
      
      const newFrontendRoute = mapBackendRouteToFrontendRoute(calculatedRouteResponse, parcelToRoute.id);

      setRoutes((prev) => {
        const filteredRoutes = prev.filter(
          (r) => r.parcelId !== parcelToRoute!.id
        );
        return [...filteredRoutes, newFrontendRoute];
      });
      return newFrontendRoute;
    } catch (err) {
      setError(
        err instanceof Error
          ? err.message
          : `Failed to calculate route for parcel ${parcelId}`
      );
      console.error(`useParcelData: Error calculating route for parcel ${parcelId}:`, err);
      throw err;
    }
  };

  const refreshData = useCallback(() => {
    console.log('useParcelData: refreshData called. Reloading parcels...');
    loadParcels();
  }, [loadParcels]);

  return {
    parcels,
    routes,
    loading,
    error,
    addParcel,
    updateParcelStatus,
    calculateAndSetRoute,
    refreshData,
  };
};
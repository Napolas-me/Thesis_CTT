import { useCallback, useEffect, useState } from 'react';
import { BackendRouteResponse, CreateParcelRequest, parcelService } from '../services/parcelService';
import { CorrespondenceType, Parcel, ParcelStatus, Route } from '../types/parcel'; // Import ParcelStatus
import { APP_CONFIG } from '../utils/constants';

export const useParcelData = () => {
  const [parcels, setParcels] = useState<Parcel[]>([]);
  const [routes, setRoutes] = useState<Route[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Helper to map BackendRouteResponse to frontend Route
  const mapBackendRouteToFrontendRoute = (
    backendRoute: BackendRouteResponse,
    parcelId: number // Need to pass parcelId to link it
  ): Route => {
    // Assuming backend sequence contains strings for path visualization
    // If backend sequence contains Trip/Stop objects, you'd need to extract names.
    const path: string[] = backendRoute.sequence
      ? backendRoute.sequence.map(item => {
          // This is a simplification. You might need more robust type checking
          // if sequence items are complex objects (TripDTO/StopDTO).
          // For now, assuming they have a 'name' or 'stopName' property.
          if (typeof item === 'object' && item !== null) {
            if (item.stopName) return item.stopName; // For StopDTO
            if (item.origin && item.destination) return `${item.origin} -> ${item.destination}`; // For TripDTO
          }
          return String(item); // Fallback
        })
      : [];

    return {
      parcelId: parcelId,
      path: path,
      currentPosition: 0, // Default for new route
      status: 'active', // New routes are active
    };
  };

  const loadParcels = useCallback(async () => {
    console.log('useParcelData: Starting loadParcels...');
    try {
      setLoading(true);
      setError(null);

      const parcelsData = await parcelService.getAllParcels();
      console.log('useParcelData: Received parcelsData from service:', parcelsData);
      setParcels(parcelsData);
      console.log('useParcelData: parcels state updated to:', parcelsData);

      // Routes are calculated on demand (e.g., when "Ver no Mapa" is clicked)
      // So, we don't clear them here, but rather add/update them when calculated.
      // For initial load, we might try to load routes for 'active' parcels if they exist in backend.
      // For now, we'll keep routes managed when calculateAndSetRoute is called.
      // setRoutes([]); // Removed this line to persist routes across refreshes if they were already calculated
      console.log('useParcelData: Finished loadParcels, loading set to false.');

    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load parcels');
      console.error('useParcelData: Error in loadParcels:', err);
    } finally {
      setLoading(false);
    }
  }, []);

  // Load initial data and set up refresh interval
  useEffect(() => {
    console.log('useParcelData: useEffect triggered.');
    loadParcels();
    const interval = setInterval(loadParcels, APP_CONFIG.REFRESH_INTERVAL);
    console.log(`useParcelData: Set up refresh interval for ${APP_CONFIG.REFRESH_INTERVAL / 1000} seconds.`);
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
    deadline: string; // ISO string
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
        status: 'created', // Always created initially
      };
      
      const newParcel = await parcelService.createParcel(createRequest);
      setParcels(prev => [...prev, newParcel]);
      console.log('useParcelData: Added new parcel:', newParcel);
      return newParcel;
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to create parcel');
      console.error('useParcelData: Error adding parcel:', err);
      throw err;
    }
  };

  const updateParcelStatus = async (
    parcelId: number, // Changed to number
    newStatus: ParcelStatus, // Use ParcelStatus type
    progress?: number, // Optional, for progress updates
    currentLocation?: string // Optional, for location updates
  ) => {
    try {
      setError(null);
      
      const updatedParcel = await parcelService.updateParcel(parcelId, {
        status: newStatus,
        // Add other fields if they are part of the update and need to be sent
        // For example, if progress/currentLocation are part of Parcel and need to be updated in DB
        // For now, assuming status is the primary update.
      });
      
      setParcels(prev => 
        prev.map(parcel => 
          parcel.id === parcelId ? updatedParcel : parcel
        )
      );
      
      // Update route status/progress if a route exists for this parcel
      setRoutes(prev =>
        prev.map(route =>
          route.parcelId === parcelId
            ? {
                ...route,
                // Assuming progress is only relevant if route.path exists
                currentPosition: typeof progress === 'number' && route.path.length > 0
                  ? Math.floor((progress / 100) * (route.path.length - 1))
                  : route.currentPosition, // Keep current position if no progress
                status: newStatus === 'completed' ? 'completed' : 'active' // Map parcel status to route status
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

      let parcelToRoute = parcels.find(p => p.id === parcelId);
      if (!parcelToRoute) {
        console.log(`useParcelData: Parcel ${parcelId} not found in current state, fetching from backend.`);
        const fetchedParcel = await parcelService.getParcelById(parcelId);
        if (!fetchedParcel) {
          throw new Error(`Parcel with ID ${parcelId} not found.`);
        }
        // Add fetched parcel to state if it's not already there
        setParcels(prev => {
          if (!prev.some(p => p.id === fetchedParcel.id)) {
            return [...prev, fetchedParcel];
          }
          return prev;
        });
        parcelToRoute = fetchedParcel;
      }

      console.log('useParcelData: Calling parcelService.calculateOptimalRouteForEntity with entityId:', parcelToRoute.id);
      const calculatedRouteResponse = await parcelService.calculateOptimalRouteForEntity(parcelToRoute.id);
      console.log('useParcelData: Received calculated route response:', calculatedRouteResponse);

      const newFrontendRoute = mapBackendRouteToFrontendRoute(calculatedRouteResponse, parcelToRoute.id);
      console.log('useParcelData: Mapped to frontend route:', newFrontendRoute);

      setRoutes(prev => {
        const filteredRoutes = prev.filter(r => r.parcelId !== parcelToRoute!.id); // Remove old route for this parcel
        return [...filteredRoutes, newFrontendRoute]; // Add the new one
      });
      console.log('useParcelData: Routes state updated.');
      return newFrontendRoute;
    } catch (err) {
      setError(err instanceof Error ? err.message : `Failed to calculate route for parcel ${parcelId}`);
      console.error(`useParcelData: Error calculating route for parcel ${parcelId}:`, err);
      throw err;
    }
  };

  const refreshData = () => {
    console.log('useParcelData: refreshData called. Reloading parcels...');
    loadParcels();
    // Decide if routes should be cleared on refresh or re-calculated if active
    // For now, we'll let `calculateAndSetRoute` manage route state.
  };

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

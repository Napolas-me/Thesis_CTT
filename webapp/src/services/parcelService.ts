// Parcel Service - Handles all parcel-related API calls
import { BackendRouteItem } from '../types/backendRouteItem';
import { CorrespondenceType, Parcel, ParcelStatus } from '../types/parcel';
import { apiService } from './api';

// CreateParcelRequest should match the EntityDTO structure for creation
export interface CreateParcelRequest {
  name: string;
  description: string;
  type: CorrespondenceType;
  origin: string;
  destination: string;
  maxTransfers: number;
  deadline: string;
  status: ParcelStatus;
}

export interface ParcelSearchFilters {
  status?: string;
  type?: string;
  origin?: string;
  destination?: string;
  dateFrom?: string;
  dateTo?: string;
}

// Interface for the backend's RouteDTO response from calculateOptimalRoute (and getRouteById)
export interface BackendRouteResponse {
  id: number;
  name: string;
  routeStartName: string;
  routeEndName: string;
  routeStartDate: string;
  routeEndDate: string;
  status: string;
  assignedTransport?: {
    id: number;
    name: string;
    type: string;
    capacity: number;
    maxCapacity: number;
    status: string;
    carbonEmissionsGkm: number;
  };
  sequence?: BackendRouteItem[];
}

class ParcelService {
  // Get all parcels
  async getAllParcels(): Promise<Parcel[]> {
    try {
      console.log(
        'ParcelService: Attempting to fetch parcels from /api/entities'
      );
      const parcelsData = await apiService.get<Parcel[]>('/entities');
      console.log(
        'ParcelService: Raw API response received (now directly data):',
        parcelsData
      );
      return parcelsData;
    } catch (error) {
      console.error('ParcelService: Failed to fetch parcels:', error);
      throw error;
    }
  }

  // Get parcel by ID
  async getParcelById(id: number): Promise<Parcel> {
    try {
      const parcel = await apiService.get<Parcel>(`/entities/${id}`);
      return parcel;
    } catch (error) {
      console.error(`Failed to fetch parcel ${id}:`, error);
      throw error;
    }
  }

  // 💡 NEW METHOD: Fetches a stored route by its ID (maps to GET /api/routes/{id})
  async getRouteById(routeId: number): Promise<BackendRouteResponse> {
    try {
      // Uses the RouteController endpoint /api/routes/{id}
      const route = await apiService.get<BackendRouteResponse>(`/routes/${routeId}`);
      return route;
    } catch (error) {
      console.error(`Failed to fetch route ${routeId}:`, error);
      throw error;
    }
  }

  // Create new parcel
  async createParcel(parcelData: CreateParcelRequest): Promise<Parcel> {
    try {
      const newParcel = await apiService.post<Parcel>('/entities', parcelData);
      return newParcel;
    } catch (error) {
      console.error('Failed to create parcel:', error);
      throw error;
    }
  }

  // Update parcel status
  async updateParcelStatus( id: number, status: ParcelStatus): Promise<Parcel> {
    try {
      const response = await apiService.post<Parcel>(`/entities/update-status`, 
      {
        entityId: id,
        entityStatus: status
      });

      return response; 
    } catch (error) {
      console.error(`Failed to update parcel ${id}:`, error);
      throw error;
    }
  }

  // Search parcels with filters
  async searchParcels(filters: ParcelSearchFilters): Promise<Parcel[]> {
    try {
      const queryParams = new URLSearchParams();

      Object.entries(filters).forEach(([key, value]) => {
        if (value) {
          queryParams.append(key, value as string);
        }
      });

      const endpoint = `/entities/search?${queryParams.toString()}`;
      const searchResults = await apiService.get<Parcel[]>(endpoint);
      return searchResults;
    } catch (error) {
      console.error('Failed to search parcels:', error);
      throw error;
    }
  }

  // Get running processes (parcels in 'active' status)
  async getRunningProcesses(): Promise<Parcel[]> {
    try {
      const runningProcesses = await apiService.get<Parcel[]>('/entities/getactive');
      return runningProcesses;
    } catch (error) {
      console.error('Failed to fetch running processes:', error);
      throw error;
    }
  }

  // Calculate optimal route for a given entity ID
  async calculateOptimalRouteForEntity(entityId: number): Promise<BackendRouteResponse> {
    try {
      // 1. Calculate the route (maps to POST /api/entities/get-route)
      const calculatedRoute = await apiService.post<BackendRouteResponse>('/entities/get-route',
        {
          entityId: entityId,
        }
      );
      
      // 2. Assign the route ID to the entity (UA) in the database (maps to POST /api/entities/add-route-id)
      console.log('Updating route id with id ', calculatedRoute.id);
      await apiService.post<Parcel>(`/entities/add-route-id`, 
      {
        entityId: entityId,
        routeId: calculatedRoute.id
      });

      return calculatedRoute;
    } catch (error) {
      console.error('Failed to calculate route:', error);
      throw error;
    }
  }
}

export const parcelService = new ParcelService();
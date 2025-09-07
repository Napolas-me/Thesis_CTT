// Parcel Service - Handles all parcel-related API calls
import { CorrespondenceType, Parcel, ParcelStatus } from '../types/parcel'; // Import ParcelStatus
import { apiService } from './api';

// CreateParcelRequest should match the EntityDTO structure for creation
export interface CreateParcelRequest {
  name: string; // Matches EntityDTO
  description: string;
  type: CorrespondenceType;
  origin: string;
  destination: string;
  maxTransfers: number;
  deadline: string; // ISO string format for LocalDateTime
  status: ParcelStatus; // Initial status, e.g., 'created'
}

// Removed UpdateParcelStatusRequest as we will send full Parcel object

export interface ParcelSearchFilters {
  status?: string;
  type?: string;
  origin?: string;
  destination?: string;
  dateFrom?: string;
  dateTo?: string;
}

// Interface for the backend's RouteDTO response from calculateOptimalRoute
export interface BackendRouteResponse {
  id: number;
  name: string;
  routeStartName: string;
  routeEndName: string;
  routeStartDate: string;
  routeEndDate: string;
  status: string;
  assignedTransport?: { // This matches your TransportDTO structure
    id: number;
    name: string;
    type: string;
    capacity: number;
    maxCapacity: number;
    status: string;
    carbonEmissionsGkm: number;
  };
  sequence?: (any)[]; // Can be TripDTO or StopDTO, using 'any' for flexibility here
}


class ParcelService {
  // Get all parcels
  async getAllParcels(): Promise<Parcel[]> {
    try {
      console.log('ParcelService: Attempting to fetch parcels from /api/entities');
      const parcelsData = await apiService.get<Parcel[]>('/entities');
      console.log('ParcelService: Raw API response received (now directly data):', parcelsData);
      return parcelsData;
    } catch (error) {
      console.error('ParcelService: Failed to fetch parcels:', error);
      throw error;
    }
  }

  // Get parcel by ID
  async getParcelById(id: number): Promise<Parcel> { // Changed id to number
    try {
      const parcel = await apiService.get<Parcel>(`/entities/${id}`);
      return parcel;
    } catch (error) {
      console.error(`Failed to fetch parcel ${id}:`, error);
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

  // Update parcel status (now sends full Parcel object)
  async updateParcel(
    id: number, // Changed id to number
    updatedFields: Partial<Parcel> // Accepts partial Parcel data
  ): Promise<Parcel> {
    try {
      // First, get the existing parcel data
      const existingParcel = await this.getParcelById(id);

      // Merge the updated fields into the existing parcel data
      const parcelToUpdate = { ...existingParcel, ...updatedFields };

      // Send the full, merged parcel object to the backend PUT endpoint
      const response = await apiService.put<Parcel>(`/entities/${id}`, parcelToUpdate);
      return response; // apiService.put now returns the data directly
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
      // Assuming backend has an endpoint to get active entities
      // The current backend has /api/entities/getactive
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
      // The backend endpoint is /api/entities/get-route and expects { "entityId": number }
      const calculatedRoute = await apiService.post<BackendRouteResponse>('/entities/get-route', {
        entityId: entityId
      });
      return calculatedRoute;
    } catch (error) {
      console.error('Failed to calculate route:', error);
      throw error;
    }
  }
}

export const parcelService = new ParcelService();

// Separation Center Service - Handles separation center and gate operations
import { Gate, SeparationCenter, Truck } from '../types/parcel';
import { apiService } from './api'; // No need to import ApiResponse here, as apiService handles it

export interface UpdateGateRequest {
  truckId?: string;
  status: Gate['status'];
  parcelsEntered?: number;
  parcelsToEnter?: number;
}

class SeparationCenterService {
  // Get all separation centers
  async getAllCenters(): Promise<SeparationCenter[]> {
    try {
      const response = await apiService.get<SeparationCenter[]>('/separation-centers');
      return response.data; // CORRECTED: Return the 'data' property
    } catch (error) {
      console.error('Failed to fetch separation centers:', error);
      throw error;
    }
  }

  // Get specific separation center
  async getCenterById(id: string): Promise<SeparationCenter> {
    try {
      const response = await apiService.get<SeparationCenter>(`/separation-centers/${id}`);
      return response.data; // CORRECTED: Return the 'data' property
    } catch (error) {
      console.error(`Failed to fetch center ${id}:`, error);
      throw error;
    }
  }

  // Get gates for a specific center
  async getGatesByCenter(centerId: string): Promise<Gate[]> {
    try {
      const response = await apiService.get<Gate[]>(`/separation-centers/${centerId}/gates`);
      return response.data; // CORRECTED: Return the 'data' property
    } catch (error) {
      console.error(`Failed to fetch gates for center ${centerId}:`, error);
      throw error;
    }
  }

  // Update gate information
  async updateGate(centerId: string, gateId: string, updateData: UpdateGateRequest): Promise<Gate> {
    try {
      const response = await apiService.put<Gate>(
        `/separation-centers/${centerId}/gates/${gateId}`,
        updateData
      );
      return response.data; // CORRECTED: Return the 'data' property
    } catch (error) {
      console.error(`Failed to update gate ${gateId}:`, error);
      throw error;
    }
  }

  // Get all trucks
  async getAllTrucks(): Promise<Truck[]> {
    try {
      const response = await apiService.get<Truck[]>('/trucks');
      return response.data; // CORRECTED: Return the 'data' property
    } catch (error) {
      console.error('Failed to fetch trucks:', error);
      throw error;
    }
  }

  // Assign truck to gate
  async assignTruckToGate(centerId: string, gateId: string, truckId: string): Promise<Gate> {
    try {
      const response = await apiService.post<Gate>(
        `/separation-centers/${centerId}/gates/${gateId}/assign-truck`,
        { truckId }
      );
      return response.data; // CORRECTED: Return the 'data' property
    } catch (error) {
      console.error(`Failed to assign truck ${truckId} to gate ${gateId}:`, error);
      throw error;
    }
  }

  // Get real-time center statistics
  async getCenterStatistics(centerId: string): Promise<{
    totalGates: number;
    activeGates: number;
    totalParcels: number;
    totalCapacity: number;
  }> {
    try {
      const response = await apiService.get<{
        totalGates: number;
        activeGates: number;
        totalParcels: number;
        totalCapacity: number;
      }>(`/separation-centers/${centerId}/statistics`);
      return response.data; // CORRECTED: Return the 'data' property
    } catch (error) {
      console.error(`Failed to fetch statistics for center ${centerId}:`, error);
      throw error;
    }
  }
}

export const separationCenterService = new SeparationCenterService();

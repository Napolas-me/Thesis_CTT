// Custom hook for separation center data management
import { useState, useEffect } from 'react';
import { SeparationCenter, Gate } from '../types/parcel';
import { separationCenterService, UpdateGateRequest } from '../services/separationCenterService';

export const useSeparationCenters = () => {
  const [centers, setCenters] = useState<SeparationCenter[]>([]);
  const [selectedCenter, setSelectedCenter] = useState<SeparationCenter | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Load initial data
  useEffect(() => {
    loadCenters();
  }, []);

  const loadCenters = async () => {
    try {
      setLoading(true);
      setError(null);
      
      const centersData = await separationCenterService.getAllCenters();
      setCenters(centersData);
      
      if (centersData.length > 0 && !selectedCenter) {
        setSelectedCenter(centersData[0]);
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load separation centers');
      console.error('Error loading centers:', err);
    } finally {
      setLoading(false);
    }
  };

  const selectCenter = async (centerId: string) => {
    try {
      setError(null);
      const center = await separationCenterService.getCenterById(centerId);
      setSelectedCenter(center);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load center');
      console.error('Error selecting center:', err);
    }
  };

  const updateGate = async (gateId: string, updateData: UpdateGateRequest) => {
    if (!selectedCenter) return;

    try {
      setError(null);
      
      const updatedGate = await separationCenterService.updateGate(
        selectedCenter.id,
        gateId,
        updateData
      );

      // Update local state
      setSelectedCenter(prev => {
        if (!prev) return prev;
        
        return {
          ...prev,
          gates: prev.gates.map(gate =>
            gate.id === gateId ? updatedGate : gate
          )
        };
      });

      setCenters(prev =>
        prev.map(center =>
          center.id === selectedCenter.id
            ? {
                ...center,
                gates: center.gates.map(gate =>
                  gate.id === gateId ? updatedGate : gate
                )
              }
            : center
        )
      );

      return updatedGate;
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to update gate');
      throw err;
    }
  };

  const assignTruckToGate = async (gateId: string, truckId: string) => {
    if (!selectedCenter) return;

    try {
      setError(null);
      
      const updatedGate = await separationCenterService.assignTruckToGate(
        selectedCenter.id,
        gateId,
        truckId
      );

      // Update local state
      setSelectedCenter(prev => {
        if (!prev) return prev;
        
        return {
          ...prev,
          gates: prev.gates.map(gate =>
            gate.id === gateId ? updatedGate : gate
          )
        };
      });

      return updatedGate;
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to assign truck');
      throw err;
    }
  };

  const refreshCenterData = () => {
    if (selectedCenter) {
      selectCenter(selectedCenter.id);
    } else {
      loadCenters();
    }
  };

  return {
    centers,
    selectedCenter,
    loading,
    error,
    selectCenter,
    updateGate,
    assignTruckToGate,
    refreshCenterData,
  };
};
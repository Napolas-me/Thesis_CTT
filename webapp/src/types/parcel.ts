import { BackendRouteItem } from './backendRouteItem';

export type CorrespondenceType = 'EXPRESSO' | 'VERDE' | 'AZUL' | 'NORMAL'; // Match backend enum names

// Align Parcel interface more closely with backend EntityDTO
export type ParcelStatus = 'created' | 'active' | 'completed'; // Simplified to match primary backend states

export interface Parcel {
  id: number; // Matches backend Integer ID
  name: string;
  description: string;
  type: CorrespondenceType;
  origin: string;
  destination: string;
  maxTransfers: number;
  deadline: string; // ISO date string from backend (e.g., "2025-07-26T18:00:00")
  status: ParcelStatus;
  routeId: number;
  progress?: number; // Frontend concept, 0-100
  estimatedDelivery?: string; // Derived from deadline and route
}

// Keep existing frontend-specific types as they are for now
export interface UserParam {
  origin: string;
  destination: string;
  maxTransfers: number;
  date: Date;
  type: CorrespondenceType;
}

export interface City {
  name: string;
  x: number;
  y: number;
  isCapital?: boolean;
}

export interface Route {
  parcelId: number;
  path: string[];
  sequence: BackendRouteItem[];
  currentPosition: number;
  status: string;
}

export interface Gate {
  id: string;
  number: number;
  truck?: Truck;
  parcelsEntered: number;
  parcelsToEnter: number;
  status: 'empty' | 'loading' | 'ready' | 'departed';
}

export interface Truck {
  id: string;
  name: string;
  route: string[];
  arrivalTime: Date;
  departureTime: Date;
  capacity: number;
  currentLoad: number;
}

export interface SeparationCenter {
  id: string;
  name: string;
  location: string;
  gates: Gate[];
}

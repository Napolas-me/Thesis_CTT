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
  createdDate?: string; // Optional: if backend sends it, keep as string
  updatedDate?: string; // Optional: if backend sends it

  // Frontend-specific derived or temporary fields (not directly from EntityDTO)
  // These will be managed in useParcelData or components
  currentLocation?: string; // Derived from route progress
  route?: string[]; // Derived from backend route sequence, or a simple path string[]
  progress?: number; // Frontend concept, 0-100
  estimatedDelivery?: string; // Derived from deadline and route
}

// Keep existing frontend-specific types as they are for now
export interface UserParam {
  origin: string;
  destination: string;
  maxTransfers: number;
  date: Date; // This is used in frontend forms, might need conversion to deadline: string
  type: CorrespondenceType;
}

export interface City {
  name: string;
  x: number;
  y: number;
  isCapital?: boolean;
}

export interface Route {
  parcelId: number; // Changed to number to match Parcel.id
  path: string[]; // Simple string array for map visualization
  currentPosition: number; // Index in the path array
  status: 'active' | 'completed' | 'delayed'; // Frontend route status
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

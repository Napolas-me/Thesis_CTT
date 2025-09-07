// Application constants and configuration
export const APP_CONFIG = {
  // CORRECTED: Use import.meta.env for Vite environment variables
  // If your .env file has REACT_APP_API_URL, rename it to VITE_REACT_APP_API_URL
  API_BASE_URL: import.meta.env.VITE_REACT_APP_API_URL || 'http://localhost:8080/api',
  REFRESH_INTERVAL: 120000, // 2 minutes
  MAX_RETRIES: 3,
  TIMEOUT: 10000, // 10 seconds
};

export const PARCEL_STATUS = {
  CREATED: 'created', // Corresponds to 'created' in backend
  ACTIVE: 'active',   // Corresponds to 'active' in backend (combines processing/in_transit)
  COMPLETED: 'completed', // Corresponds to 'completed' in backend
  // PENDING, PROCESSING, IN_TRANSIT, DELIVERED, FAILED from previous versions
  // are mapped to 'created', 'active', 'completed' for backend consistency.
  // If your backend has more granular statuses, ensure they are reflected here.
} as const;

export const CORRESPONDENCE_TYPES = {
  NORMAL: 'NORMAL', // Ensure these match the backend ENUM values exactly
  EXPRESSO: 'EXPRESSO',
  AZUL: 'AZUL',
  VERDE: 'VERDE',
} as const;

export const GATE_STATUS = {
  EMPTY: 'empty',
  LOADING: 'loading',
  READY: 'ready',
  DEPARTED: 'departed',
} as const;

export const ROUTE_STATUS = {
  ACTIVE: 'active',
  COMPLETED: 'completed',
  DELAYED: 'delayed', // This might be a frontend-derived status or needs backend support
} as const;

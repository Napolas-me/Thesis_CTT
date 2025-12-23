export interface Stop {
  type: 'stop';
  id?: number;
  stopName: string;
  gateName: string;
  arrivalDate: string; // Represents the LocalDateTime, typically as an ISO 8601 string
  departureDate: string; // Represents the LocalDateTime, typically as an ISO 8601 string
  status: string;
}

export interface Trip {
  type: 'trip';
  id?: number;
  origin: string;
  destination: string;
  departureDate: string; // Represents the LocalDateTime, typically as an ISO 8601 string
  destinationDate: string; // Represents the LocalDateTime, typically as an ISO 8601 string
  status: string;
}

export type BackendRouteItem = Stop | Trip;

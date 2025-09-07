import { SeparationCenter, Gate, Truck } from '../types/parcel';

const mockTrucks: Truck[] = [
  {
    id: 'TRK001',
    name: 'Camião Norte',
    route: ['Lisboa', 'Coimbra', 'Porto', 'Braga'],
    arrivalTime: new Date(Date.now() - 30 * 60 * 1000), // 30 minutes ago
    departureTime: new Date(Date.now() + 45 * 60 * 1000), // 45 minutes from now
    capacity: 150,
    currentLoad: 89,
  },
  {
    id: 'TRK002',
    name: 'Expresso Sul',
    route: ['Lisboa', 'Setúbal', 'Évora', 'Beja', 'Faro'],
    arrivalTime: new Date(Date.now() - 15 * 60 * 1000), // 15 minutes ago
    departureTime: new Date(Date.now() + 25 * 60 * 1000), // 25 minutes from now
    capacity: 120,
    currentLoad: 67,
  },
  {
    id: 'TRK003',
    name: 'Rota Centro',
    route: ['Lisboa', 'Santarém', 'Leiria', 'Coimbra', 'Viseu'],
    arrivalTime: new Date(Date.now() - 45 * 60 * 1000), // 45 minutes ago
    departureTime: new Date(Date.now() + 15 * 60 * 1000), // 15 minutes from now
    capacity: 100,
    currentLoad: 95,
  },
];

const mockGates: Gate[] = [
  {
    id: 'GATE001',
    number: 1,
    truck: mockTrucks[0],
    parcelsEntered: 89,
    parcelsToEnter: 23,
    status: 'loading',
  },
  {
    id: 'GATE002',
    number: 2,
    truck: mockTrucks[1],
    parcelsEntered: 67,
    parcelsToEnter: 15,
    status: 'loading',
  },
  {
    id: 'GATE003',
    number: 3,
    truck: mockTrucks[2],
    parcelsEntered: 95,
    parcelsToEnter: 5,
    status: 'ready',
  },
  {
    id: 'GATE004',
    number: 4,
    status: 'empty',
    parcelsEntered: 0,
    parcelsToEnter: 0,
  },
  {
    id: 'GATE005',
    number: 5,
    status: 'empty',
    parcelsEntered: 0,
    parcelsToEnter: 0,
  },
  {
    id: 'GATE006',
    number: 6,
    status: 'empty',
    parcelsEntered: 0,
    parcelsToEnter: 0,
  },
];

export const separationCenters: SeparationCenter[] = [
  {
    id: 'SC001',
    name: 'Centro de Separação Lisboa',
    location: 'Lisboa',
    gates: mockGates,
  },
  {
    id: 'SC002',
    name: 'Centro de Separação Porto',
    location: 'Porto',
    gates: mockGates.slice(0, 4).map(gate => ({
      ...gate,
      id: gate.id.replace('001', '101').replace('002', '102').replace('003', '103').replace('004', '104'),
    })),
  },
];
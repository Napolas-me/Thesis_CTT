import React from 'react';
import { Parcel, Route } from '../../types/parcel'; // Import Parcel and Route types

interface RunningProcessesProps {
  parcels: Parcel[];
  selectedParcelId?: number; // Changed to number to match Parcel.id
  routes: Route[]; // Routes prop added
  onSelectParcel: (parcelId: number) => void; // Changed to number
}

const RunningProcesses: React.FC<RunningProcessesProps> = ({
  parcels,
  selectedParcelId,
  routes, // Destructure routes
  onSelectParcel,
}) => {
  // Filter parcels that are 'active' to be considered "running processes"
  const runningParcels = parcels.filter(
    (parcel) => parcel.status === 'active'
  );

  return (
    <div className="bg-white shadow-lg rounded-lg p-6">
      <h2 className="text-2xl font-bold text-gray-800 mb-6">Processos em Execução</h2>
      {runningParcels.length === 0 ? (
        <p className="text-gray-600 text-center py-8">
          Nenhum processo em execução no momento.
        </p>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {runningParcels.map((parcel) => {
            // Find the associated route for the current parcel
            const associatedRoute = routes.find(route => route.parcelId === parcel.id);
            return (
              <div
                key={parcel.id}
                className={`border rounded-lg p-4 cursor-pointer transition-all ${
                  selectedParcelId === parcel.id
                    ? 'border-blue-500 bg-blue-50 shadow-md'
                    : 'border-gray-200 hover:border-blue-300'
                }`}
                onClick={() => onSelectParcel(parcel.id)}
              >
                <h3 className="text-lg font-semibold text-gray-900 mb-2">
                  Encomenda ID: {parcel.id}
                </h3>
                <p className="text-gray-700 text-sm mb-1">
                  <span className="font-medium">Nome:</span> {parcel.name}
                </p>
                <p className="text-gray-700 text-sm mb-1">
                  <span className="font-medium">Origem:</span> {parcel.origin}
                </p>
                <p className="text-gray-700 text-sm mb-1">
                  <span className="font-medium">Destino:</span> {parcel.destination}
                </p>
                <p className="text-gray-700 text-sm mb-1">
                  <span className="font-medium">Status:</span>{' '}
                  <span
                    className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                      parcel.status === 'active'
                        ? 'bg-blue-100 text-blue-800'
                        : 'bg-gray-100 text-gray-800'
                    }`}
                  >
                    {parcel.status.toUpperCase()}
                  </span>
                </p>
                {/* Display route information if available */}
                {associatedRoute && (
                  <div className="mt-3 pt-3 border-t border-gray-200">
                    <p className="text-gray-700 text-sm font-medium mb-1">Rota Atribuída:</p>
                    <p className="text-gray-600 text-xs">
                      {/* Display route path */}
                      {associatedRoute.path.join(' → ')}
                    </p>
                    {/* You can add more route details here if needed, e.g., estimated times from backend RouteDTO */}
                  </div>
                )}
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
};

export default RunningProcesses;

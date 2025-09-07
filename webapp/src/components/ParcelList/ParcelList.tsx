import { CheckCircle, CircleDot, Clock, MapPin, Truck } from 'lucide-react';
import React from 'react';
import { Parcel, ParcelStatus } from '../../types/parcel';
import { formatDateTime } from '../../utils/formatters';

interface ParcelListProps {
  parcels: Parcel[];
  selectedParcelId?: number;
  onParcelSelect: (parcelId: number) => void;
  onViewOnMap: (parcelId: number) => void;
  onStartProcessing: (parcelId: number) => void;
}

const ParcelList: React.FC<ParcelListProps> = ({
  parcels,
  selectedParcelId,
  onParcelSelect,
  onViewOnMap,
  onStartProcessing,
}) => {
  const safeParcels = parcels || [];
  console.log('ParcelList: Component rendering. Received parcels prop:', safeParcels);
  console.log('ParcelList: Number of parcels to display:', safeParcels.length);

  const getStatusIcon = (status: ParcelStatus) => {
    switch (status) {
      case 'created':
        return <CircleDot className="w-4 h-4 text-gray-500" />;
      case 'active':
        return <Truck className="w-4 h-4 text-blue-500" />;
      case 'completed':
        return <CheckCircle className="w-4 h-4 text-green-500" />;
      default:
        return <CircleDot className="w-4 h-4 text-gray-500" />;
    }
  };

  const getStatusColorClass = (status: ParcelStatus) => {
    switch (status) {
      case 'created':
        return 'bg-gray-100 text-gray-800';
      case 'active':
        return 'bg-blue-100 text-blue-800';
      case 'completed':
        return 'bg-green-100 text-green-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  return (
    <div className="bg-white shadow-lg rounded-lg p-6">
      <h2 className="text-2xl font-bold text-gray-800 mb-6">Todas as Encomendas</h2>
      {safeParcels.length === 0 && (
        <p className="text-gray-600 text-center py-8">Nenhuma encomenda encontrada. Adicione uma nova encomenda para começar.</p>
      )}
      <div className="overflow-x-auto">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                ID
              </th>
              <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Nome
              </th>
              <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Tipo
              </th>
              <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Origem
              </th>
              <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Destino
              </th>
              <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Deadline
              </th>
              <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Status
              </th>
              <th scope="col" className="relative px-6 py-3 w-48 text-center">
                <span className="sr-only">Ações</span>
                Ações
              </th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {safeParcels.map((parcel) => (
              <tr
                key={parcel.id}
                className={`hover:bg-gray-50 cursor-pointer ${
                  selectedParcelId === parcel.id ? 'bg-blue-50' : ''
                }`}
                onClick={() => onParcelSelect(parcel.id)}
              >
                <td className="px-6 py-4 text-sm font-medium text-gray-900">
                  {parcel.id}
                </td>
                <td className="px-6 py-4 text-sm text-gray-900">
                  {parcel.name}
                </td>
                <td className="px-6 py-4 text-sm text-gray-900">
                  {parcel.type}
                </td>
                <td className="px-6 py-4 text-sm text-gray-900">
                  <div className="flex items-center">
                    <MapPin className="w-4 h-4 text-gray-400 mr-1" />
                    {parcel.origin}
                  </div>
                </td>
                <td className="px-6 py-4 text-sm text-gray-900">
                  <div className="flex items-center">
                    <MapPin className="w-4 h-4 text-gray-400 mr-1" />
                    {parcel.destination}
                  </div>
                </td>
                <td className="px-6 py-4 text-sm text-gray-900">
                  <div className="flex items-center">
                    <Clock className="w-4 h-4 text-gray-400 mr-1" />
                    {formatDateTime(new Date(parcel.deadline))}
                  </div>
                </td>
                <td className="px-6 py-4 text-sm text-gray-900">
                  <span
                    className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getStatusColorClass(parcel.status)}`}
                  >
                    {getStatusIcon(parcel.status)}
                    <span className="ml-1">{parcel.status.toUpperCase()}</span>
                  </span>
                </td>
                {/* Ações: Usando flexbox para alinhar os botões na mesma linha */}
                <td className="px-6 py-4 text-right text-sm font-medium">
                  <div className="flex items-center justify-end space-x-2">
                    {parcel.status === 'created' && (
                      <button
                        onClick={(e) => {
                          e.stopPropagation();
                          onStartProcessing(parcel.id);
                        }}
                        className="text-white bg-green-600 hover:bg-green-700 px-3 py-1 rounded-md text-xs font-semibold transition-colors"
                      >
                        Processar
                      </button>
                    )}
                    <button
                      onClick={(e) => {
                        e.stopPropagation();
                        onViewOnMap(parcel.id);
                      }}
                      className="px-3 py-1 rounded-md text-xs font-semibold text-blue-600 border border-blue-600 hover:bg-blue-50 transition-colors"
                    >
                      Mapa
                    </button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default ParcelList;

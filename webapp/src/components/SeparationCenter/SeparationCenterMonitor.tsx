import React, { useState, useEffect } from 'react';
import { SeparationCenter, Gate } from '../../types/parcel';
import { separationCenters } from '../../data/separationCenters';
import { Truck, Clock, Package, Users, AlertCircle, CheckCircle } from 'lucide-react';

interface SeparationCenterMonitorProps {
  onSelectGate?: (gateId: string) => void;
}

const SeparationCenterMonitor: React.FC<SeparationCenterMonitorProps> = ({ onSelectGate }) => {
  const [selectedCenter, setSelectedCenter] = useState<SeparationCenter>(separationCenters[0]);
  const [selectedGate, setSelectedGate] = useState<Gate | null>(null);
  const [currentTime, setCurrentTime] = useState(new Date());

  useEffect(() => {
    const timer = setInterval(() => {
      setCurrentTime(new Date());
    }, 1000);
    return () => clearInterval(timer);
  }, []);

  const getGateStatusColor = (status: string) => {
    switch (status) {
      case 'loading': return 'bg-blue-100 text-blue-800 border-blue-200';
      case 'ready': return 'bg-green-100 text-green-800 border-green-200';
      case 'departed': return 'bg-gray-100 text-gray-800 border-gray-200';
      case 'empty': return 'bg-yellow-100 text-yellow-800 border-yellow-200';
      default: return 'bg-gray-100 text-gray-800 border-gray-200';
    }
  };

  const getGateStatusIcon = (status: string) => {
    switch (status) {
      case 'loading': return <Package className="w-4 h-4" />;
      case 'ready': return <CheckCircle className="w-4 h-4" />;
      case 'departed': return <Truck className="w-4 h-4" />;
      case 'empty': return <AlertCircle className="w-4 h-4" />;
      default: return <Clock className="w-4 h-4" />;
    }
  };

  const formatTimeUntilDeparture = (departureTime: Date) => {
    const diff = departureTime.getTime() - currentTime.getTime();
    if (diff <= 0) return 'Partiu';
    
    const minutes = Math.floor(diff / (1000 * 60));
    const hours = Math.floor(minutes / 60);
    const remainingMinutes = minutes % 60;
    
    if (hours > 0) {
      return `${hours}h ${remainingMinutes}m`;
    }
    return `${remainingMinutes}m`;
  };

  const getCapacityColor = (currentLoad: number, capacity: number) => {
    const percentage = (currentLoad / capacity) * 100;
    if (percentage >= 90) return 'bg-red-500';
    if (percentage >= 75) return 'bg-yellow-500';
    return 'bg-green-500';
  };

  return (
    <div className="bg-white rounded-lg shadow-lg p-6">
      <div className="flex items-center justify-between mb-6">
        <h2 className="text-xl font-semibold text-gray-800">Monitor de Centro de Separação</h2>
        <select
          value={selectedCenter.id}
          onChange={(e) => {
            const center = separationCenters.find(c => c.id === e.target.value);
            if (center) setSelectedCenter(center);
          }}
          className="px-3 py-2 border border-gray-300 rounded-md text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
        >
          {separationCenters.map((center) => (
            <option key={center.id} value={center.id}>
              {center.name}
            </option>
          ))}
        </select>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Gates Grid */}
        <div className="lg:col-span-2">
          <h3 className="text-lg font-medium text-gray-800 mb-4">Portões Disponíveis</h3>
          <div className="grid grid-cols-2 md:grid-cols-3 gap-4">
            {selectedCenter.gates.map((gate) => (
              <div
                key={gate.id}
                className={`border-2 rounded-lg p-4 cursor-pointer transition-all hover:shadow-md ${
                  selectedGate?.id === gate.id
                    ? 'border-blue-500 bg-blue-50'
                    : 'border-gray-200 hover:border-gray-300'
                }`}
                onClick={() => {
                  setSelectedGate(gate);
                  onSelectGate?.(gate.id);
                }}
              >
                <div className="flex items-center justify-between mb-2">
                  <span className="font-semibold text-gray-900">Portão {gate.number}</span>
                  <span className={`px-2 py-1 rounded-full text-xs font-medium border ${getGateStatusColor(gate.status)}`}>
                    {getGateStatusIcon(gate.status)}
                    <span className="ml-1 capitalize">{gate.status}</span>
                  </span>
                </div>
                
                {gate.truck ? (
                  <div className="space-y-2 text-sm">
                    <div className="flex items-center space-x-1">
                      <Truck className="w-3 h-3 text-gray-500" />
                      <span className="font-medium">{gate.truck.name}</span>
                    </div>
                    <div className="text-gray-600">
                      <div>Entrada: {gate.parcelsEntered}</div>
                      <div>Pendente: {gate.parcelsToEnter}</div>
                    </div>
                    <div className="w-full bg-gray-200 rounded-full h-2">
                      <div
                        className={`h-2 rounded-full ${getCapacityColor(gate.truck.currentLoad, gate.truck.capacity)}`}
                        style={{ width: `${(gate.truck.currentLoad / gate.truck.capacity) * 100}%` }}
                      />
                    </div>
                    <div className="text-xs text-gray-500">
                      {gate.truck.currentLoad}/{gate.truck.capacity} ({Math.round((gate.truck.currentLoad / gate.truck.capacity) * 100)}%)
                    </div>
                  </div>
                ) : (
                  <div className="text-sm text-gray-500">
                    <div className="flex items-center space-x-1">
                      <AlertCircle className="w-3 h-3" />
                      <span>Sem camião atribuído</span>
                    </div>
                  </div>
                )}
              </div>
            ))}
          </div>
        </div>

        {/* Gate Details */}
        <div className="lg:col-span-1">
          <h3 className="text-lg font-medium text-gray-800 mb-4">Detalhes do Portão</h3>
          {selectedGate ? (
            <div className="bg-gray-50 rounded-lg p-4 space-y-4">
              <div>
                <h4 className="font-semibold text-gray-900 mb-2">Portão {selectedGate.number}</h4>
                <span className={`px-2 py-1 rounded-full text-xs font-medium border ${getGateStatusColor(selectedGate.status)}`}>
                  {getGateStatusIcon(selectedGate.status)}
                  <span className="ml-1 capitalize">{selectedGate.status}</span>
                </span>
              </div>

              {selectedGate.truck ? (
                <div className="space-y-3">
                  <div>
                    <label className="text-sm font-medium text-gray-600">Camião</label>
                    <p className="text-gray-900">{selectedGate.truck.name}</p>
                  </div>

                  <div>
                    <label className="text-sm font-medium text-gray-600">Rota</label>
                    <p className="text-gray-900 text-sm">{selectedGate.truck.route.join(' → ')}</p>
                  </div>

                  <div className="grid grid-cols-2 gap-3">
                    <div>
                      <label className="text-sm font-medium text-gray-600">Chegada</label>
                      <p className="text-gray-900 text-sm">
                        {selectedGate.truck.arrivalTime.toLocaleTimeString('pt-PT', { 
                          hour: '2-digit', 
                          minute: '2-digit' 
                        })}
                      </p>
                    </div>
                    <div>
                      <label className="text-sm font-medium text-gray-600">Partida</label>
                      <p className="text-gray-900 text-sm">
                        {selectedGate.truck.departureTime.toLocaleTimeString('pt-PT', { 
                          hour: '2-digit', 
                          minute: '2-digit' 
                        })}
                      </p>
                    </div>
                  </div>

                  <div>
                    <label className="text-sm font-medium text-gray-600">Tempo até Partida</label>
                    <p className={`font-semibold ${
                      formatTimeUntilDeparture(selectedGate.truck.departureTime) === 'Partiu' 
                        ? 'text-red-600' 
                        : 'text-blue-600'
                    }`}>
                      {formatTimeUntilDeparture(selectedGate.truck.departureTime)}
                    </p>
                  </div>

                  <div className="grid grid-cols-2 gap-3">
                    <div>
                      <label className="text-sm font-medium text-gray-600">Encomendas Entrada</label>
                      <p className="text-gray-900 font-semibold">{selectedGate.parcelsEntered}</p>
                    </div>
                    <div>
                      <label className="text-sm font-medium text-gray-600">Por Entrar</label>
                      <p className="text-gray-900 font-semibold">{selectedGate.parcelsToEnter}</p>
                    </div>
                  </div>

                  <div>
                    <label className="text-sm font-medium text-gray-600">Capacidade do Camião</label>
                    <div className="mt-1">
                      <div className="flex items-center justify-between text-sm mb-1">
                        <span>{selectedGate.truck.currentLoad}/{selectedGate.truck.capacity}</span>
                        <span>{Math.round((selectedGate.truck.currentLoad / selectedGate.truck.capacity) * 100)}%</span>
                      </div>
                      <div className="w-full bg-gray-200 rounded-full h-3">
                        <div
                          className={`h-3 rounded-full transition-all duration-500 ${getCapacityColor(selectedGate.truck.currentLoad, selectedGate.truck.capacity)}`}
                          style={{ width: `${(selectedGate.truck.currentLoad / selectedGate.truck.capacity) * 100}%` }}
                        />
                      </div>
                    </div>
                  </div>

                  <div>
                    <label className="text-sm font-medium text-gray-600">Espaço Livre</label>
                    <p className="text-gray-900 font-semibold">
                      {selectedGate.truck.capacity - selectedGate.truck.currentLoad} encomendas
                    </p>
                  </div>
                </div>
              ) : (
                <div className="text-center py-8 text-gray-500">
                  <Truck className="w-12 h-12 mx-auto mb-2 text-gray-400" />
                  <p>Nenhum camião atribuído a este portão</p>
                </div>
              )}
            </div>
          ) : (
            <div className="text-center py-8 text-gray-500">
              <Users className="w-12 h-12 mx-auto mb-2 text-gray-400" />
              <p>Selecione um portão para ver os detalhes</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default SeparationCenterMonitor;
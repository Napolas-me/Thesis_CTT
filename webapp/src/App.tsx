import { Activity, Map, Package, Plus, Truck } from 'lucide-react'; // Import RefreshCw icon
import { useState, useEffect } from 'react';
import AddParcelForm from './components/AddParcelForm/AddParcelForm';
import PortugalMap from './components/Map/PortugalMap';
import ParcelList from './components/ParcelList/ParcelList';
import RunningProcesses from './components/RunningProcesses/RunningProcesses';
import SeparationCenterMonitor from './components/SeparationCenter/SeparationCenterMonitor';
import { useParcelData } from './hooks/useParcelData';

type ActiveTab = 'parcels' | 'processes' | 'map' | 'separation';

function App() {
  const [activeTab, setActiveTab] = useState<ActiveTab>('parcels');
  const [selectedParcelId, setSelectedParcelId] = useState<number | undefined>();
  const [showAddForm, setShowAddForm] = useState(false);
  const { parcels, routes, addParcel, updateParcelStatus, calculateAndSetRoute, refreshData } = useParcelData();

  useEffect(() => {
    refreshData();
  }, [refreshData]); // Added refreshData to dependency array for safety, although it's stable

  const handleParcelSelect = (parcelId: number) => {
    setSelectedParcelId(parcelId);
  };

  const handleViewOnMap = async (parcelId: number) => {
    setSelectedParcelId(parcelId);
    setActiveTab('map');
    // When viewing on map, calculate the route for this parcel
    try {
      await calculateAndSetRoute(parcelId);
    } catch (error) {
      console.error('Failed to calculate route for map view:', error);
    }
  };

  const handleAddParcel = (parcelData: Parameters<typeof addParcel>[0]) => {
    addParcel(parcelData);
    setShowAddForm(false);
  };

  // Fixed handler for starting parcel processing
  const handleStartProcessing = async (parcelId: number) => {
    console.log(`Attempting to start processing for parcel ID: ${parcelId}`);
    try {
      // Step 1: Attempt to calculate the route first
      // The calculateAndSetRoute function already fetches parcel details if needed.
      await calculateAndSetRoute(parcelId);
      console.log(`Route successfully calculated for parcel ID: ${parcelId}.`);

      // Step 2: If route calculation is successful, then update the status to 'active'
      await updateParcelStatus(parcelId, 'active', 0); // 0 progress initially
      console.log(`Parcel ID: ${parcelId} status updated to 'active'.`);

      setActiveTab('processes'); // Switch to processes tab to see it
    } catch (error) {
      // Step 3: If route calculation or status update fails, catch the error
      console.error(`Failed to start processing for parcel ID: ${parcelId}:`, error);
      alert(`Erro ao iniciar processamento para a encomenda ${parcelId}. Verifique o console para mais detalhes.`);
    }
  };

  const renderContent = () => {
    switch (activeTab) {
      case 'parcels':
        return (
          <ParcelList
            parcels={parcels}
            selectedParcelId={selectedParcelId}
            onParcelSelect={handleParcelSelect}
            onViewOnMap={handleViewOnMap}
            onStartProcessing={handleStartProcessing}
          />
        );
      case 'processes':
        return (
          <RunningProcesses
            parcels={parcels}
            selectedParcelId={selectedParcelId}
            routes={routes}
            onSelectParcel={handleParcelSelect}
          />
        );
      case 'map':
        return (
          <div className="h-[calc(100vh-200px)]">
            <PortugalMap
              selectedParcelId={selectedParcelId}
              routes={routes}
              onCityClick={(city) => console.log('City clicked:', city)}
            />
          </div>
        );
      case 'separation':
        return (
          <SeparationCenterMonitor
            onSelectGate={(gateId) => console.log('Gate selected:', gateId)}
          />
        );
      default:
        return null;
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white shadow-sm border-b">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-16">
            <div className="flex items-center space-x-3">
              <Package className="w-8 h-8 text-blue-600" />
              <h1 className="text-2xl font-bold text-gray-900">Sistema de Gestão de Encomendas CTT</h1>
            </div>
            <div className="flex items-center space-x-4">
              <button
                onClick={() => setShowAddForm(true)}
                className="flex items-center space-x-2 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
              >
                <Plus className="w-4 h-4" />
                <span>Nova Encomenda</span>
              </button>
            </div>
          </div>
        </div>
      </header>

      {/* Navigation */}
      <nav className="bg-white border-b">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex space-x-8">
            <button
              onClick={() => setActiveTab('parcels')}
              className={`flex items-center space-x-2 px-3 py-4 text-sm font-medium border-b-2 transition-colors ${
                activeTab === 'parcels'
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              <Package className="w-4 h-4" />
              <span>Encomendas</span>
            </button>
            <button
              onClick={() => setActiveTab('processes')}
              className={`flex items-center space-x-2 px-3 py-4 text-sm font-medium border-b-2 transition-colors ${
                activeTab === 'processes'
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              <Activity className="w-4 h-4" />
              <span>Processos em Execução</span>
            </button>
            <button
              onClick={() => setActiveTab('map')}
              className={`flex items-center space-x-2 px-3 py-4 text-sm font-medium border-b-2 transition-colors ${
                activeTab === 'map'
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              <Map className="w-4 h-4" />
              <span>Mapa</span>
            </button>
            <button
              onClick={() => setActiveTab('separation')}
              className={`flex items-center space-x-2 px-3 py-4 text-sm font-medium border-b-2 transition-colors ${
                activeTab === 'separation'
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              <Truck className="w-4 h-4" />
              <span>Centro de Separação</span>
            </button>
          </div>
        </div>
      </nav>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {renderContent()}
      </main>

      {/* Add Parcel Modal */}
      {showAddForm && (
        <AddParcelForm
          onAddParcel={handleAddParcel}
          onClose={() => setShowAddForm(false)}
        />
      )}
    </div>
  );
}

export default App;

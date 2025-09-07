import React, { useEffect, useRef, useState } from 'react';
import { portugueseCities } from '../../data/cities';
import { Route as FrontendRouteType } from '../../types/parcel'; // Import FrontendRouteType, Trip, Stop

interface PortugalMapProps {
  selectedParcelId?: number; // Changed to number
  routes: FrontendRouteType[]; // Now expects FrontendRouteType
  onCityClick?: (city: string) => void;
}

const PortugalMap: React.FC<PortugalMapProps> = ({ selectedParcelId, routes, onCityClick }) => {
  const canvasRef = useRef<HTMLCanvasElement>(null);
  const [mapDimensions, setMapDimensions] = useState({ width: 0, height: 0 });

  // Adjust map dimensions based on container size
  useEffect(() => {
    const updateDimensions = () => {
      if (canvasRef.current) {
        const parent = canvasRef.current.parentElement;
        if (parent) {
          // Set dimensions to fill parent, but with a max-height to avoid excessive stretching
          const aspectRatio = 600 / 800; // Original SVG viewBox ratio
          let width = parent.clientWidth;
          let height = parent.clientHeight;

          // Maintain aspect ratio if needed, or simply fill parent
          // For now, let's just fill parent, assuming parent has controlled height
          setMapDimensions({ width, height });
        }
      }
    };

    updateDimensions();
    window.addEventListener('resize', updateDimensions);
    return () => window.removeEventListener('resize', updateDimensions);
  }, []);

  useEffect(() => {
    const canvas = canvasRef.current;
    if (!canvas) return;

    const ctx = canvas.getContext('2d');
    if (!ctx) return;

    // Set canvas dimensions explicitly for drawing quality
    canvas.width = mapDimensions.width;
    canvas.height = mapDimensions.height;

    // Clear canvas
    ctx.clearRect(0, 0, mapDimensions.width, mapDimensions.height);

    // Draw background (simple rectangle for Portugal outline)
    ctx.fillStyle = '#f0f9ff'; // Light blue for land
    ctx.fillRect(0, 0, mapDimensions.width, mapDimensions.height);

    // Define a padding/margin for the drawing area within the canvas
    const padding = 50;
    const drawingWidth = mapDimensions.width - 2 * padding;
    const drawingHeight = mapDimensions.height - 2 * padding;

    // Normalize coordinates to fit canvas drawing area
    const minOriginalX = Math.min(...portugueseCities.map(c => c.x));
    const maxOriginalX = Math.max(...portugueseCities.map(c => c.x));
    const minOriginalY = Math.min(...portugueseCities.map(c => c.y));
    const maxOriginalY = Math.max(...portugueseCities.map(c => c.y));

    // Calculate scaling factors
    const scaleX = drawingWidth / (maxOriginalX - minOriginalX);
    const scaleY = drawingHeight / (maxOriginalY - minOriginalY);
    const scale = Math.min(scaleX, scaleY); // Use the smaller scale to fit both dimensions

    // Calculate offsets to center the map
    const offsetX = padding + (drawingWidth - (maxOriginalX - minOriginalX) * scale) / 2 - minOriginalX * scale;
    const offsetY = padding + (drawingHeight - (maxOriginalY - minOriginalY) * scale) / 2 - minOriginalY * scale;

    const getCanvasCoords = (cityX: number, cityY: number) => ({
      x: cityX * scale + offsetX,
      y: cityY * scale + offsetY,
    });

    // Draw routes
    routes.forEach(route => {
      if (route.sequence && route.sequence.length > 1) {
        ctx.beginPath();
        let currentCoords: { x: number; y: number } | null = null;

        route.sequence.forEach((item, index) => {
          let cityToDraw: string | undefined;

          if ('origin' in item && 'destination' in item) { // It's a Trip
            // For a trip, draw from origin to destination
            const originCity = portugueseCities.find(c => c.name === item.origin);
            const destinationCity = portugueseCities.find(c => c.name === item.destination);

            if (originCity && destinationCity) {
              const startCoords = getCanvasCoords(originCity.x, originCity.y);
              const endCoords = getCanvasCoords(destinationCity.x, destinationCity.y);

              if (index === 0) { // Move to the start of the first trip
                ctx.moveTo(startCoords.x, startCoords.y);
              } else if (currentCoords && (currentCoords.x !== startCoords.x || currentCoords.y !== startCoords.y)) {
                // If previous item was a stop or different trip, ensure connection
                ctx.lineTo(startCoords.x, startCoords.y);
              }
              ctx.lineTo(endCoords.x, endCoords.y);
              currentCoords = endCoords; // Update current position
            }
          } else if ('stopName' in item) { // It's a Stop
            cityToDraw = item.stopName;
            const stopCity = portugueseCities.find(c => c.name === cityToDraw);
            if (stopCity) {
              const coords = getCanvasCoords(stopCity.x, stopCity.y);
              if (index === 0) {
                ctx.moveTo(coords.x, coords.y);
              } else if (currentCoords && (currentCoords.x !== coords.x || currentCoords.y !== coords.y)) {
                ctx.lineTo(coords.x, coords.y);
              }
              currentCoords = coords; // Update current position
            }
          }
        });

        // Determine route color and style
        const isSelected = selectedParcelId === route.parcelId; // Use parcelId for highlighting
        ctx.strokeStyle = isSelected ? '#3B82F6' : '#6B7280'; // Blue if selected, gray otherwise
        ctx.lineWidth = isSelected ? 5 : 3;
        ctx.lineDashOffset = 0; // Reset dash offset
        ctx.setLineDash([]); // Solid line by default

        if (route.status === 'active') {
          ctx.setLineDash([10, 5]); // Dashed line for active
          // Optional: Animate dash offset for "moving" effect
          // ctx.lineDashOffset = (performance.now() / 50) % 15;
        } else if (route.status === 'completed') {
          ctx.strokeStyle = '#10B981'; // Green for completed
        } else if (route.status === 'delayed') { // Assuming a 'delayed' status might exist
          ctx.strokeStyle = '#F59E0B'; // Orange for delayed
        }

        ctx.stroke();
      }
    });


    // Draw cities
    portugueseCities.forEach(city => {
      const { x, y } = getCanvasCoords(city.x, city.y);

      // Draw city circle
      ctx.beginPath();
      ctx.arc(x, y, city.isCapital ? 10 : 7, 0, Math.PI * 2); // Larger for capitals
      ctx.fillStyle = city.isCapital ? '#DC2626' : '#374151'; // Red for capitals, dark gray for others
      ctx.fill();
      ctx.strokeStyle = '#FFFFFF';
      ctx.lineWidth = 2;
      ctx.stroke();

      // Draw city name
      ctx.fillStyle = '#333333';
      ctx.font = '12px Inter';
      ctx.textAlign = 'center';
      ctx.textBaseline = 'bottom';
      ctx.fillText(city.name, x, y - (city.isCapital ? 15 : 10)); // Adjust text position for capitals
    });

    // Handle click events
    const handleClick = (event: MouseEvent) => {
      const rect = canvas.getBoundingClientRect();
      const mouseX = event.clientX - rect.left;
      const mouseY = event.clientY - rect.top;

      portugueseCities.forEach(city => {
        const { x, y } = getCanvasCoords(city.x, city.y);
        const radius = city.isCapital ? 15 : 10; // Larger click radius for capitals
        const distance = Math.sqrt(Math.pow(mouseX - x, 2) + Math.pow(mouseY - y, 2));
        if (distance < radius) {
          onCityClick?.(city.name); // Use optional chaining for onCityClick
        }
      });
    };

    canvas.addEventListener('click', handleClick);

    return () => {
      canvas.removeEventListener('click', handleClick);
    };
  }, [mapDimensions, routes, selectedParcelId, onCityClick]); // Redraw when dimensions, routes, selectedParcelId, or click handler changes

  return (
    <div className="bg-white rounded-lg shadow-md p-6 h-full flex flex-col">
      <div className="flex items-center justify-between mb-4">
        <h2 className="text-2xl font-bold text-gray-900">Mapa de Portugal</h2>
        <div className="flex items-center space-x-4 text-sm text-gray-600">
          <div className="flex items-center space-x-1">
            <div className="w-3 h-3 bg-blue-500 rounded-full"></div>
            <span>Ativo</span>
          </div>
          <div className="flex items-center space-x-1">
            <div className="w-3 h-3 bg-green-500 rounded-full"></div>
            <span>Completo</span>
          </div>
          <div className="flex items-center space-x-1">
            <div className="w-3 h-3 bg-yellow-500 rounded-full"></div>
            <span>Atrasado (Exemplo)</span> {/* Clarified this is an example status */}
          </div>
        </div>
      </div>

      <div className="relative flex-grow min-h-[400px]"> {/* Use flex-grow to fill available space */}
        <canvas
          ref={canvasRef}
          className="w-full h-full border-2 border-gray-300 rounded-xl bg-gradient-to-br from-blue-50 via-blue-100 to-green-50"
        >
          Seu navegador não suporta o elemento canvas.
        </canvas>
      </div>

      {selectedParcelId !== undefined && routes.length > 0 && (
        // Find the specific route that matches the selectedParcelId
        // This assumes that when a route is calculated and stored, it has the parcelId attached
        // If multiple routes could be associated with one parcel, this logic would need refinement.
        routes.find(r => r.parcelId === selectedParcelId) && (
          <div className="mt-4 p-4 bg-blue-50 rounded-lg">
            <h3 className="text-lg font-semibold text-blue-900 mb-3">Rota Selecionada</h3>
            <div className="text-sm text-blue-800">
              <p><strong>Encomenda ID:</strong> {selectedParcelId}</p>
              {/* Display details of the found route */}
              {routes.filter(r => r.parcelId === selectedParcelId).map(route => (
                <div key={route.id}>
                  <p><strong>Nome da Rota:</strong> {route.name}</p>
                  <p><strong>Status:</strong> {route.status}</p>
                  <p><strong>Origem:</strong> {route.routeStartName}</p>
                  <p><strong>Destino:</strong> {route.routeEndName}</p>
                  <p><strong>Início:</strong> {new Date(route.routeStartDate).toLocaleString('pt-PT')}</p>
                  <p><strong>Fim:</strong> {new Date(route.routeEndDate).toLocaleString('pt-PT')}</p>
                  <p><strong>Sequência:</strong> {route.sequence?.map(item => {
                    if ('origin' in item) return `${item.origin}→${item.destination}`;
                    if ('stopName' in item) return item.stopName;
                    return '';
                  }).join(' → ')}</p>
                </div>
              ))}
            </div>
          </div>
        )
      )}

      {/* Monitor Access Information */}
      <div className="mt-4 p-4 bg-yellow-50 rounded-lg border border-yellow-200">
        <h3 className="text-lg font-semibold text-yellow-800 mb-2">Acesso aos Centros de Separação (Standalone)</h3>
        <p className="text-sm text-yellow-700 mb-2">
          Para aceder ao monitor dos centros de separação (para monitores da linha de montagem), abra este URL diretamente no seu navegador:
        </p>
        <div className="bg-yellow-100 p-2 rounded border text-sm font-mono text-yellow-800 break-all">
          {window.location.origin}/monitor.html
        </div>
      </div>
    </div>
  );
};

export default PortugalMap;

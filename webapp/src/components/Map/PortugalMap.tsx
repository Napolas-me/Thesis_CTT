import React, { useEffect, useRef, useState } from 'react';
import { portugueseCities } from '../../data/cities';
import { Route as FrontendRouteType } from '../../types/parcel'; // Import FrontendRouteType, Trip, Stop

interface PortugalMapProps {
  selectedParcelId?: number; // Changed to number
  routes: FrontendRouteType[]; // Now expects FrontendRouteType
  onCityClick?: (city: string) => void;
}

const dummyRoutes = [
  {
    id: 'route1',
    parcelId: 'parcel-12345',
    name: 'Rota do Norte',
    status: 'active',
    routeStartName: 'Porto',
    routeEndName: 'Braga',
    routeStartDate: Date.now(),
    routeEndDate: Date.now() + 1000 * 60 * 60,
    sequence: [
      { origin: 'Porto', destination: 'Braga' },
    ],
  },
  {
    id: 'route2',
    parcelId: 'parcel-67890',
    name: 'Rota do Sul',
    status: 'completed',
    routeStartName: 'Lisboa',
    routeEndName: 'Faro',
    routeStartDate: Date.now() - 1000 * 60 * 60 * 24,
    routeEndDate: Date.now() - 1000 * 60 * 60 * 12,
    sequence: [
      { origin: 'Lisboa', destination: 'Évora' },
      { origin: 'Évora', destination: 'Beja' },
      { origin: 'Beja', destination: 'Faro' },
    ],
  },
  {
    id: 'route3',
    parcelId: 'parcel-11223',
    name: 'Rota do Centro',
    status: 'delayed',
    routeStartName: 'Coimbra',
    routeEndName: 'Lisboa',
    routeStartDate: Date.now() - 1000 * 60 * 60 * 6,
    routeEndDate: Date.now() + 1000 * 60 * 60 * 2,
    sequence: [
      { origin: 'Coimbra', destination: 'Leiria' },
      { origin: 'Leiria', destination: 'Santarém' },
      { origin: 'Santarém', destination: 'Lisboa' },
    ],
  },
  {
    id: 'route4',
    parcelId: 'parcel-44556',
    name: 'Rota Transmontana',
    status: 'active',
    routeStartName: 'Vila Real',
    routeEndName: 'Bragança',
    routeStartDate: Date.now(),
    routeEndDate: Date.now() + 1000 * 60 * 60 * 3,
    sequence: [
      { origin: 'Vila Real', destination: 'Bragança' }
    ]
  },
];

const PortugalMap = ({ selectedParcelId, routes, onCityClick }) => {
  const canvasRef = useRef(null);
  const [mapImage, setMapImage] = useState(null);
  const [animationProgress, setAnimationProgress] = useState(0);
  const [displayRoutes, setDisplayRoutes] = useState(dummyRoutes);
  const fixedDimensions = { width: 330, height: 600 };

  // Verifica se o array de rotas não está vazio e o utiliza, caso contrário, usa as rotas de exemplo.
  useEffect(() => {
    if (Array.isArray(routes) && routes.length > 0) {
      setDisplayRoutes(routes);
    } else {
      setDisplayRoutes(dummyRoutes);
    }
  }, [routes]);

  useEffect(() => {
    const img = new Image();
    img.src = 'https://upload.wikimedia.org/wikipedia/commons/thumb/4/48/Portugal_location_map.svg/330px-Portugal_location_map.svg.png';
    img.onload = () => {
      setMapImage(img);
    };
    img.onerror = () => {
      console.error("Failed to load map image.");
    };
  }, []);

  useEffect(() => {
    let animationFrameId;
    const animate = () => {
      setAnimationProgress(prev => (prev + 0.1) % 100);
      animationFrameId = requestAnimationFrame(animate);
    };

    if (mapImage) {
      animate();
    }

    return () => cancelAnimationFrame(animationFrameId);
  }, [mapImage]);

  useEffect(() => {
    const canvas = canvasRef.current;
    if (!canvas || !mapImage) return;

    const ctx = canvas.getContext('2d');
    if (!ctx) return;

    canvas.width = fixedDimensions.width;
    canvas.height = fixedDimensions.height;

    // Desenhar a imagem do mapa
    ctx.clearRect(0, 0, fixedDimensions.width, fixedDimensions.height);
    ctx.drawImage(mapImage, 0, 0, fixedDimensions.width, fixedDimensions.height);

    const getCanvasCoords = (cityX, cityY) => ({ x: cityX, y: cityY });
    
    // Desenhar rotas e a bolinha de progresso
    displayRoutes.forEach(route => {
      if (route.sequence && route.sequence.length > 0) {
        let routeSegments = [];

        // Calcular segmentos da rota
        route.sequence.forEach(item => {
          if ('origin' in item && 'destination' in item) {
            const originCity = portugueseCities.find(c => c.name === item.origin);
            const destinationCity = portugueseCities.find(c => c.name === item.destination);

            if (originCity && destinationCity) {
              const startCoords = getCanvasCoords(originCity.x, originCity.y);
              const endCoords = getCanvasCoords(destinationCity.x, destinationCity.y);
              routeSegments.push({ start: startCoords, end: endCoords, length: Math.hypot(endCoords.x - startCoords.x, endCoords.y - startCoords.y) });
            }
          }
        });

        // Desenhar a rota principal
        ctx.beginPath();
        routeSegments.forEach((segment, index) => {
          if (index === 0) {
            ctx.moveTo(segment.start.x, segment.start.y);
          } else {
            ctx.lineTo(segment.start.x, segment.start.y);
          }
          ctx.lineTo(segment.end.x, segment.end.y);
        });

        const isSelected = selectedParcelId === route.parcelId;
        ctx.lineWidth = isSelected ? 5 : 3;
        ctx.strokeStyle = route.status === 'completed' ? '#10B981' : (route.status === 'delayed' ? '#F59E0B' : '#3B82F6');
        ctx.setLineDash([]);
        ctx.stroke();

        // Desenhar a "bolinha" a progredir na rota
        if (route.status !== 'completed') {
          let totalLength = routeSegments.reduce((sum, seg) => sum + seg.length, 0);
          let currentProgress = (animationProgress / 100) * totalLength;
          let segmentProgress = 0;

          for (const segment of routeSegments) {
            if (currentProgress <= segmentProgress + segment.length) {
              const segmentRatio = (currentProgress - segmentProgress) / segment.length;
              const ballX = segment.start.x + (segment.end.x - segment.start.x) * segmentRatio;
              const ballY = segment.start.y + (segment.end.y - segment.start.y) * segmentRatio;
              ctx.beginPath();
              ctx.arc(ballX, ballY, 5, 0, Math.PI * 2);
              ctx.fillStyle = isSelected ? '#3B82F6' : '#fff';
              ctx.fill();
              ctx.strokeStyle = '#333';
              ctx.lineWidth = 2;
              ctx.stroke();
              break;
            }
            segmentProgress += segment.length;
          }
        }
      }
    });

    // Desenhar os pontos das cidades
    portugueseCities.forEach(city => {
      const { x, y } = getCanvasCoords(city.x, city.y);
      ctx.beginPath();
      ctx.arc(x, y, city.isCapital ? 10 : 7, 0, Math.PI * 2);
      ctx.fillStyle = city.isCapital ? '#DC2626' : '#374151';
      ctx.fill();
      ctx.strokeStyle = '#FFFFFF';
      ctx.lineWidth = 2;
      ctx.stroke();
      ctx.fillStyle = '#333333';
      ctx.font = '12px Inter';
      ctx.textAlign = 'center';
      ctx.textBaseline = 'bottom';
      ctx.fillText(city.name, x, y - (city.isCapital ? 15 : 10));
    });

    const handleClick = (event) => {
      const rect = canvas.getBoundingClientRect();
      const mouseX = event.clientX - rect.left;
      const mouseY = event.clientY - rect.top;
      portugueseCities.forEach(city => {
        const { x, y } = getCanvasCoords(city.x, city.y);
        const radius = city.isCapital ? 15 : 10;
        const distance = Math.sqrt(Math.pow(mouseX - x, 2) + Math.pow(mouseY - y, 2));
        if (distance < radius) {
          onCityClick?.(city.name);
        }
      });
    };
    canvas.addEventListener('click', handleClick);

    return () => {
      canvas.removeEventListener('click', handleClick);
    };
  }, [mapImage, selectedParcelId, onCityClick, animationProgress, displayRoutes]);

  return (
    <div className="flex flex-col items-center justify-center p-6 min-h-screen">
      <div className="bg-white rounded-lg shadow-md p-6 w-full mx-auto">
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
              <span>Atrasado</span>
            </div>
          </div>
        </div>
        <div className="flex-grow flex items-center justify-center">
          <canvas
            ref={canvasRef}
            className="border-2 border-gray-300 rounded-xl bg-gradient-to-br from-blue-50 via-blue-100 to-green-50"
          >
            O seu navegador não suporta o elemento canvas.
          </canvas>
        </div>
      </div>

      {selectedParcelId !== undefined && displayRoutes && displayRoutes.length > 0 && (
        displayRoutes.find(r => r.parcelId === selectedParcelId) && (
          <div className="mt-4 p-4 bg-blue-50 rounded-lg w-full max-w-lg">
            <h3 className="text-lg font-semibold text-blue-900 mb-3">Rota Selecionada</h3>
            <div className="text-sm text-blue-800">
              <p><strong>Encomenda ID:</strong> {selectedParcelId}</p>
              {displayRoutes.filter(r => r.parcelId === selectedParcelId).map(route => (
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
      
      <div className="mt-4 p-4 bg-yellow-50 rounded-lg border border-yellow-200 w-full max-w-lg">
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

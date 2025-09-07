# CTT Parcel Management System

A comprehensive parcel route management system for CTT (Correios de Portugal) built with React, TypeScript, and Tailwind CSS.

## 🏗️ Project Structure

```
src/
├── components/           # React components
│   ├── Map/             # Map visualization components
│   ├── ParcelList/      # Parcel management components
│   ├── RunningProcesses/# Process monitoring components
│   ├── AddParcelForm/   # Form components
│   └── SeparationCenter/# Separation center components
├── services/            # API service layers
│   ├── api.ts          # Base API service
│   ├── parcelService.ts # Parcel-related API calls
│   └── separationCenterService.ts # Center management API
├── hooks/               # Custom React hooks
│   ├── useParcelData.ts # Parcel data management
│   └── useSeparationCenters.ts # Center data management
├── types/               # TypeScript type definitions
├── data/                # Static data and mock data
├── utils/               # Utility functions
│   ├── constants.ts     # App constants
│   └── formatters.ts    # Data formatting utilities
└── main.tsx            # Application entry point

public/
└── monitor.html         # Standalone monitor for assembly lines
```

## 🚀 Getting Started

### Prerequisites
- Node.js (v16 or higher)
- npm or yarn

### Installation
```bash
# Install dependencies
npm install

# Start development server
npm run dev

# Build for production
npm run build
```

### Environment Variables
Create a `.env` file in the root directory:
```env
REACT_APP_API_URL=http://localhost:8080/api
```

## 📡 API Integration

### Base API Service
The application uses a centralized API service (`src/services/api.ts`) that handles:
- HTTP requests (GET, POST, PUT, DELETE)
- Error handling
- Response formatting
- Authentication headers

### Service Modules

#### Parcel Service (`src/services/parcelService.ts`)
```typescript
// Get all parcels
const parcels = await parcelService.getAllParcels();

// Create new parcel
const newParcel = await parcelService.createParcel({
  userParam: {
    origin: 'Lisboa',
    destination: 'Porto',
    type: 'Expresso',
    maxTransfers: 2,
    date: new Date()
  }
});

// Update parcel status
await parcelService.updateParcelStatus('PKG001', {
  status: 'in_transit',
  progress: 75,
  currentLocation: 'Coimbra'
});
```

#### Separation Center Service (`src/services/separationCenterService.ts`)
```typescript
// Get all centers
const centers = await separationCenterService.getAllCenters();

// Update gate
await separationCenterService.updateGate('CENTER001', 'GATE001', {
  status: 'loading',
  parcelsEntered: 45
});

// Assign truck to gate
await separationCenterService.assignTruckToGate('CENTER001', 'GATE001', 'TRUCK001');
```

## 🔧 How to Modify the Application

### 1. Adding New Components
```typescript
// Create new component in src/components/
export const MyNewComponent: React.FC<Props> = ({ prop1, prop2 }) => {
  return (
    <div className="bg-white rounded-lg p-4">
      {/* Component content */}
    </div>
  );
};
```

### 2. Adding New API Endpoints
```typescript
// In src/services/yourService.ts
class YourService {
  async getYourData(): Promise<YourDataType> {
    try {
      const response = await apiService.get<YourDataType>('/your-endpoint');
      return response.data;
    } catch (error) {
      console.error('Failed to fetch your data:', error);
      throw error;
    }
  }
}
```

### 3. Creating Custom Hooks
```typescript
// In src/hooks/useYourHook.ts
export const useYourHook = () => {
  const [data, setData] = useState<YourType[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      const result = await yourService.getData();
      setData(result);
    } catch (error) {
      console.error('Error:', error);
    } finally {
      setLoading(false);
    }
  };

  return { data, loading, loadData };
};
```

### 4. Adding New Types
```typescript
// In src/types/yourTypes.ts
export interface YourNewType {
  id: string;
  name: string;
  status: 'active' | 'inactive';
  createdAt: Date;
}
```

## 🎯 Key Features

### Main Application
- **Parcel Management**: Create, track, and manage parcels
- **Route Visualization**: Interactive map showing parcel routes
- **Process Monitoring**: Real-time tracking of running processes
- **Separation Centers**: Monitor loading gates and truck assignments

### Monitor Page (`/monitor.html`)
- **Full-screen interface** for assembly line displays
- **Real-time updates** every 5 seconds
- **Gate selection** with detailed information
- **Capacity monitoring** with visual indicators
- **Departure countdowns** with urgency alerts

## 🔄 Real-time Updates

The application implements real-time updates through:
- Periodic API polling (configurable interval)
- WebSocket connections (when available)
- Manual refresh capabilities
- Error handling and retry logic

## 🎨 Styling

- **Tailwind CSS** for utility-first styling
- **Responsive design** for all screen sizes
- **Dark mode support** for monitor displays
- **Consistent color system** and spacing

## 🧪 Development Tips

1. **Use TypeScript strictly** - Define proper types for all data
2. **Follow the service pattern** - Keep API calls in service modules
3. **Use custom hooks** - Encapsulate data logic in reusable hooks
4. **Handle errors gracefully** - Always implement proper error handling
5. **Keep components focused** - Single responsibility principle
6. **Use constants** - Define reusable constants in utils/constants.ts

## 📱 Responsive Design

The application is fully responsive with breakpoints:
- Mobile: `< 768px`
- Tablet: `768px - 1024px`
- Desktop: `> 1024px`

## 🔐 Security Considerations

- API authentication headers
- Input validation
- Error message sanitization
- CORS configuration
- Environment variable protection
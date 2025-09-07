import { PackagePlus, X } from 'lucide-react'; // Changed Plus to PackagePlus for icon
import React, { useState } from 'react';
import { portugueseCities } from '../../data/cities'; // Corrected import path for cities
import { CreateParcelRequest } from '../../types/parcel'; // Import CreateParcelRequest
import { CORRESPONDENCE_TYPES, PARCEL_STATUS } from '../../utils/constants'; // Import constants for types and status

interface AddParcelFormProps {
  // Now expects CreateParcelRequest, which maps directly to backend's EntityDTO
  onAddParcel: (parcelData: CreateParcelRequest) => void;
  onClose: () => void;
}

const AddParcelForm: React.FC<AddParcelFormProps> = ({ onAddParcel, onClose }) => {
  // State to hold all form data, matching CreateParcelRequest (EntityDTO)
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [type, setType] = useState<keyof typeof CORRESPONDENCE_TYPES>('NORMAL'); // Default type
  const [origin, setOrigin] = useState('');
  const [destination, setDestination] = useState('');
  const [maxTransfers, setMaxTransfers] = useState(0); // Default to 0, as per backend default
  const [deadline, setDeadline] = useState(''); // Will be ISO string for datetime-local
  const [status, setStatus] = useState<keyof typeof PARCEL_STATUS>('CREATED'); // Default status for new entities

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    // Basic validation
    if (!name || !origin || !destination || !deadline) {
      alert('Por favor, preencha todos os campos obrigatórios: Nome, Origem, Destino, Prazo de Entrega.');
      return;
    }
    if (origin === destination) {
        alert('Origem e Destino não podem ser iguais.');
        return;
    }

    // Prepare data to match CreateParcelRequest (which maps to backend EntityDTO)
    const parcelData: CreateParcelRequest = {
      name,
      description,
      type, // Already correct enum string
      origin,
      destination,
      maxTransfers: Number(maxTransfers), // Ensure it's a number
      deadline: new Date(deadline).toISOString(), // Convert datetime-local string to ISO string for backend
      status, // Default status
    };

    onAddParcel(parcelData);
    // onClose(); // Let the parent component handle closing after successful add
  };

  return (
    <div className="fixed inset-0 bg-gray-900 bg-opacity-75 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-lg shadow-xl w-full max-w-md p-6 relative">
        <button
          onClick={onClose}
          className="absolute top-3 right-3 text-gray-500 hover:text-gray-700"
        >
          <X className="w-5 h-5" />
        </button>
        <h2 className="text-2xl font-bold text-gray-900 mb-6 flex items-center space-x-2">
          <PackagePlus className="w-6 h-6 text-blue-600" />
          <span>Nova Encomenda</span>
        </h2>
        <form onSubmit={handleSubmit} className="space-y-4">
          {/* Name Field */}
          <div>
            <label htmlFor="name" className="block text-sm font-medium text-gray-700">Nome da Encomenda</label>
            <input
              type="text"
              id="name"
              value={name}
              onChange={(e) => setName(e.target.value)}
              className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
              required
            />
          </div>
          {/* Description Field */}
          <div>
            <label htmlFor="description" className="block text-sm font-medium text-gray-700">Descrição</label>
            <textarea
              id="description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              rows={3}
              className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
            ></textarea>
          </div>
          {/* Type Field */}
          <div>
            <label htmlFor="type" className="block text-sm font-medium text-gray-700">Tipo de Correspondência</label>
            <select
              id="type"
              value={type}
              onChange={(e) => setType(e.target.value as keyof typeof CORRESPONDENCE_TYPES)}
              className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
            >
              {/* Use Object.keys to iterate over enum values */}
              {Object.keys(CORRESPONDENCE_TYPES).map((key) => (
                <option key={key} value={key}>
                  {CORRESPONDENCE_TYPES[key as keyof typeof CORRESPONDENCE_TYPES]}
                </option>
              ))}
            </select>
          </div>
          {/* Origin Field (Dropdown) */}
          <div>
            <label htmlFor="origin" className="block text-sm font-medium text-gray-700">Origem</label>
            <select
              id="origin"
              value={origin}
              onChange={(e) => setOrigin(e.target.value)}
              className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            >
              <option value="">Selecione a origem</option>
              {portugueseCities.map((city) => (
                <option key={city.name} value={city.name}>
                  {city.name}
                </option>
              ))}
            </select>
          </div>
          {/* Destination Field (Dropdown) */}
          <div>
            <label htmlFor="destination" className="block text-sm font-medium text-gray-700">Destino</label>
            <select
              id="destination"
              value={destination}
              onChange={(e) => setDestination(e.target.value)}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            >
              <option value="">Selecione o destino</option>
              {portugueseCities.map((city) => (
                <option key={city.name} value={city.name}>
                  {city.name}
                </option>
              ))}
            </select>
          </div>
          {/* Max Transfers Field */}
          <div>
            <label htmlFor="maxTransfers" className="block text-sm font-medium text-gray-700">Máx. Transferências</label>
            <input
              type="number"
              id="maxTransfers"
              value={maxTransfers}
              onChange={(e) => setMaxTransfers(Number(e.target.value))}
              className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
              min="0" // Max transfers can be 0 or more
            />
          </div>
          {/* Deadline Field */}
          <div>
            <label htmlFor="deadline" className="block text-sm font-medium text-gray-700">Prazo de Entrega</label>
            <input
              type="datetime-local" // Use datetime-local for both date and time
              id="deadline"
              value={deadline}
              onChange={(e) => setDeadline(e.target.value)}
              className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
              required
            />
          </div>
          {/* Status field, default to 'created' and hidden */}
          <input type="hidden" value={status} />

          <div className="flex justify-end space-x-3 mt-6">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="px-4 py-2 bg-blue-600 text-white rounded-md shadow-sm text-sm font-medium hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
            >
              Adicionar Encomenda
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AddParcelForm;

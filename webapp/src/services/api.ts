// API Configuration and Base Service
const API_BASE_URL = 'http://localhost:8080/api';

// Removed ApiResponse interface as it's not consistently used by the backend.
// If your backend starts wrapping ALL responses, you can reintroduce it.

export interface ApiError {
  error: string;
  status: number;
  timestamp: string;
}

class ApiService {
  private baseUrl: string;

  constructor(baseUrl: string = API_BASE_URL) {
    this.baseUrl = baseUrl;
  }

  // The request method now directly returns the parsed JSON data (type T)
  private async request<T>(
    endpoint: string,
    options: RequestInit = {}
  ): Promise<T> { // Changed return type from ApiResponse<T> to T
    const url = `${this.baseUrl}${endpoint}`;
    
    const defaultHeaders = {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    };

    try {
      const response = await fetch(url, {
        ...options,
        headers: {
          ...defaultHeaders,
          ...options.headers,
        },
      });

      if (!response.ok) {
        // Attempt to parse error details if available
        let errorData: ApiError | string = `API Error: ${response.status} ${response.statusText}`;
        try {
          errorData = await response.json();
        } catch (jsonError) {
          console.error('Failed to parse error response JSON:', jsonError);
        }
        throw new Error(`API Error: ${typeof errorData === 'object' && errorData.error ? errorData.error : errorData} (Status: ${response.status})`);
      }

      // Directly return the JSON data, as the backend sends the data without an outer wrapper
      const data: T = await response.json(); // Changed type from ApiResponse<T> to T
      return data;
    } catch (error) {
      console.error('API Request failed:', error);
      throw error;
    }
  }

  // GET request now directly returns T
  async get<T>(endpoint: string): Promise<T> {
    return this.request<T>(endpoint, { method: 'GET' });
  }

  // POST request now directly returns T
  async post<T>(endpoint: string, data: any): Promise<T> {
    return this.request<T>(endpoint, {
      method: 'POST',
      body: JSON.stringify(data),
    });
  }

  // PUT request now directly returns T
  async put<T>(endpoint: string, data: any): Promise<T> {
    return this.request<T>(endpoint, {
      method: 'PUT',
      body: JSON.stringify(data),
    });
  }

  // DELETE request now directly returns T
  async delete<T>(endpoint: string): Promise<T> {
    return this.request<T>(endpoint, { method: 'DELETE' });
  }
}

export const apiService = new ApiService();

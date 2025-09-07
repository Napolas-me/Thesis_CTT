import react from '@vitejs/plugin-react';
import { defineConfig } from 'vite';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  // REMOVED: optimizeDeps.exclude: ['lucide-react'],
  // This exclusion can cause issues with module resolution for lucide-react.
  // By removing it, Vite will pre-bundle lucide-react correctly.
  server: {
    port: 3000, // Frontend will run on port 3000
    proxy: {
      // Proxy API requests to your Spring Boot backend
      '/api': {
        target: 'http://localhost:8080', // Your Spring Boot backend URL
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '/api'),
      },
    },
  },
});
import { resolve } from 'path';
import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:80',
        changeOrigin: true,
      },
      '/media': {
        target: 'http://localhost:80',
        changeOrigin: true,
      },
    },
  },
  build: {
    outDir: 'build', 
    rollupOptions: {
      input: {
        index: resolve(__dirname, 'index.html'),
        art: resolve(__dirname, 'art.html'),
        artists: resolve(__dirname, 'artists.html'),
        statistics: resolve(__dirname, 'statistics.html'),
        admin: resolve(__dirname, 'admin.html'),
        login: resolve(__dirname, 'login.html'),
      },
    },
  },
});

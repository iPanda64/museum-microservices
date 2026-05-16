import Navbar from '../components/Navbar/Navbar';
import { AuthProvider, useAuth } from '../context/AuthContext';

function AppContent() {
  const { loading } = useAuth();

  if (loading) {
    return <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>Loading...</div>;
  }

  return (
    <div>
      <Navbar />
      <main style={{ padding: '2rem' }}>
        <h1>Public Website</h1>
        <p>Welcome! Anyone can see this page.</p>
      </main>
    </div>
  );
}

export default function App() {
  return (
    <AuthProvider>
      <AppContent />
    </AuthProvider>
  )
}

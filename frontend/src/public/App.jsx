import React, { useState } from 'react';
import Navbar from '../components/Navbar/Navbar';
import { AuthProvider, useAuth } from '../context/AuthContext';
import Carousel from '../components/Carousel/Carousel';
import ArtModal from '../components/ArtModal/ArtModal';
import GenericButton from '../components/GenericButton/GenericButton';
import styles from './Home.module.css';

function AppContent() {
  const { loading } = useAuth();
  const [selectedArtwork, setSelectedArtwork] = useState(null);

  if (loading) {
    return <div className={styles.loader}>Loading...</div>;
  }

  const handleArtworkClick = (artwork) => {
    setSelectedArtwork(artwork);
  };

  const handleCloseModal = () => {
    setSelectedArtwork(null);
  };

  return (
    <div>
      <Navbar />
      <main className={styles.main}>
        <div className={styles.welcomeSection}>
          <h1>Welcome to the Museum</h1>
          <p>Explore our collection of art.</p>
        </div>

        <section className={styles.featuredSection}>
          <div className={styles.sectionHeader}>
            <h2>Featured Artworks</h2>
            <GenericButton onClick={() => window.location.href = '/art.html'}>
              View All Artworks
            </GenericButton>
          </div>
          <Carousel onArtworkClick={handleArtworkClick} limit={3} />
        </section>
      </main>

      {selectedArtwork && (
        <ArtModal
          artwork={selectedArtwork}
          onClose={handleCloseModal}
        />
      )}
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

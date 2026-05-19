import React, { useState, useEffect } from 'react';
import Navbar from '../components/Navbar/Navbar';
import { AuthProvider, useAuth } from '../context/AuthContext';
import Carousel from '../components/Carousel/Carousel';
import ArtistCarousel from '../components/ArtistCarousel/ArtistCarousel';
import ArtModal from '../components/ArtModal/ArtModal';
import ArtistModal from '../components/ArtistModal/ArtistModal';
import GenericButton from '../components/GenericButton/GenericButton';
import { getArtworks } from '../services/art';
import { getArtists } from '../services/artist';
import styles from './Home.module.css';

function AppContent() {
  const { loading: authLoading } = useAuth();
  const [artworks, setArtworks] = useState([]);
  const [artists, setArtists] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedArtwork, setSelectedArtwork] = useState(null);
  const [selectedArtist, setSelectedArtist] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      const [artData, artistData] = await Promise.all([
        getArtworks(),
        getArtists()
      ]);
      setArtworks(artData);
      setArtists(artistData);
      setLoading(false);
    };
    fetchData();
  }, []);

  const handleUpdateArtwork = (updatedArt) => {
    setArtworks(prev => prev.map(art => art.artworkId === updatedArt.artworkId ? updatedArt : art));
  };

  const handleUpdateArtist = (updatedArtist) => {
    setArtists(prev => prev.map(a => a.artistId === updatedArtist.artistId ? updatedArtist : a));
  };

  if (authLoading || loading) {
    return <div className={styles.loader}>Loading...</div>;
  }

  return (
    <div>
      <Navbar />
      <main className={styles.main}>
        <div className={styles.welcomeSection}>
          <h1>Welcome to the Museum</h1>
          <p>Explore world-class art.</p>
        </div>

        <section className={styles.featuredSection}>
          <div className={styles.sectionHeader}>
            <h2>Featured Artworks</h2>
            <GenericButton onClick={() => window.location.href = '/art.html'}>
              View All Artworks
            </GenericButton>
          </div>
          <Carousel onArtworkClick={setSelectedArtwork} data={artworks} limit={3} />
        </section>

        <section className={styles.featuredSection}>
          <div className={styles.sectionHeader}>
            <h2>Featured Artists</h2>
            <GenericButton onClick={() => window.location.href = '/artists.html'}>
              View All Artists
            </GenericButton>
          </div>
          <ArtistCarousel onArtistClick={setSelectedArtist} data={artists} limit={3} />
        </section>
      </main>

      {selectedArtwork && (
        <ArtModal
          artwork={selectedArtwork}
          onClose={() => setSelectedArtwork(null)}
          onUpdate={handleUpdateArtwork}
        />
      )}

      {selectedArtist && (
        <ArtistModal
          artist={selectedArtist}
          onClose={() => setSelectedArtist(null)}
          onUpdate={handleUpdateArtist}
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
  );
}

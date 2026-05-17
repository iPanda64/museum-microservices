import React, { useState, useEffect } from 'react';
import Navbar from '../../components/Navbar/Navbar';
import { AuthProvider, useAuth } from '../../context/AuthContext';
import ArtistGrid from '../../components/ArtistGrid/ArtistGrid';
import ArtistModal from '../../components/ArtistModal/ArtistModal';
import AddArtistModal from '../../components/AddArtistModal/AddArtistModal';
import SearchBar from '../../components/SearchBar/SearchBar';
import GenericButton from '../../components/GenericButton/GenericButton';
import { getArtists } from '../../services/artist';
import { hasRole } from '../../utils/auth';
import styles from './Artists.module.css';

function ArtistsContent() {
  const { loading: authLoading } = useAuth();
  const [artists, setArtists] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState('');
  const [selectedNationality, setSelectedNationality] = useState('');
  const [selectedArtist, setSelectedArtist] = useState(null);
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);

  const isStaff = hasRole('ADMIN') || hasRole('MANAGER') || hasRole('EMPLOYEE');

  useEffect(() => {
    const fetchArtists = async () => {
      setLoading(true);
      const data = await getArtists();
      setArtists(data);
      setLoading(false);
    };
    fetchArtists();
  }, []);

  const handleAddSuccess = (newArtist) => {
    setArtists(prev => [newArtist, ...prev]);
  };

  const handleDeleteSuccess = (artistId) => {
    setArtists(prev => prev.filter(a => a.artistId !== artistId));
  };

  const handleUpdateArtist = (updatedArtist) => {
    setArtists(prev => prev.map(a => a.artistId === updatedArtist.artistId ? updatedArtist : a));
  };

  // Get unique nationalities
  const nationalityOptions = [...new Set(artists.map(a => a.nationality))].filter(Boolean).sort();

  const filteredArtists = artists.filter(artist => {
    const searchMatch = artist.fullName.toLowerCase().includes(filter.toLowerCase());
    const nationalityMatch = !selectedNationality || artist.nationality === selectedNationality;
    return searchMatch && nationalityMatch;
  });

  if (authLoading || loading) {
    return <div className={styles.loader}>Loading artists...</div>;
  }

  return (
    <div className={styles.container}>
      <Navbar />
      <main className={styles.main}>
        <div className={styles.hero}>
          <h1>The Creators</h1>
          <p>The visionaries behind our collection</p>
        </div>

        <SearchBar
          searchValue={filter}
          onSearchChange={setFilter}
          placeholder="Search by name..."
          typeOptions={nationalityOptions}
          selectedType={selectedNationality}
          onTypeChange={setSelectedNationality}
          actionElement={isStaff && (
            <GenericButton type="default" onClick={() => setIsAddModalOpen(true)}>
              Add Artist
            </GenericButton>
          )}
        />

        <ArtistGrid
          data={filteredArtists}
          onArtistClick={setSelectedArtist}
          onDeleteArtist={handleDeleteSuccess}
        />
      </main>

      {selectedArtist && (
        <ArtistModal
          artist={selectedArtist}
          onClose={() => setSelectedArtist(null)}
          onUpdate={handleUpdateArtist}
        />
      )}

      {isAddModalOpen && (
        <AddArtistModal
          onClose={() => setIsAddModalOpen(false)}
          onSuccess={handleAddSuccess}
        />
      )}
    </div>
  );
}

export default function App() {
  return (
    <AuthProvider>
      <ArtistsContent />
    </AuthProvider>
  );
}

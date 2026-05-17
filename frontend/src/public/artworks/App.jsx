import React, { useState, useEffect } from 'react';
import Navbar from '../../components/Navbar/Navbar';
import { AuthProvider, useAuth } from '../../context/AuthContext';
import ArtGrid from '../../components/ArtGrid/ArtGrid';
import ArtModal from '../../components/ArtModal/ArtModal';
import AddArtworkModal from '../../components/AddArtworkModal/AddArtworkModal';
import ExportModal from '../../components/ExportModal/ExportModal';
import SearchBar from '../../components/SearchBar/SearchBar';
import GenericButton from '../../components/GenericButton/GenericButton';
import { getArtworks } from '../../services/art';
import { getArtists } from '../../services/artist';
import { hasRole } from '../../utils/auth';
import styles from './Artworks.module.css';

function ArtworksContent() {
  const { loading: authLoading } = useAuth();
  const [artworks, setArtworks] = useState([]);
  const [artists, setArtists] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState('');
  const [selectedArtist, setSelectedArtist] = useState('');
  const [selectedType, setSelectedType] = useState('');
  const [sortByArtist, setSortByArtist] = useState(false);
  const [selectedArtwork, setSelectedArtwork] = useState(null);
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [isExportModalOpen, setIsExportModalOpen] = useState(false);

  const isStaff = hasRole('ADMIN') || hasRole('MANAGER') || hasRole('EMPLOYEE');

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

  const getArtistName = (id) => {
    const artist = artists.find(a => a.artistId === id);
    return artist ? artist.fullName : 'Unknown Artist';
  };

  const handleAddSuccess = (newArtwork) => {
    setArtworks(prev => [newArtwork, ...prev]);
  };

  const handleDeleteSuccess = (artworkId) => {
    setArtworks(prev => prev.filter(art => art.artworkId !== artworkId));
  };

  const handleUpdateArtwork = (updatedArt) => {
    setArtworks(prev => prev.map(art => art.artworkId === updatedArt.artworkId ? updatedArt : art));
  };

  // Get unique types from artworks
  const typeOptions = [...new Set(artworks.map(art => art.artworkType))].filter(Boolean).sort();

  // Prepare artist options for dropdown
  const artistOptions = artists.map(a => ({ id: a.artistId, name: a.fullName }));

  const filteredAndSortedArtworks = artworks
    .filter(art => {
      const searchMatch = art.title.toLowerCase().includes(filter.toLowerCase());
      const artistMatch = !selectedArtist || art.artistId === parseInt(selectedArtist);
      const typeMatch = !selectedType || art.artworkType === selectedType;
      return searchMatch && artistMatch && typeMatch;
    })
    .sort((a, b) => {
      if (!sortByArtist) return 0;
      const nameA = getArtistName(a.artistId).toLowerCase();
      const nameB = getArtistName(b.artistId).toLowerCase();
      return nameA.localeCompare(nameB);
    });

  if (authLoading || loading) {
    return <div className={styles.loader}>Loading gallery...</div>;
  }

  return (
    <div className={styles.container}>
      <Navbar />
      <main className={styles.main}>
        <div className={styles.hero}>
          <h1>The Collection</h1>
          <p>Showing every single piece in the collection</p>
        </div>

        <SearchBar
          searchValue={filter}
          onSearchChange={setFilter}
          placeholder="Search by title..."
          showSort={true}
          sortValue={sortByArtist}
          onSortChange={setSortByArtist}
          sortLabel="Sort by Artist"
          artistOptions={artistOptions}
          selectedArtist={selectedArtist}
          onArtistChange={setSelectedArtist}
          typeOptions={typeOptions}
          selectedType={selectedType}
          onTypeChange={setSelectedType}
          actionElement={isStaff && (
            <GenericButton type="default" onClick={() => setIsAddModalOpen(true)}>
              Add Artwork
            </GenericButton>
          )}
          secondaryActionElement={isStaff && (
            <GenericButton type="default" onClick={() => setIsExportModalOpen(true)}>
              Export
            </GenericButton>
          )}
        />

        <ArtGrid
          data={filteredAndSortedArtworks}
          onArtworkClick={setSelectedArtwork}
          onDeleteArtwork={handleDeleteSuccess}
        />
      </main>

      {selectedArtwork && (
        <ArtModal
          artwork={selectedArtwork}
          onClose={() => setSelectedArtwork(null)}
          onUpdate={handleUpdateArtwork}
        />
      )}

      {isAddModalOpen && (
        <AddArtworkModal
          artists={artists}
          onClose={() => setIsAddModalOpen(false)}
          onSuccess={handleAddSuccess}
        />
      )}

      {isExportModalOpen && (
        <ExportModal
          onClose={() => setIsExportModalOpen(false)}
        />
      )}
    </div>
  );
}

export default function App() {
  return (
    <AuthProvider>
      <ArtworksContent />
    </AuthProvider>
  );
}

import React, { useEffect, useState } from 'react';
import styles from './ArtistCarousel.module.css';
import { getArtists } from '../../services/artist';
import ArtistCard from '../ArtistCard/ArtistCard';

const ArtistCarousel = ({ onArtistClick, limit, data }) => {
  const [artists, setArtists] = useState([]);
  const [loading, setLoading] = useState(!data);

  useEffect(() => {
    if (data) {
      setArtists(limit ? data.slice(0, limit) : data);
      setLoading(false);
      return;
    }

    const loadArtists = async () => {
      const result = await getArtists();
      if (Array.isArray(result)) {
        const sliced = limit ? result.slice(0, limit) : result;
        setArtists(sliced);
      }
      setLoading(false);
    };
    loadArtists();
  }, [limit, data]);

  const handleDeleteSuccess = (artistId) => {
    setArtists(prev => prev.filter(a => a.artistId !== artistId));
  };

  if (loading) {
    return <div className={styles.loading}>Loading artists...</div>;
  }

  if (artists.length === 0) {
    return null;
  }

  return (
    <div className={styles.carouselContainer}>
      <div className={styles.track}>
        {artists.map(artist => (
          <ArtistCard 
            key={artist.artistId} 
            artist={artist} 
            onClick={onArtistClick}
            onDelete={handleDeleteSuccess}
          />
        ))}
      </div>
    </div>
  );
};

export default ArtistCarousel;

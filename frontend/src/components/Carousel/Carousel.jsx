import React, { useEffect, useState } from 'react';
import styles from './Carousel.module.css';
import { getArtworks } from '../../services/art';
import ArtCard from '../ArtCard/ArtCard';
import GenericButton from '../GenericButton/GenericButton';

const Carousel = ({ onArtworkClick, limit, title = "Featured Artworks", data }) => {
  const [artworks, setArtworks] = useState([]);
  const [loading, setLoading] = useState(!data);

  useEffect(() => {
    if (data) {
      setArtworks(limit ? data.slice(0, limit) : data);
      setLoading(false);
      return;
    }

    const loadArt = async () => {
      const result = await getArtworks();
      if (Array.isArray(result)) {
        const sliced = limit ? result.slice(0, limit) : result;
        console.log(`Carousel: Displaying ${sliced.length} artworks (limit: ${limit || 'none'})`);
        setArtworks(sliced);
      }
      setLoading(false);
    };
    loadArt();
  }, [limit, data]);

  const handleDeleteSuccess = (artworkId) => {
    setArtworks(prev => prev.filter(art => art.artworkId !== artworkId));
  };

  if (loading) {
    return <div className={styles.loading}>Loading art...</div>;
  }

  if (artworks.length === 0) {
    return null;
  }

  return (
    <div className={styles.carouselContainer}>
      <div className={styles.track}>
        {artworks.map(art => (
          <ArtCard 
            key={art.artworkId} 
            artwork={art} 
            onClick={onArtworkClick}
            onDelete={handleDeleteSuccess}
          />
        ))}
      </div>
    </div>
  );
};

export default Carousel;

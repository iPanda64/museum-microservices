import React from 'react';
import ArtCard from '../ArtCard/ArtCard';
import styles from './ArtGrid.module.css';

const ArtGrid = ({ data, onArtworkClick, onDeleteArtwork }) => {
  if (!data || data.length === 0) {
    return (
      <div className={styles.empty}>
        <p>No artworks found matching your criteria.</p>
      </div>
    );
  }

  return (
    <div className={styles.gridContainer}>
      <div className={styles.grid}>
        {data.map((art) => (
          <ArtCard 
            key={art.artworkId} 
            artwork={art} 
            onClick={onArtworkClick} 
            onDelete={onDeleteArtwork}
          />
        ))}
      </div>
    </div>
  );
};

export default ArtGrid;

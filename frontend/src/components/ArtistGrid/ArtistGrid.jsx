import React from 'react';
import ArtistCard from '../ArtistCard/ArtistCard';
import styles from './ArtistGrid.module.css';

const ArtistGrid = ({ data, onArtistClick, onDeleteArtist }) => {
  if (!data || data.length === 0) {
    return (
      <div className={styles.empty}>
        <p>No artists found matching your criteria.</p>
      </div>
    );
  }

  return (
    <div className={styles.gridContainer}>
      <div className={styles.grid}>
        {data.map((artist) => (
          <ArtistCard 
            key={artist.artistId} 
            artist={artist} 
            onClick={onArtistClick} 
            onDelete={onDeleteArtist}
          />
        ))}
      </div>
    </div>
  );
};

export default ArtistGrid;

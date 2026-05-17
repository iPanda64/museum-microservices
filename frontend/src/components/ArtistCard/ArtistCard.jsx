import React from 'react';
import styles from './ArtistCard.module.css';
import { getArtistImageUrl, deleteArtistWithArtworks } from '../../services/artist';
import { hasRole } from '../../utils/auth';
import GenericButton from '../GenericButton/GenericButton';

const ArtistCard = ({ artist, onClick, onDelete }) => {
  const isStaff = hasRole('ADMIN') || hasRole('MANAGER') || hasRole('EMPLOYEE');

  const handleDelete = async (e) => {
    e.stopPropagation();
    if (window.confirm(`Are you sure you want to delete "${artist.fullName}"? This will also delete all their artworks.`)) {
      try {
        await deleteArtistWithArtworks(artist.artistId);
        if (onDelete) onDelete(artist.artistId);
      } catch (err) {
        alert(err.message || 'Failed to delete artist.');
      }
    }
  };

  return (
    <div className={styles.card} onClick={() => onClick && onClick(artist)}>
      <div className={styles.imageContainer}>
        {artist.profilePhotoPath ? (
          <img 
            src={getArtistImageUrl(artist.profilePhotoPath)} 
            alt={artist.fullName} 
            className={styles.image}
          />
        ) : (
          <div className={styles.placeholder}>
            <span>No Photo</span>
          </div>
        )}
        
        {isStaff && (
          <div className={styles.hoverActions}>
            <GenericButton 
              type="danger" 
              onClick={handleDelete}
              className={styles.deleteBtn}
            >
              Delete
            </GenericButton>
          </div>
        )}
      </div>
      <div className={styles.info}>
        <h3 className={styles.name}>{artist.fullName}</h3>
        <p className={styles.nationality}>{artist.nationality}</p>
      </div>
    </div>
  );
};

export default ArtistCard;

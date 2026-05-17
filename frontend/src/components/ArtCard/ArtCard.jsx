import React from 'react';
import styles from './ArtCard.module.css';
import { getImageUrl, deleteArtwork } from '../../services/art';
import { hasRole } from '../../utils/auth';
import GenericButton from '../GenericButton/GenericButton';

const ArtCard = ({ artwork, onClick, onDelete }) => {
  const isStaff = hasRole('ADMIN') || hasRole('MANAGER') || hasRole('EMPLOYEE');
  
  const primaryImage = artwork.images && artwork.images.length > 0 
    ? artwork.images[0].imagePath 
    : null;

  const handleDelete = async (e) => {
    e.stopPropagation(); // Don't open the modal
    if (window.confirm(`Are you sure you want to delete "${artwork.title}"?`)) {
      try {
        await deleteArtwork(artwork.artworkId);
        if (onDelete) onDelete(artwork.artworkId);
      } catch (err) {
        alert(err.message || 'Failed to delete artwork.');
      }
    }
  };

  return (
    <div className={styles.card} onClick={() => onClick && onClick(artwork)}>
      <div className={styles.imageContainer}>
        {primaryImage ? (
          <img 
            src={getImageUrl(primaryImage)} 
            alt={artwork.title} 
            className={styles.image}
          />
        ) : (
          <div className={styles.placeholder}>
            <span>No Image</span>
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
        <h3 className={styles.title}>{artwork.title}</h3>
        <p className={styles.type}>{artwork.artworkType}</p>
      </div>
    </div>
  );
};

export default ArtCard;

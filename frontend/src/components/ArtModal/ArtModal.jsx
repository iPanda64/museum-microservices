import React, { useState, useEffect, useCallback } from 'react';
import styles from './ArtModal.module.css';
import { getArtistById, getArtworkById } from '../../services/art';
import { hasRole } from '../../utils/auth';
import ArtImageCarousel from '../ArtImageCarousel/ArtImageCarousel';
import ArtDetails from '../ArtDetails/ArtDetails';
import ArtPhotoManager from '../ArtPhotoManager/ArtPhotoManager';

const ArtModal = ({ artwork, onClose }) => {
  const [localArtwork, setLocalArtwork] = useState(artwork);
  const [currentImageIndex, setCurrentImageIndex] = useState(0);
  const [artist, setArtist] = useState(null);
  const [loadingArtist, setLoadingArtist] = useState(false);

  const isStaff = hasRole('ADMIN') || hasRole('MANAGER') || hasRole('EMPLOYEE');

  // Prevent scroll when modal is open
  useEffect(() => {
    document.body.style.overflow = 'hidden';
    return () => {
      document.body.style.overflow = 'unset';
    };
  }, []);

  const refreshArtwork = useCallback(async () => {
    if (!artwork?.artworkId) return;
    const updatedArt = await getArtworkById(artwork.artworkId);
    if (updatedArt) {
      setLocalArtwork(updatedArt);
      // If we deleted an image, reset index if it's out of bounds
      if (currentImageIndex >= updatedArt.images.length && updatedArt.images.length > 0) {
        setCurrentImageIndex(updatedArt.images.length - 1);
      }
    }
  }, [artwork?.artworkId, currentImageIndex]);

  useEffect(() => {
    if (localArtwork?.artistId) {
      const fetchArtist = async () => {
        setLoadingArtist(true);
        const data = await getArtistById(localArtwork.artistId);
        setArtist(data);
        setLoadingArtist(false);
      };
      fetchArtist();
    }
  }, [localArtwork?.artistId]);

  if (!localArtwork) return null;

  return (
    <div className={styles.overlay} onClick={onClose}>
      <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
        <button className={styles.closeBtn} onClick={onClose}>&times;</button>
        
        <div className={styles.content}>
          <ArtImageCarousel 
            images={localArtwork.images || []} 
            title={localArtwork.title}
            currentIndex={currentImageIndex}
            setCurrentIndex={setCurrentImageIndex}
          />
          
          <div className={styles.infoWrapper}>
            <ArtDetails 
              artworkId={localArtwork.artworkId}
              artistId={localArtwork.artistId}
              title={localArtwork.title}
              artist={artist}
              type={localArtwork.artworkType}
              loadingArtist={loadingArtist}
              isStaff={isStaff}
              onUpdate={refreshArtwork}
            />
            
            <ArtPhotoManager 
              artworkId={localArtwork.artworkId}
              images={localArtwork.images || []}
              currentImageIndex={currentImageIndex}
              onUpdate={refreshArtwork}
            />
          </div>
        </div>
      </div>
    </div>
  );
};

export default ArtModal;

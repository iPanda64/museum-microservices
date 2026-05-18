import React, { useState, useEffect, useCallback } from 'react';
import styles from './ArtistModal.module.css';
import { getArtistById } from '../../services/artist';
import { getArtworks } from '../../services/art';
import { isStaff } from '../../utils/auth';
import ArtistDetails from '../ArtistDetails/ArtistDetails';
import ArtistPhotoManager from '../ArtistPhotoManager/ArtistPhotoManager';
import ArtGrid from '../ArtGrid/ArtGrid';

const ArtistModal = ({ artist, onClose, onUpdate }) => {
  const [localArtist, setLocalArtist] = useState(artist);
  const [artworks, setArtworks] = useState([]);
  const [loadingArtworks, setLoadingArtworks] = useState(true);

  const showStaffTools = isStaff();

  useEffect(() => {
    document.body.style.overflow = 'hidden';
    return () => {
      document.body.style.overflow = 'unset';
    };
  }, []);

  const refreshArtist = useCallback(async () => {
    if (!artist?.artistId) return;
    const updated = await getArtistById(artist.artistId);
    if (updated) {
      setLocalArtist(updated);
      if (onUpdate) onUpdate(updated);
    }
  }, [artist?.artistId, onUpdate]);

  useEffect(() => {
    const fetchArtistArtworks = async () => {
      setLoadingArtworks(true);
      const allArt = await getArtworks();
      const filtered = allArt.filter(art => art.artistId === localArtist.artistId);
      setArtworks(filtered);
      setLoadingArtworks(false);
    };
    fetchArtistArtworks();
  }, [localArtist.artistId]);

  if (!localArtist) return null;

  return (
    <div className={styles.overlay} onClick={onClose}>
      <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
        <button className={styles.closeBtn} onClick={onClose}>&times;</button>
        
        <div className={styles.content}>
          <div className={styles.leftSection}>
            <ArtistPhotoManager 
              artistId={localArtist.artistId}
              photoPath={localArtist.profilePhotoPath}
              onUpdate={refreshArtist}
            />
            
            <ArtistDetails 
              artist={localArtist}
              isStaff={showStaffTools}
              onUpdate={refreshArtist}
            />
          </div>
          
          <div className={styles.rightSection}>
            <h3>Artworks by {localArtist.fullName}</h3>
            {loadingArtworks ? (
              <p className={styles.loading}>Loading artworks...</p>
            ) : (
              <ArtGrid data={artworks} onArtworkClick={null} />
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ArtistModal;

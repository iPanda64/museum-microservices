import React, { useState } from 'react';
import styles from './AddArtworkModal.module.css';
import GenericButton from '../GenericButton/GenericButton';
import { createArtwork } from '../../services/art';

const AddArtworkModal = ({ artists, onClose, onSuccess }) => {
  const [title, setTitle] = useState('');
  const [type, setType] = useState('');
  const [artistId, setArtistId] = useState(artists[0]?.artistId || '');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!title || !type || !artistId) {
      setError('Please fill out all fields.');
      return;
    }

    setLoading(true);
    setError('');

    try {
      const newArtwork = await createArtwork({
        title,
        artworkType: type,
        artistId: parseInt(artistId)
      });
      onSuccess(newArtwork);
      onClose();
    } catch (err) {
      setError(err.message || 'Failed to create artwork.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.overlay} onClick={onClose}>
      <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
        <button className={styles.closeBtn} onClick={onClose}>&times;</button>
        
        <div className={styles.content}>
          <h1>Add New Artwork</h1>
          
          <form onSubmit={handleSubmit} className={styles.form}>
            {error && <div className={styles.error}>{error}</div>}

            <div className={styles.inputGroup}>
              <label>Title</label>
              <input 
                type="text" 
                value={title} 
                onChange={(e) => setTitle(e.target.value)} 
                required 
                placeholder="e.g. The Starry Night"
              />
            </div>

            <div className={styles.inputGroup}>
              <label>Type</label>
              <input 
                type="text" 
                value={type} 
                onChange={(e) => setType(e.target.value)} 
                required 
                placeholder="e.g. Painting, Sculpture"
              />
            </div>

            <div className={styles.inputGroup}>
              <label>Artist</label>
              <select 
                value={artistId} 
                onChange={(e) => setArtistId(e.target.value)}
                required
              >
                <option value="" disabled>Select an artist</option>
                {artists.map(artist => (
                  <option key={artist.artistId} value={artist.artistId}>
                    {artist.fullName}
                  </option>
                ))}
              </select>
            </div>

            <div className={styles.actions}>
              <GenericButton type="action" fullWidth={true} disabled={loading}>
                {loading ? 'Creating...' : 'Create Artwork'}
              </GenericButton>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default AddArtworkModal;

import React, { useState, useEffect } from 'react';
import styles from './ArtDetails.module.css';
import GenericButton from '../GenericButton/GenericButton';
import AuthorizedInput from '../AuthorizedInput/AuthorizedInput';
import { updateArtwork } from '../../services/art';

const ArtDetails = ({ artworkId, artistId, title, artist, type, loadingArtist, isStaff, onUpdate }) => {
  const [localTitle, setLocalTitle] = useState(title);
  const [localType, setLocalType] = useState(type);
  const [isSaving, setIsSaving] = useState(false);
  const [error, setError] = useState('');

  // Keep in sync with props
  useEffect(() => {
    setLocalTitle(title);
    setLocalType(type);
  }, [title, type]);

  const hasChanges = localTitle !== title || localType !== type;

  const handleSave = async () => {
    if (!hasChanges) return;

    setIsSaving(true);
    setError('');

    try {
      await updateArtwork(artworkId, {
        artistId: artistId,
        title: localTitle,
        artworkType: localType
      });
      onUpdate();
    } catch (err) {
      setError(err.message || 'Failed to update.');
    } finally {
      setIsSaving(false);
    }
  };

  return (
    <div className={styles.details}>
      <div className={styles.field}>
        <AuthorizedInput
          value={localTitle}
          onChange={setLocalTitle}
          placeholder="Artwork Title"
          className={styles.titleDisplay}
          tag="h2"
          fontSize="2rem"
        />
      </div>

      <p className={styles.artist}>
        <strong>Artist:</strong> {loadingArtist ? 'Loading...' : (artist?.fullName || 'Unknown Artist')}
      </p>

      <div className={styles.field}>
        <strong>Type:</strong>
        <AuthorizedInput
          value={localType}
          onChange={setLocalType}
          placeholder="e.g. Painting"
          className={styles.typeDisplay}
          fontSize="1.1rem"
        />
      </div>

      {hasChanges && isStaff && (
        <div className={styles.saveContainer}>
          <GenericButton
            onClick={handleSave}
            disabled={isSaving}
            type="action"
            className={styles.saveBtn}
          >
            {isSaving ? 'Saving...' : 'Save Changes'}
          </GenericButton>
          {error && <span className={styles.error}>{error}</span>}
        </div>
      )}
    </div>
  );
};

export default ArtDetails;

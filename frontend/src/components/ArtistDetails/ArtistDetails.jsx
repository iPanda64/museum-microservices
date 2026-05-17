import React, { useState, useEffect } from 'react';
import styles from './ArtistDetails.module.css';
import GenericButton from '../GenericButton/GenericButton';
import AuthorizedInput from '../AuthorizedInput/AuthorizedInput';
import { updateArtist } from '../../services/artist';

const ArtistDetails = ({ artist, isStaff, onUpdate }) => {
  const [formData, setFormData] = useState({ ...artist });
  const [isSaving, setIsSaving] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    setFormData({ ...artist });
  }, [artist]);

  const hasChanges = Object.keys(formData).some(key => formData[key] !== artist[key]);

  const handleFieldChange = (field, value) => {
    setFormData(prev => ({ ...prev, [field]: value }));
  };

  const handleSave = async () => {
    if (!hasChanges) return;
    setIsSaving(true);
    setError('');
    try {
      await updateArtist(artist.artistId, formData);
      onUpdate();
    } catch (err) {
      setError(err.message || 'Failed to update artist.');
    } finally {
      setIsSaving(false);
    }
  };

  return (
    <div className={styles.details}>
      <AuthorizedInput 
        value={formData.fullName} 
        onChange={(v) => handleFieldChange('fullName', v)}
        tag="h2"
        fontSize="2.5rem"
        className={styles.nameInput}
      />
      
      <div className={styles.infoGrid}>
        <div className={styles.field}>
          <strong>Nationality:</strong>
          <AuthorizedInput 
            value={formData.nationality} 
            onChange={(v) => handleFieldChange('nationality', v)}
            fontSize="1.1rem"
          />
        </div>
        <div className={styles.field}>
          <strong>Born:</strong>
          <AuthorizedInput 
            value={formData.birthDate} 
            onChange={(v) => handleFieldChange('birthDate', v)}
            fontSize="1.1rem"
            type="date"
          />
        </div>
        <div className={styles.field}>
          <strong>Birth Place:</strong>
          <AuthorizedInput 
            value={formData.birthPlace} 
            onChange={(v) => handleFieldChange('birthPlace', v)}
            fontSize="1.1rem"
          />
        </div>
      </div>

      <div className={styles.bioSection}>
        <strong>Biography:</strong>
        {isStaff ? (
          <textarea 
            className={styles.bioTextarea}
            value={formData.biography}
            onChange={(e) => handleFieldChange('biography', e.target.value)}
          />
        ) : (
          <p className={styles.bioText}>{artist.biography}</p>
        )}
      </div>

      {hasChanges && isStaff && (
        <div className={styles.saveContainer}>
          <GenericButton onClick={handleSave} disabled={isSaving} type="action">
            {isSaving ? 'Saving...' : 'Save Artist Details'}
          </GenericButton>
          {error && <p className={styles.error}>{error}</p>}
        </div>
      )}
    </div>
  );
};

export default ArtistDetails;

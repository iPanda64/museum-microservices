import React, { useState, useRef } from 'react';
import styles from './ArtistPhotoManager.module.css';
import GenericButton from '../GenericButton/GenericButton';
import { hasRole } from '../../utils/auth';
import { uploadArtistPhoto, deleteArtistPhoto, getArtistImageUrl } from '../../services/artist';

const ArtistPhotoManager = ({ artistId, photoPath, onUpdate }) => {
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState({ text: '', type: '' });
  const fileInputRef = useRef(null);

  const isStaff = hasRole('ADMIN') || hasRole('MANAGER') || hasRole('EMPLOYEE');

  const handleUpload = async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    setLoading(true);
    setMessage({ text: 'Uploading photo...', type: 'info' });

    try {
      await uploadArtistPhoto(artistId, file);
      setMessage({ text: 'Photo updated!', type: 'success' });
      onUpdate();
    } catch (err) {
      setMessage({ text: err.message || 'Upload failed.', type: 'error' });
    } finally {
      setLoading(false);
      if (fileInputRef.current) fileInputRef.current.value = '';
    }
  };

  const handleDelete = async () => {
    if (!photoPath) return;
    if (!window.confirm('Are you sure you want to delete the profile photo?')) return;

    setLoading(true);
    setMessage({ text: 'Deleting...', type: 'info' });

    try {
      await deleteArtistPhoto(artistId);
      setMessage({ text: 'Photo deleted.', type: 'success' });
      onUpdate();
    } catch (err) {
      setMessage({ text: err.message || 'Deletion failed.', type: 'error' });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.container}>
      <div className={styles.imageWrapper}>
        {photoPath ? (
          <img 
            src={getArtistImageUrl(photoPath)} 
            alt="Artist Profile" 
            className={styles.profileImage}
          />
        ) : (
          <div className={styles.placeholder}>No Photo Available</div>
        )}
      </div>

      {isStaff && (
        <div className={styles.staffTools}>
          <div className={styles.actions}>
            <GenericButton 
              onClick={() => fileInputRef.current?.click()} 
              disabled={loading}
              type="default"
            >
              Change Photo
            </GenericButton>
            
            {photoPath && (
              <GenericButton 
                onClick={handleDelete} 
                disabled={loading}
                type="danger"
              >
                Delete
              </GenericButton>
            )}
          </div>

          <input 
            type="file" 
            ref={fileInputRef} 
            onChange={handleUpload} 
            style={{ display: 'none' }} 
            accept="image/*"
          />

          {message.text && (
            <div className={`${styles.message} ${styles[message.type]}`}>
              {message.text}
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default ArtistPhotoManager;

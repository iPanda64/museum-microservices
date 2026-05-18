import React, { useState, useRef } from 'react';
import styles from './ArtPhotoManager.module.css';
import GenericButton from '../GenericButton/GenericButton';
import { isStaff } from '../../utils/auth';
import { uploadArtworkPhoto, deleteArtworkPhoto } from '../../services/art';

const ArtPhotoManager = ({ artworkId, images, currentImageIndex, onUpdate }) => {
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState({ text: '', type: '' });
  const fileInputRef = useRef(null);

  const showStaffTools = isStaff();

  if (!showStaffTools) return null;

  const handleUpload = async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    if (images.length >= 3) {
      setMessage({ text: 'Maximum of 3 photos allowed.', type: 'error' });
      return;
    }

    setLoading(true);
    setMessage({ text: 'Uploading...', type: 'info' });

    try {
      await uploadArtworkPhoto(artworkId, file);
      setMessage({ text: 'Uploaded successfully!', type: 'success' });
      onUpdate();
    } catch (err) {
      setMessage({ text: err.message || 'Upload failed.', type: 'error' });
    } finally {
      setLoading(false);
      if (fileInputRef.current) fileInputRef.current.value = '';
    }
  };

  const handleDelete = async () => {
    if (images.length <= 0) {
      setMessage({ text: 'No photos to delete.', type: 'error' });
      return;
    }

    const imageId = images[currentImageIndex]?.imageId;
    if (!imageId) return;

    if (!window.confirm('Are you sure you want to delete this photo?')) return;

    setLoading(true);
    setMessage({ text: 'Deleting...', type: 'info' });

    try {
      await deleteArtworkPhoto(artworkId, imageId);
      setMessage({ text: 'Deleted successfully!', type: 'success' });
      onUpdate();
    } catch (err) {
      setMessage({ text: err.message || 'Deletion failed.', type: 'error' });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.manager}>
      <div className={styles.actions}>
        <GenericButton
          onClick={() => fileInputRef.current?.click()}
          disabled={loading || images.length >= 3}
          type="default"
        >
          Add Photo
        </GenericButton>

        {images.length > 0 && (
          <GenericButton
            onClick={handleDelete}
            disabled={loading}
            type="danger"
          >
            Delete Photo
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
  );
};

export default ArtPhotoManager;

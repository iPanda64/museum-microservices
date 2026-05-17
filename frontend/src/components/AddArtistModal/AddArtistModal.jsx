import React, { useState } from 'react';
import styles from './AddArtistModal.module.css';
import GenericButton from '../GenericButton/GenericButton';
import { createArtist } from '../../services/artist';

const AddArtistModal = ({ onClose, onSuccess }) => {
  const [formData, setFormData] = useState({
    fullName: '',
    birthDate: '',
    birthPlace: '',
    nationality: '',
    biography: ''
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const newArtist = await createArtist(formData);
      onSuccess(newArtist);
      onClose();
    } catch (err) {
      setError(err.message || 'Failed to create artist.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.overlay} onClick={onClose}>
      <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
        <button className={styles.closeBtn} onClick={onClose}>&times;</button>
        
        <div className={styles.content}>
          <h1>Add New Artist</h1>
          
          <form onSubmit={handleSubmit} className={styles.form}>
            {error && <div className={styles.error}>{error}</div>}

            <div className={styles.inputGroup}>
              <label>Full Name</label>
              <input type="text" name="fullName" value={formData.fullName} onChange={handleChange} required placeholder="Leonardo da Vinci" />
            </div>

            <div className={styles.row}>
              <div className={styles.inputGroup}>
                <label>Nationality</label>
                <input type="text" name="nationality" value={formData.nationality} onChange={handleChange} required placeholder="Italian" />
              </div>
              <div className={styles.inputGroup}>
                <label>Birth Date</label>
                <input type="date" name="birthDate" value={formData.birthDate} onChange={handleChange} />
              </div>
            </div>

            <div className={styles.inputGroup}>
              <label>Birth Place</label>
              <input type="text" name="birthPlace" value={formData.birthPlace} onChange={handleChange} placeholder="Vinci, Italy" />
            </div>

            <div className={styles.inputGroup}>
              <label>Biography</label>
              <textarea name="biography" value={formData.biography} onChange={handleChange} placeholder="Brief history of the artist..." />
            </div>

            <div className={styles.actions}>
              <GenericButton type="action" fullWidth={true} disabled={loading}>
                {loading ? 'Creating...' : 'Create Artist'}
              </GenericButton>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default AddArtistModal;

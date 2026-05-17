import React, { useState } from 'react';
import styles from './ExportModal.module.css';
import GenericButton from '../GenericButton/GenericButton';
import { exportArtworks } from '../../services/art';

const ExportModal = ({ onClose }) => {
  const [format, setFormat] = useState('CSV');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleExport = async () => {
    setLoading(true);
    setError('');

    try {
      const blob = await exportArtworks(format);
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `artworks_export.${format.toLowerCase()}`;
      document.body.appendChild(a);
      a.click();
      window.URL.revokeObjectURL(url);
      document.body.removeChild(a);
      onClose();
    } catch (err) {
      setError(err.message || 'Failed to export artworks.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.overlay} onClick={onClose}>
      <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
        <button className={styles.closeBtn} onClick={onClose}>&times;</button>

        <div className={styles.content}>
          <h1>Export Collection</h1>
          <p className={styles.subtitle}>Choose the format for exporting.</p>

          <div className={styles.inputGroup}>
            <label>Format</label>
            <select
              value={format}
              onChange={(e) => setFormat(e.target.value)}
              className={styles.select}
            >
              <option value="CSV">CSV</option>
              <option value="JSON">JSON</option>
              <option value="XML">XML</option>
              <option value="DOC">DOC</option>
            </select>
          </div>

          {error && <div className={styles.error}>{error}</div>}

          <div className={styles.actions}>
            <GenericButton
              type="action"
              fullWidth={true}
              disabled={loading}
              onClick={handleExport}
            >
              {loading ? 'Exporting...' : 'Start Download'}
            </GenericButton>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ExportModal;

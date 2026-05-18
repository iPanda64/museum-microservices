import React, { useState } from 'react';
import styles from './AddUserModal.module.css';
import GenericButton from '../../../components/GenericButton/GenericButton';
import { createCredentials, createProfile } from '../../../services/admin';

const AddUserModal = ({ onClose, onSuccess }) => {
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    roleName: 'USER',
    email: '',
    phoneNumber: ''
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
      // 1. Create Credentials
      // Removed ROLE_ prefix as documentation shows "ADMIN", "EMPLOYEE", etc.
      const credResponse = await createCredentials({
        username: formData.username,
        password: formData.password,
        roleName: formData.roleName 
      });

      if (!credResponse || !credResponse.userId) {
        throw new Error('User was created but the system did not return a valid ID.');
      }

      // 2. Create Profile
      const profileResponse = await createProfile(credResponse.userId, {
        email: formData.email,
        phoneNumber: formData.phoneNumber
      });

      if (!profileResponse) {
        // Fallback: Credentials created but profile failed. 
        // We might want to notify admin that profile creation failed.
        console.warn('Credentials created but profile creation failed.');
      }

      onSuccess();
      onClose();
    } catch (err) {
      setError(err.message || 'An error occurred during user creation.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.overlay} onClick={onClose}>
      <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
        <h2>Add New User</h2>
        <form onSubmit={handleSubmit} className={styles.form}>
          {error && <div className={styles.error}>{error}</div>}

          <div className={styles.field}>
            <label>Username</label>
            <input 
              name="username" 
              value={formData.username} 
              onChange={handleChange} 
              required 
            />
          </div>

          <div className={styles.field}>
            <label>Password</label>
            <input 
              name="password" 
              type="password"
              value={formData.password} 
              onChange={handleChange} 
              required 
            />
          </div>

          <div className={styles.field}>
            <label>Role</label>
            <select name="roleName" value={formData.roleName} onChange={handleChange}>
              <option value="USER">Visitor</option>
              <option value="EMPLOYEE">Employee</option>
              <option value="MANAGER">Manager</option>
              <option value="ADMIN">Administrator</option>
            </select>
          </div>

          <div className={styles.field}>
            <label>Email Address</label>
            <input 
              name="email" 
              type="email"
              value={formData.email} 
              onChange={handleChange} 
              required 
            />
          </div>

          <div className={styles.field}>
            <label>Phone Number</label>
            <input 
              name="phoneNumber" 
              value={formData.phoneNumber} 
              onChange={handleChange} 
            />
          </div>

          <div className={styles.actions}>
            <GenericButton type="default" onClick={onClose} disabled={loading}>
              Cancel
            </GenericButton>
            <GenericButton type="action" disabled={loading}>
              {loading ? 'Creating...' : 'Create User'}
            </GenericButton>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AddUserModal;

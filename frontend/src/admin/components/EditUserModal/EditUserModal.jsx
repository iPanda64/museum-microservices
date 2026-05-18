import React, { useState, useEffect } from 'react';
import styles from './EditUserModal.module.css';
import GenericButton from '../../../components/GenericButton/GenericButton';
import { updateCredentials, updateProfile } from '../../../services/admin';

const EditUserModal = ({ user, onClose, onSuccess }) => {
  const [formData, setFormData] = useState({
    username: user.username || '',
    password: '', // Password is empty by default
    roleName: (user.roleName || 'ROLE_USER').replace('ROLE_', ''),
    email: user.email || '',
    phoneNumber: user.phoneNumber || ''
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
      // 1. Update Credentials
      const credData = {
        username: formData.username,
        roleName: formData.roleName
      };
      
      // Only include password if it was typed
      if (formData.password.trim()) {
        credData.password = formData.password;
      }

      await updateCredentials(user.userId, credData);

      // 2. Update Profile
      await updateProfile(user.userId, {
        email: formData.email,
        phoneNumber: formData.phoneNumber
      });

      onSuccess();
      onClose();
    } catch (err) {
      setError(err.message || 'An error occurred during user update.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.overlay} onClick={onClose}>
      <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
        <h2>Edit User #{user.userId}</h2>
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
            <label>New Password (Leave empty to keep current)</label>
            <input 
              name="password" 
              type="password"
              value={formData.password} 
              onChange={handleChange} 
              placeholder="••••••••"
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
              {loading ? 'Saving...' : 'Save Changes'}
            </GenericButton>
          </div>
        </form>
      </div>
    </div>
  );
};

export default EditUserModal;

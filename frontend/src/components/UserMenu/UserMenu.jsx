import React from 'react';
import styles from './UserMenu.module.css';
import { useAuth } from '../../context/AuthContext';
import GenericButton from '../GenericButton/GenericButton';

const UserMenu = () => {
  const { user, logout } = useAuth();

  const handleLogout = () => {
    logout();
    window.location.href = '/';
  };

  const handleLogin = () => {
    window.location.href = '/login.html';
  };

  if (user) {
    return (
      <div className={styles.userSection}>
        <span className={styles.username}>Hello, {user.username}</span>
        <GenericButton type="danger" onClick={handleLogout}>
          Logout
        </GenericButton>
      </div>
    );
  }

  return (
    <GenericButton type="action" onClick={handleLogin}>
      Login
    </GenericButton>
  );
};

export default UserMenu;

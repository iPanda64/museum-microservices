import React from 'react';
import styles from './UserMenu.module.css';
import { useAuth } from '../../context/AuthContext';
import Button from '../Button/Button';

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
        <Button type="logout" onClick={handleLogout}>
          Logout
        </Button>
      </div>
    );
  }

  return (
    <Button type="login" onClick={handleLogin}>
      Login
    </Button>
  );
};

export default UserMenu;

import React, { useState } from 'react';
import styles from './UserMenu.module.css';
import { useAuth } from '../../context/AuthContext';
import GenericButton from '../GenericButton/GenericButton';
import TelegramIcon from '../Icons/TelegramIcon';
import { getTelegramLink } from '../../services/user';

const UserMenu = () => {
  const { user, logout } = useAuth();
  const [loadingTelegram, setLoadingTelegram] = useState(false);

  const handleLogout = () => {
    logout();
    window.location.href = '/';
  };

  const handleLogin = () => {
    window.location.href = '/login.html';
  };

  const handleLinkTelegram = async () => {
    if (!user?.userId || loadingTelegram) return;
    
    setLoadingTelegram(true);
    const url = await getTelegramLink(user.userId);
    setLoadingTelegram(false);

    if (url) {
      window.location.href = url;
    } else {
      alert('Failed to get Telegram link. Please try again later or contact an administrator.');
    }
  };

  if (user) {
    return (
      <div className={styles.userSection}>
        <button 
          className={`${styles.telegramBtn} ${loadingTelegram ? styles.loading : ''}`} 
          onClick={handleLinkTelegram}
          disabled={loadingTelegram}
          title={loadingTelegram ? "Getting link..." : "Link Telegram Account"}
        >
          <TelegramIcon className={styles.telegramIcon} />
        </button>
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

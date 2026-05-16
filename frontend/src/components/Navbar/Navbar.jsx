import React from 'react';
import styles from './Navbar.module.css';
import UserMenu from '../UserMenu/UserMenu';
import { useAuth } from '../../context/AuthContext';
import { hasRole } from '../../utils/auth';

const Navbar = () => {
  const { user } = useAuth();

  return (
    <nav className={styles.navbar}>
      <div className={styles.logo} onClick={() => window.location.href = '/'}>
        Museum App
      </div>

      <div className={styles.navLinks}>
        <a href="/" className={styles.link}>Home</a>
        <a href="/art.html" className={styles.link}>Artworks</a>
        <a href="/artists.html" className={styles.link}>Artists</a>

        {hasRole('ADMIN') && (
          <a href="/admin.html" className={styles.link}>Users</a>
        )}

        {(hasRole('ADMIN') || hasRole('MANAGER')) && (
          <a href="/statistics.html" className={styles.link}>Statistics</a>
        )}
      </div>

      <UserMenu />
    </nav>
  );
};


export default Navbar;

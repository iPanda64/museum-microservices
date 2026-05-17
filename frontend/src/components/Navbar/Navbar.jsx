import React, { useState, useEffect } from 'react';
import styles from './Navbar.module.css';
import UserMenu from '../UserMenu/UserMenu';
import { useAuth } from '../../context/AuthContext';
import { hasRole } from '../../utils/auth';

const Navbar = () => {
  const { user } = useAuth();
  const [scrolled, setScrolled] = useState(false);

  useEffect(() => {
    const handleScroll = () => {
      setScrolled(window.scrollY > 20);
    };

    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  return (
    <nav className={`${styles.navbar} ${scrolled ? styles.scrolled : ''}`}>
      <div className={styles.logo} onClick={() => window.location.href = '/'}>
        Museum
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

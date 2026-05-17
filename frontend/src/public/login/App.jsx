import React, { useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import GenericButton from '../../components/GenericButton/GenericButton';
import styles from './Login.module.css';

const App = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const response = await fetch('/api/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password }),
      });

      if (response.ok) {
        const token = await response.text();
        await login(token, username);
        window.location.href = '/';
      } else {
        const errorData = await response.text();
        setError(errorData || 'Login failed. Please check your credentials.');
      }
    } catch (err) {
      setError('An error occurred during login. Please try again later.');
      console.error('Login error:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.pageWrapper}>
      <main className={styles.container}>
        <div className={styles.loginCard}>
          <h1>Hello again!</h1>
          <form onSubmit={handleSubmit} className={styles.form}>
            {error && <div className={styles.error}>{error}</div>}

            <div className={styles.inputGroup}>
              <label htmlFor="username">Username</label>
              <input
                id="username"
                type="text"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                required
              />
            </div>

            <div className={styles.inputGroup}>
              <label htmlFor="password">Password</label>
              <input
                id="password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
            </div>

            <GenericButton type="action" fullWidth={true} disabled={loading}>
              {loading ? 'Logging in...' : 'Login'}
            </GenericButton>
          </form>
          <div className={styles.footer}>
            <a href="/">← Back to Home</a>
          </div>
        </div>
      </main>
    </div>
  );
};

export default App;

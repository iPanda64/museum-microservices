import React, { createContext, useContext, useState, useEffect } from 'react';
import { getToken, setToken as saveToken, removeToken as clearToken, getUser } from '../utils/auth';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = getToken();
    const storedUsername = sessionStorage.getItem('museum_username');

    if (token) {
      const userData = getUser();
      if (userData) {
        setUser({
          ...userData,
          username: storedUsername || 'User'
        });
      }
    }
    setLoading(false);
  }, []);

  const login = (token, username) => {
    saveToken(token);
    sessionStorage.setItem('museum_username', username);

    const userData = getUser();
    setUser({
      userId: userData?.userId || null,
      role: userData?.role || null,
      username: username
    });
  };

  const logout = () => {
    clearToken();
    sessionStorage.removeItem('museum_username');
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, login, logout, loading }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

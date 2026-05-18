import React, { useState, useEffect, useCallback } from 'react';
import styles from './UserList.module.css';
import UserRow from '../UserRow/UserRow';
import { getAllCredentials, getAllProfiles } from '../../../services/admin';

const UserList = ({ refreshTrigger, selectedRole }) => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchData = useCallback(async (showLoading = true) => {
    if (showLoading) setLoading(true);
    const [creds, profiles] = await Promise.all([
      getAllCredentials(),
      getAllProfiles()
    ]);

    // Merge by userId
    const profileMap = profiles.reduce((acc, p) => {
      acc[p.userId] = p;
      return acc;
    }, {});

    const merged = creds.map(c => ({
      ...c,
      ...(profileMap[c.userId] || {})
    }));

    setUsers(merged);
    setLoading(false);
  }, []);

  useEffect(() => {
    fetchData();
  }, [fetchData, refreshTrigger]);

  const filteredUsers = users.filter(user => {
    if (selectedRole === 'ALL') return true;
    
    // Normalize both for comparison
    const normalizedUserRole = user.roleName.replace('ROLE_', '');
    const normalizedSelectedRole = selectedRole.replace('ROLE_', '');
    
    return normalizedUserRole === normalizedSelectedRole;
  });

  if (loading) {
    return (
      <div className={styles.loadingWrapper}>
        <div className={styles.spinner}></div>
        <p>Loading user database...</p>
      </div>
    );
  }

  return (
    <div className={styles.listContainer}>
      <div className={styles.header}>
        <span>ID</span>
        <span>Username</span>
        <span>Email Address</span>
        <span>Role</span>
        <span>Phone</span>
        <span style={{ justifyContent: 'center' }}>Telegram</span>
      </div>

      {filteredUsers.length > 0 ? (
        filteredUsers.map(user => (
          <UserRow 
            key={user.userId} 
            user={user} 
            onUpdate={() => fetchData(false)} 
          />
        ))
      ) : (
        <div className={styles.empty}>
          <h3>No users matching your filter.</h3>
          <p>Try selecting a different role or add a new user.</p>
        </div>
      )}
    </div>
  );
};

export default UserList;

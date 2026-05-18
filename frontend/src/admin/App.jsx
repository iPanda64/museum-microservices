import React, { useState } from 'react';
import Navbar from '../components/Navbar/Navbar';
import UserList from './components/UserList/UserList';
import AddUserModal from './components/AddUserModal/AddUserModal';
import GenericButton from '../components/GenericButton/GenericButton';
import { AuthProvider, useAuth } from '../context/AuthContext';
import { hasRole } from '../utils/auth';
import styles from './App.module.css';

const AdminContent = () => {
  const { user, loading: authLoading } = useAuth();
  const [showAddModal, setShowAddModal] = useState(false);
  const [refreshTrigger, setRefreshTrigger] = useState(0);
  const [selectedRole, setSelectedRole] = useState('ALL');

  const handleRefresh = () => {
    setRefreshTrigger(prev => prev + 1);
  };

  if (authLoading) return <div className={styles.loader}>Checking permissions...</div>;

  if (!hasRole('ADMIN')) {
    return (
      <div className={styles.adminContainer}>
        <Navbar />
        <div className={styles.accessDenied}>
          <h1>Access Denied</h1>
          <p>You do not have the necessary permissions to view this page.</p>
          <GenericButton onClick={() => window.location.href = '/'}>Return Home</GenericButton>
        </div>
      </div>
    );
  }

  return (
    <div className={styles.adminContainer}>
      <Navbar />
      
      <div className={styles.welcomeSection}>
        <h1>User Management</h1>
      </div>

      <main className={styles.content}>
        <div className={styles.controls}>
          <div className={styles.filterGroup}>
            <label>Filter by Role:</label>
            <select 
              className={styles.roleSelect}
              value={selectedRole}
              onChange={(e) => setSelectedRole(e.target.value)}
            >
              <option value="ALL">All Roles</option>
              <option value="ROLE_ADMIN">Administrator</option>
              <option value="ROLE_MANAGER">Manager</option>
              <option value="ROLE_EMPLOYEE">Employee</option>
              <option value="ROLE_USER">Visitor</option>
            </select>
          </div>

          <GenericButton type="default" onClick={() => setShowAddModal(true)}>
            Add New User
          </GenericButton>
        </div>

        <UserList refreshTrigger={refreshTrigger} selectedRole={selectedRole} />
      </main>

      {showAddModal && (
        <AddUserModal 
          onClose={() => setShowAddModal(false)} 
          onSuccess={handleRefresh}
        />
      )}
    </div>
  );
};

const App = () => {
  return (
    <AuthProvider>
      <AdminContent />
    </AuthProvider>
  );
};

export default App;

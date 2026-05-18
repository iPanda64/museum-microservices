import React, { useState, useEffect } from 'react';
import styles from './UserRow.module.css';
import { getTelegramStatus, deleteUserCompletely, unlinkTelegram } from '../../../services/admin';
import GenericButton from '../../../components/GenericButton/GenericButton';
import EditUserModal from '../EditUserModal/EditUserModal';

const UserRow = ({ user, onUpdate }) => {
  const [telegramLinked, setTelegramLinked] = useState(null); // null = loading
  const [loading, setLoading] = useState(true);
  const [showEditModal, setShowEditModal] = useState(false);
  const [isDeleting, setIsDeleting] = useState(false);
  const [isUnlinking, setIsUnlinking] = useState(false);

  useEffect(() => {
    let isMounted = true;
    const fetchStatus = async () => {
      setLoading(true);
      const linked = await getTelegramStatus(user.userId);
      if (isMounted) {
        setTelegramLinked(linked);
        setLoading(false);
      }
    };
    fetchStatus();
    return () => { isMounted = false; };
  }, [user.userId]);

  const handleUnlinkTelegram = async () => {
    if (!telegramLinked || isUnlinking) return;
    
    if (!window.confirm(`Are you sure you want to unlink Telegram for user "${user.username}"?`)) {
      return;
    }

    setIsUnlinking(true);
    try {
      await unlinkTelegram(user.userId);
      setTelegramLinked(false);
    } catch (err) {
      alert(err.message || 'Failed to unlink Telegram.');
    } finally {
      setIsUnlinking(false);
    }
  };

  const handleDelete = async () => {
    if (!window.confirm(`Are you sure you want to delete user "${user.username}"? This action cannot be undone.`)) {
      return;
    }

    setIsDeleting(true);
    try {
      await deleteUserCompletely(user.userId);
      onUpdate();
    } catch (err) {
      alert(err.message || 'Failed to delete user.');
      setIsDeleting(false);
    }
  };

  return (
    <div className={`${styles.row} ${isDeleting ? styles.deleting : ''}`}>
      <div className={styles.id}>#{user.userId}</div>
      <div className={styles.username} title={user.username}>{user.username}</div>
      <div className={styles.email} title={user.email}>{user.email || 'N/A'}</div>
      <div className={styles.role}>
        {user.roleName.replace('ROLE_', '') === 'USER' ? 'VISITOR' : user.roleName.replace('ROLE_', '')}
      </div>
      <div className={styles.phone}>{user.phoneNumber || 'N/A'}</div>
      <div className={styles.telegram}>
        {loading || isUnlinking ? (
          <div className={styles.loading} title={isUnlinking ? "Unlinking..." : "Checking Telegram status..."} />
        ) : (
          <span 
            className={`${styles.badge} ${telegramLinked ? styles.linked : styles.notLinked} ${telegramLinked ? styles.clickable : ''}`}
            onClick={telegramLinked ? handleUnlinkTelegram : undefined}
            title={telegramLinked ? "Click to unlink Telegram" : ""}
          >
            {telegramLinked ? 'LINKED' : 'NOT LINKED'}
          </span>
        )}
      </div>
      <div className={styles.actions}>
        <GenericButton 
          type="default" 
          onClick={() => setShowEditModal(true)}
          disabled={isDeleting}
        >
          Edit
        </GenericButton>
        <GenericButton 
          type="danger" 
          onClick={handleDelete}
          disabled={isDeleting}
        >
          {isDeleting ? '...' : 'Delete'}
        </GenericButton>
      </div>

      {showEditModal && (
        <EditUserModal 
          user={user} 
          onClose={() => setShowEditModal(false)} 
          onSuccess={onUpdate}
        />
      )}
    </div>
  );
};

export default UserRow;

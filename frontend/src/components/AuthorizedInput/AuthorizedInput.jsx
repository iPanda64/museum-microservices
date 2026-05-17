import React from 'react';
import styles from './AuthorizedInput.module.css';
import { hasRole } from '../../utils/auth';

const AuthorizedInput = ({ 
  value, 
  onChange, 
  requiredRoles = ['ADMIN', 'MANAGER', 'EMPLOYEE'], 
  className = '', 
  placeholder = '',
  tag: Tag = 'div',
  fontSize,
  type = 'text'
}) => {
  const isAuthorized = requiredRoles.some(role => hasRole(role));
  const style = fontSize ? { fontSize } : {};

  if (!isAuthorized) {
    return (
      <Tag 
        className={`${styles.readOnly} ${className}`} 
        style={style}
      >
        {value}
      </Tag>
    );
  }

  return (
    <input 
      type={type}
      className={`${styles.input} ${styles.editable} ${className}`}
      value={value}
      onChange={(e) => onChange(e.target.value)}
      placeholder={placeholder}
      style={style}
    />
  );
};

export default AuthorizedInput;

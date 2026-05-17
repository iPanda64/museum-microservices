import React from 'react';
import styles from './GenericButton.module.css';

const GenericButton = ({ 
  children, 
  onClick, 
  type = 'default', 
  disabled = false, 
  className = '',
  fullWidth = false 
}) => {
  const buttonClass = `
    ${styles.button} 
    ${styles[type]} 
    ${fullWidth ? styles.fullWidth : ''} 
    ${className}
  `.trim();

  return (
    <button
      className={buttonClass}
      onClick={onClick}
      disabled={disabled}
    >
      {children}
    </button>
  );
};

export default GenericButton;

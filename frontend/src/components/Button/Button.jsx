import React from 'react';
import styles from './Button.module.css';

const Button = ({ children, onClick, type = 'default', disabled = false, className = '' }) => {
  const buttonClass = `${styles.button} ${styles[type]} ${className}`;

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

export default Button;

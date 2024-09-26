import React, { useState, useContext } from 'react';
import { AuthContext } from '../../Context/AuthContext';
import styles from '../../css/Menubar.module.css';

const DarkModeToggle = () => {
  const { isDarkMode, toggleDarkMode } = useContext(AuthContext);
  const [isOn, setIsOn] = useState(isDarkMode);

  const handleToggle = () => {
    console.log(handleToggle+"handleToggle 실행")
    setIsOn(!isOn);
    toggleDarkMode();
  };

  return (
    <button
      className={`${styles.toggleButton} ${isOn ? styles.toggleButtonOn : ''}`}
      onClick={handleToggle}
    >
      {isOn ? 'Light' : '🌙 Dark'}
    </button>
  );
};

export default DarkModeToggle;

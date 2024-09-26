import React from 'react';
import { useContext } from 'react';
import { AuthContext } from '../Context/AuthContext';

const JoinModal = ({ closeModal }) => {
  const { modalMessage } = useContext(AuthContext);  

  return (
    <div>
      <p>{modalMessage}</p>
      <button onClick={closeModal}>닫기</button>
    </div>
  );
};

export default JoinModal;

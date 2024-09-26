import React, { useState } from 'react';
import axios from 'axios';

function EmailDuplicateModal ({ closeModal }) {
  const [email, setEmail] = useState('');
  const [isDuplicate, setIsDuplicate] = useState(false);

  const handleDuplicateCheck = async () => {
    try {
      const response = await axios.get('http://localhost/auth/v1/validate/join', {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('accessToken')}`
        },
        params: {
          email,
        },
      });

      setIsDuplicate(response.data.isDuplicate);
    } catch (error) {
      console.error('이메일 중복 확인 실패:', error);
    }
  };


  return (
    <div>
        <input
          type="text"
          placeholder="이메일"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        <button onClick={handleDuplicateCheck}>중복 확인</button>
        {isDuplicate && <span>이메일이 중복되었습니다.</span>}
        <span onClick={closeModal}>취소</span>
      </div>
  );
};

export default EmailDuplicateModal;

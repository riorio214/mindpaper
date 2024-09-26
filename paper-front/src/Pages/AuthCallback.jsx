import React, { useContext, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { AuthContext } from '../Context/AuthContext';

const AuthCallback = () => {
    const { onLoginSuccess } = useContext(AuthContext);
    const navigate = useNavigate();

    useEffect(() => {
        async function handleSocialLogin() {
            try {
                const response = await axios.get('http://localhost/auth/oauth/token', {
                    withCredentials: true,
                });
                onLoginSuccess(response);
            } catch (error) {
                console.error('로그인 오류 :', error);
            }
        }

        handleSocialLogin().then(r => console.log('social login done'));
    }, [onLoginSuccess, navigate]);

    return (
        <div>
            소셜 로그인 처리 중...
        </div>
    );
};

export default AuthCallback;

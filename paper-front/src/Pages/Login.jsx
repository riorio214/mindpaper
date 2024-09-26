import React, {useCallback, useContext} from 'react';
import D from '../css/LoginFormD.module.css';
import L from '../css/LoginFormL.module.css';
import KakaoLogo from '../Image/kakao.png';
import GoogleLogo from '../Image/google.png'
import { useNavigate } from 'react-router-dom';
import Modal from 'react-modal';
import { AuthContext } from '../Context/AuthContext';
import axios from "axios";

const SocialKakao = React.memo(function SocialKakao() {
    const { isDarkMode } = useContext(AuthContext);
    // 로컬호스트의 백엔드 서버를 통해 카카오 로그인을 시작하는 URL
    const BACKEND_OAUTH2_KAKAO_URL = process.env.REACT_APP_BACKEND_OAUTH2_KAKAO_URL;
    const handleLogin = useCallback(() => {
        // 백엔드 서버를 통한 로그인 프로세스 시작
        window.location.href = BACKEND_OAUTH2_KAKAO_URL;
    }, [BACKEND_OAUTH2_KAKAO_URL]);
    return (
        <button className={isDarkMode ? D.kakao : L.kakao} onClick={handleLogin}>
            <img src={KakaoLogo} alt="Kakao"
                 style={{marginRight: '10px', verticalAlign: 'middle', width: '20px', height: '20px' }} />
            카카오로 시작하기
        </button>
    );
});

const SocialGoogle = React.memo(function SocialGoogle() {
    const {isDarkMode } = useContext(AuthContext);
    const BACKEND_OAUTH2_GOOGLE_URL = process.env.REACT_APP_BACKEND_OAUTH2_GOOGLE_URL;
    const handleLogin = useCallback(() => {
        window.location.href = BACKEND_OAUTH2_GOOGLE_URL;
    }, [BACKEND_OAUTH2_GOOGLE_URL]);
    return (
        <button className={isDarkMode ? D.google : L.google} onClick={handleLogin}>
            <img src={GoogleLogo} alt="google"
                 style={{marginRight: '10px', verticalAlign: 'middle', width: '20px', height: '20px'}}/>
            구글로 시작하기
        </button>
    );
});

function Login() {
    const {email, setEmail, password, setPassword, isModalOpen, setIsModalOpen, modalMessage, handleLogin, isDarkMode } = useContext(AuthContext);
    let navigate = useNavigate();
    return (
        <div className={isDarkMode ? D.main : L.main}>
            <div className={isDarkMode ? D.container : L.container}>
                <div className={isDarkMode ? D.wrapper : L.wrapper}>
                    <h1 onClick={() => navigate('/')}>로그인</h1>
                    <form>
                        <div className={isDarkMode ? D.inputbox : L.inputbox}>
                            <input type="text" value={email} onChange={(e) => setEmail(e.target.value)}
                                   placeholder="이메일" required/>
                            <i className='bx bxs-user'></i>
                        </div>
                        <div className={isDarkMode ? D.inputbox : L.inputbox}>
                            <input type="password" value={password} onChange={(e) => setPassword(e.target.value)}
                                   placeholder="패스워드" required/>
                            <i className='bx bx-lock-alt'></i>
                        </div>
                        <div className={isDarkMode ? D.rememberforgot : L.rememberforgot}>
                            <label>
                                <input type="checkbox"/>아이디를 저장
                            </label>
                            <p onClick={() => navigate('/join')}>회원이 아니신가요?</p>
                        </div>
                        <button type="button" onClick={() => handleLogin(email, password)}
                                className={isDarkMode ? D.btn : L.btn}>로그인
                        </button>
                        <SocialKakao/>
                        <SocialGoogle/>
                    </form>
                </div>
            </div>
            <Modal
                isOpen={isModalOpen}
                onRequestClose={() => setIsModalOpen(false)}
                contentLabel="Error Modal"
                className={isDarkMode ? L.modal: D.modal}
                overlayClassName={isDarkMode ? D.modalOverlay : L.modalOverlay }
            >
                <div className={isDarkMode ? D.modalContent: L.modalContent} >
                    <h2>Error</h2>
                    <p>{modalMessage}</p>
                    <button onClick={() => setIsModalOpen(false)}>Close</button>
                </div>
            </Modal>

        </div>
    );
}

export default React.memo(Login);
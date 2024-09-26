import React, { useState, useEffect,useContext } from 'react';
import D from '../css/JoinFormD.module.css';
import L from '../css/JoinFormL.module.css';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { AuthContext } from '../Context/AuthContext';
import EmailDuplicateModal from './EmailDuplicateModal';
import JoinModal from './JoinModal';
import Modal from 'react-modal';


function Join() {
    const { isDarkMode,isModalOpen,openModal,closeModal,setIsModalOpen,setModalMessage } = useContext(AuthContext);
    const [isEmailDuplicateModalOpen, setIsEmailDuplicateModalOpen] = useState(false);
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [passwordConfirm, setPasswordConfirm] = useState('');
    const [name, setName] = useState('');
    let navigate = useNavigate();
    
    useEffect(() => {
        const link = document.createElement('link');
        link.href = 'https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css';
        link.rel = 'stylesheet';
        document.head.appendChild(link);
        return () => {
            document.head.removeChild(link);
        };
    }, []);

    async function getData(email, password, name) {
        try {
            const response = await axios.post('http://localhost/auth/v1/signup', {
                email,
                password,
                name
            });
            console.log(response);
            navigate('/Login'); // 회원가입 성공 후 로그인 페이지로 이동
        } catch (error) {
            console.error(error);
            let errorMessage = "회원가입 중 문제가 발생했습니다.";
            if (error.response && error.response.data) {
                // 백엔드에서 전달된 에러 메시지가 있다면 사용
                errorMessage = error.response.data.message || errorMessage;
            }
            // 사용자에게 에러 메시지 표시
            setModalMessage(errorMessage);
            setIsModalOpen(true);
        } 
    }

    const handleSubmit = (e) => {
        e.preventDefault();
        if(password !== passwordConfirm){
            setModalMessage('패스워드가 일치하지 않습니다.');
            setIsModalOpen(true);
            return;
        }
        getData(email, password, name).then(r => console.log(r));
    };
    const handleEmailDuplicateCheck = () => {
        setIsEmailDuplicateModalOpen(true);
    };

    return (
        <div className={isDarkMode ? D.main : L.main}>
            <div className={isDarkMode ? D.container : L.container}>
                <div className={isDarkMode ? D.wrapper : L.wrapper}>
                    <h1 onClick={() => navigate('/')}>회원가입</h1>
                    <form onSubmit={handleSubmit}>
                        <div className={isDarkMode ? D.inputbox2 : L.inputbox2}>
                            <input type="text" placeholder="이메일" required value={email} onChange={(e) => setEmail(e.target.value)}/>
                            <button className={isDarkMode ? D.emailtest:L.emailtest}onClick={handleEmailDuplicateCheck}>중복확인</button>
                            {isEmailDuplicateModalOpen && (
                                <Modal
                                    isOpen={isEmailDuplicateModalOpen}
                                    onRequestClose={() => setIsEmailDuplicateModalOpen(false)}
                                    contentLabel="Email Duplicate Modal"
                                >
                                    <EmailDuplicateModal closeModal={() => setIsEmailDuplicateModalOpen(false)}/>
                                </Modal>
                            )}
                        </div>
                        <div className={isDarkMode ? D.inputbox : L.inputbox}>
                            <input type="password" placeholder="패스워드" required value={password} onChange={(e) => setPassword(e.target.value)}/>
                            <i className='bx bx-lock-alt'></i>
                        </div>
                        <div className={isDarkMode ? D.inputbox : L.inputbox}>
                            <input type="password" placeholder="패스워드 확인" required value={passwordConfirm} onChange={(e) => setPasswordConfirm(e.target.value)}/>
                            <i className='bx bx-lock-alt'></i>
                        </div>
                        <div className={isDarkMode ? D.inputbox : L.inputbox}>
                            <input type="text" placeholder="닉네임" required value={name} onChange={(e) => setName(e.target.value)}/>
                        </div>
                        <button type="submit" className={isDarkMode ? D.btn : L.btn}>회원가입</button>          
                          <Modal
                            isOpen={isModalOpen}
                            onRequestClose={closeModal}
                            contentLabel="Write Modal"
                          >
                            <JoinModal closeModal={closeModal}/>
                          </Modal>
                    </form>
                </div>
            </div>
        </div>
    );
}

export default React.memo(Join);
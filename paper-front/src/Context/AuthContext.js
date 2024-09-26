import React, {createContext, useEffect, useState} from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import handleSocialLogin from '../Pages/AuthCallback';

export const AuthContext = createContext();
const JWT_EXPIRY_TIME = 24 * 3600 * 1000;

export const AuthProvider = ({ children }) => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [modalMessage, setModalMessage] = useState('');
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [isDarkMode, setIsDarkMode] = useState(false);
    const [inputValue, setInputValue] = useState('');
    const [papers, setPapers] = useState([]);
    const [userEmail, setUserEmail] = useState('');
    const [showPaper, setShowPaper] = useState([]); // []
    const [showPages, setShowPages] = useState([]);
    const [pageId, setPageId] = useState(0);
    const [title, setTitle] = useState('');
    const [paperId, setPaperId] = useState(0);

    let navigate = useNavigate();


    axios.defaults.baseURL = 'http://localhost'; // 기본 URL 설정

    axios.interceptors.request.use(
        config => {
            const accessToken = axios.defaults.headers.common['Authorization'];
            if (accessToken) {
                config.headers['Authorization'] = accessToken;
            }
            return config;
        },
        error => {
            return Promise.reject(error);
        }
    );

    const toggleDarkMode = () => {
        setIsDarkMode(!isDarkMode);
    };

    const onLoginSuccess = async response => {
        const { accessToken } = response.data.data;
        if (localStorage.getItem('accessToken')) {
            localStorage.removeItem('accessToken');
        }
        localStorage.setItem('accessToken', accessToken);
        axios.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;
        setIsLoggedIn(true);
        setTimeout(silentRefresh, JWT_EXPIRY_TIME - 60000);
        await checkTitleExistence();
    }
        async function silentRefresh(){
            if (!isLoggedIn) return;
            try {
                const accessToken = localStorage.getItem('accessToken');
                const response = await axios.post('/auth/v1/refresh', {}, {
                    withCredentials: true,
                    headers:{
                        Authorization: `Bearer ${localStorage.getItem('accessToken')}`
                    }
            });
            await onLoginSuccess(response);
        } catch (error) {
            console.error('토큰 발급 오류', error);
            if (error.response.status === 401) {
                // 토큰이 만료된 경우 로그인 페이지로 이동
                navigate("/Login");
            }
        }
    }

    async function onClickPage() {
        try {
            const accessToken = localStorage.getItem('accessToken');
            const response = await axios.post('/main/v1/rolls/page', {
                title:inputValue,
            },{
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
            });
            navigate("/Page");
        } catch (error) {
            console.error("페이지 생성 실패:", error);
        }
    }

    async function checkTitleExistence() {
        try {
            const accessToken = localStorage.getItem('accessToken');
            if (!accessToken) {
                navigate("/Login");
                return;
            }
            const response = await axios.get('/main/v1/validate',{
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
            });
            if (response.data) {
                navigate("/Page");
            } else {
                navigate("/Title");
            }
        } catch (error) {
            console.error("오류", error);
            if (error.response.status === 401) {
                // 토큰이 만료된 경우 로그인 페이지로 이동
                navigate("/Login");
            }
        }
    }

    async function handleLogin(email, password) {
        try {
            const response = await axios.post('/auth/v1/login', {
                email,
                password,
            });
            await onLoginSuccess(response);
        } catch (error) {
            console.error('로그인 오류 :', error);
            for (const key in error) {
                if (error.hasOwnProperty(key)) {
                    console.log(`${key}: ${error[key]}`);
                }
            }
            console.error(JSON.stringify(error));
        }
    }

    async function handleLogout() {
        try {
            await axios.post('/auth/v1/logout');
            axios.defaults.headers.common['Authorization'] = '';
            localStorage.removeItem('accessToken');
            localStorage.clear();
            setIsLoggedIn(false);
            navigate('/login');
        } catch (error) {
            console.error('로그아웃 오류 :', error);
        }
    }
    
    const showPage = async (id) => {
        try {
            const response = await axios.get(`http://localhost/main/v1/rolls/${id}`, {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('accessToken')}`
                }
            });
            setShowPages(response.data);
            setTitle(response.data.title);
            setPapers(response.data.content);
        } catch (error) {
            console.error('페이지 불러오기 실패', error);
        }
    };    
    
    const openModal = () => {
        setIsModalOpen(true);
    };

    const closeModal = () => {
        setIsModalOpen(false);
    };

    useEffect(() => {
        const storedIsDarkMode = localStorage.getItem('isDarkMode');
        if (storedIsDarkMode !== null) {
          setIsDarkMode(JSON.parse(storedIsDarkMode));
        }
      }, []);
      
      useEffect(() => {
        localStorage.setItem('isDarkMode', JSON.stringify(isDarkMode));
      }, [isDarkMode]);

      useEffect(() => {
        const accessToken = localStorage.getItem('accessToken');
        if (accessToken) {
          setIsLoggedIn(true);
          axios.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;
          setTimeout(silentRefresh, JWT_EXPIRY_TIME - 60000);
        }
      }, []);
      
    return (
        <AuthContext.Provider
            value={{
                email,
                setEmail,
                password,
                setPassword,
                isModalOpen,
                setIsModalOpen,
                modalMessage,
                setModalMessage,
                handleLogin,
                isLoggedIn,
                setIsLoggedIn,
                handleSocialLogin,
                toggleDarkMode,
                isDarkMode,
                onClickPage,
                inputValue,
                setInputValue,
                silentRefresh,
                showPaper,
                papers,setPapers,
                userEmail,setUserEmail,
                onLoginSuccess,
                handleLogout,
                showPage,
                showPages,
                pageId, 
                setPageId,
                title,
                openModal,
                closeModal,
            }}
        >
            {children}
        </AuthContext.Provider>
    );
};

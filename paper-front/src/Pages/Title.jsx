    import L from '../css/TitleL.module.css';
    import D from '../css/TitleD.module.css';
    import React, {useContext,useEffect} from 'react';
    import {useNavigate} from "react-router-dom";
    import Menubar from '../Component/Menubar/Header';
    import mainImage from '../Image/main.png';
    import mainDImage from '../Image/mainD.png';
    import {AuthContext} from "../Context/AuthContext";
    
    const Logo = React.memo(function Logo() {
        
    const {isDarkMode} = useContext(AuthContext);
        let navigate = useNavigate();

        function handleClick(){
            navigate('/');
        }
        return (
            <img src={isDarkMode ? mainDImage : mainImage} className={isDarkMode ? D.logo : L.logo} alt='main' onClick={handleClick}/>
        );
    });

    function Title() {
        const { inputValue, onClickPage, setInputValue,isDarkMode,isLoggedIn,  onLoginSuccess} = useContext(AuthContext);

        useEffect(() => {
          const storedAccessToken = localStorage.getItem('accessToken');
          if (storedAccessToken) {
            onLoginSuccess({ data: { data: { accessToken: storedAccessToken } } });
          }
        }, [onLoginSuccess]);

        return (
            <div className={isDarkMode ? D.main : L.main}>
                {isLoggedIn ?(
                <form>
                    <div className={isDarkMode ? D.container : L.container}>
                        <Logo />
                        <Menubar/>
                        <h1>이야기를 만들어주세요!</h1>
                        <div className={isDarkMode ? D.inputbox : L.inputbox}>
                            <input type="text"
                                   placeholder="제목을 입력해주세요"
                                   value={inputValue}
                                   onChange={(e) => setInputValue(e.target.value)}
                            />
                        </div>
                        <div className={isDarkMode ? D.btnbox : L.btnbox}>
                            <button type="button" onClick={() => onClickPage()} className={isDarkMode ? D.btn : L.btn}>페이지 생성</button>
                        </div>
                    </div>
                </form>
                ) : (
                    <p>로그인 안됨</p>
                )}
            </div>
        );
    }

    export default Title;

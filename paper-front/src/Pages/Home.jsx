import {useContext} from 'react';
import Menubar from '../Component/Menubar/Header'
import L from '../css/HomeL.module.css';
import D from '../css/HomeD.module.css';
import { useNavigate } from 'react-router-dom';
import menuImage from '../Image/menu.png';
import menuDImage from '../Image/menuD.png';
import { AuthContext } from '../Context/AuthContext';
function Home() {
  let navigate = useNavigate();

  const { isDarkMode } = useContext(AuthContext);

  
  function handleClick(){
    navigate('/login');
  }
  return (
      <div className={isDarkMode ? D.main : L.main}>
        <div className={isDarkMode ? D.container : L.container}>
          <Menubar/>
          <img src={isDarkMode ? menuDImage : menuImage} className={isDarkMode ? D.menuBar : L.menuBar} alt='menu' />
          <div>
            <p className={isDarkMode ? D.introduce : L.introduce}>
              여러분의 친구들과
              이야기를 만들어주세요!
            </p>
          </div>
          <button onClick={handleClick} className={isDarkMode ? D.btn : L.btn} type="button">롤링페이퍼 시작하기</button>
        </div>
      </div>
  );
}

export default Home;

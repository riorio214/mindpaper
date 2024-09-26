import React, { useEffect, useContext } from 'react';
import axios from 'axios';
import Modal from 'react-modal';
import L from '../css/PageL.module.css'; // 스타일이 정의된 파일 임포트
import D from '../css/PageD.module.css';
import Menubar from '../Component/Menubar/Header'; // Menubar 컴포넌트 임포트
import Plus from '../Image/plus.png'; // Plus 이미지 임포트
import Write from '../Pages/Write.jsx'; // Write 컴포넌트 임포트
import Share from '../Image/share.png';
import { AuthContext } from "../Context/AuthContext";

function Page() {
    const {
        showPage,
        setPageId,
        papers,
        title,
        isModalOpen,
        isDarkMode,
        openModal,
        closeModal
    } = useContext(AuthContext);

    const getPageId = async () => {
        try {
            const response = await axios.get('http://localhost/main/v1/rolls/id', {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('accessToken')}`
                }
            });
            return response.data.data;
        } catch (error) {
            console.error('페이지 아이디 불러오기 실패', error);
            throw error;
        }
    };


    const handleDelete = async (id) => {
        try {
            const accessToken = localStorage.getItem('accessToken');
            if (window.confirm('정말 삭제하시겠습니까?')) {
                const response = await axios.post(`http://localhost/main/v1/rolls/${id}`, null, {
                    headers: {
                        Authorization: `Bearer ${accessToken}`
                    }
                });
                if (response.status === 200) {
                    // 삭제 성공
                    console.log('페이퍼 삭제 성공', response.data);
                    alert('삭제되었습니다.');
                    window.location.reload();
                } else {
                    // 삭제 실패
                    console.error('페이퍼 삭제 실패', response.data);
                }
            }
        } catch (error) {
            console.error('페이퍼 삭제 실패', error);
            // 에러 처리
        }
    };

    useEffect(() => {
        const initialize = async () => {
            try {
                const id = await getPageId();
                setPageId(id);
                await showPage(id);
            } catch (error) {
                console.error('Error during initialization', error);
            }
        };
        initialize(getPageId, setPageId, showPage);
    }, []);

    const deletePaper = (index) => {
        handleDelete(index);
    };

    return (
        <div id="wrap">
            <div className={isDarkMode ? D.main : L.main}>
                <div className={isDarkMode ? D.mainpage : L.mainpage}>
                    <div className={isDarkMode ? D.container : L.container}>
                        <div className={isDarkMode ? D.headerbox1 : L.headerbox1}>
                            <div className={isDarkMode ? D.headerbox2 : L.headerbox2}>
                                <img src={Share} className={isDarkMode ? D.img : L.img} alt="Share"/>
                                <h1 className={isDarkMode ? D.h1F : L.h1F}>{title}</h1>
                                <Menubar/>
                            </div>
                            <div className={isDarkMode ? D.headerbox3 : L.headerbox3}></div>
                        </div>
                        <div className={isDarkMode ? D.paperlistbox1 : L.paperlistbox1}>
                            {papers.length}개 작성
                        </div>
                        <section className={isDarkMode ? D.post1 : L.post1}>
                            <div className={isDarkMode ? D.post2 : L.post2}>
                                {papers.map((content, index) => (
                                    <div key={index}
                                    className={isDarkMode ? D.postit : L.postit}
                                    style={{
                                      transform: `rotate(${Math.floor(Math.random() * 21) - 10}deg)`,
                                    }}>
                                        <div className={isDarkMode ? D.postitcontext : L.postitcontext}>
                                            {content}
                                        </div>
                                    </div>
                                ))}
                                <div className={isDarkMode ? D.postbox : L.postbox}></div>
                            </div>
                        </section>
                        <div className={isDarkMode ? D.write : L.write}>
                            <div className={isDarkMode ? D.writebtn : L.writebtn}>
                                <img src={Plus} width="24" height="24" onClick={openModal} alt="Plus"/>
                                <Modal
                                    isOpen={isModalOpen}
                                    onRequestClose={closeModal}
                                    contentLabel="Write Modal"
                                >
                                    <Write closeModal={closeModal}/>
                                </Modal>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
export default Page;

import React, { useState } from "react";
import axios from "axios";
import styles from "../css/Write.module.css";

function Write({ closeModal }) {
    const [inputValue, setInputValue] = useState("");
    const maxLength = 100;
    const handleChange = (event) => {
        const { value } = event.target;
        if (value.length <= maxLength) {
            setInputValue(value);
        }
    };

    async function onClickPaper() {
        try {
            const response = await axios.post(
                "http://localhost/main/v1/rolls/paper",
                {
                    content: inputValue,
                },{
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
                    }
                }
            );
            closeModal();
            window.location.reload();
        } catch (error) {
            console.error("페이퍼 생성 실패:", error);
        }
    }

    return (
        <div id="wrap">
            <div className={styles.main}>
                <div className={styles.container}>
                    <session className={styles.box1}>
                        <session className={styles.box2}>
              <span className={styles.cancle} onClick={closeModal}>
                취소
              </span>
                            <span className={styles.next}>다음</span>
                            <br></br>
                        </session>
                        <div className={styles.contextbox}>
                            <div className={styles.contextbox2}>
                <textarea
                    color="#000000"
                    spellCheck="false"
                    style={{ height: "94px" }}
                    className={styles.context}
                    value={inputValue}
                    onChange={handleChange}
                ></textarea>
                            </div>
                            <div className={styles.contextbox3}>
                                <span className={styles.hidden}>익명으로 작성하기</span>
                                <button>on</button>
                            </div>
                            <div className={styles.contextbox4}></div>
                        </div>
                        <div className={styles.btnbox}>
                            <button
                                className={styles.btn}
                                color="dark"
                                onClick={onClickPaper}
                            >
                                페이퍼 생성
                            </button>
                        </div>
                    </session>
                </div>
            </div>
        </div>
    );
}

export default Write;

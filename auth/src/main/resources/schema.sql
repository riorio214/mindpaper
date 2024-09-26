CREATE DATABASE IF NOT EXISTS user;
USE user;

-- User 테이블
DROP TABLE IF EXISTS USER;
CREATE TABLE USER
(
    id       int unsigned NOT NULL AUTO_INCREMENT,
    email    varchar(50)  NOT NULL,
    password varchar(200) NOT NULL,
    name     varchar(50),
    created  timestamp   DEFAULT CURRENT_TIMESTAMP,
    updated  timestamp   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status   varchar(10) DEFAULT 'ACTIVE',
    role    varchar(10)
    CONSTRAINT USER_PK PRIMARY KEY (id)
);


CREATE DATABASE IF NOT EXISTS scheduler;

USE scheduler;

DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS schedule;

CREATE TABLE user (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(50) NOT NULL,   -- 이름
    email           VARCHAR(50) NOT NULL UNIQUE,    -- 이메일
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,    -- 작성일
    modified_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP     -- 수정일
);

CREATE TABLE schedule (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT,   -- 사용자 식별자(id) 외래키
    task            VARCHAR(255) NOT NULL,  -- 할 일
    password        VARCHAR(255) NOT NULL,  -- 비밀번호 (해시)
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,    -- 작성일
    modified_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,    -- 수정일
    FOREIGN KEY (user_id) REFERENCES user(id)
        ON DELETE SET NULL
);

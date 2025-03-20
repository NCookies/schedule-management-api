CREATE TABLE schedule (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    task            VARCHAR(500) NOT NULL,  -- 할 일
    author          VARCHAR(50) NOT NULL,   -- 작성자명
    password        VARCHAR(255) NOT NULL,  -- 비밀번호 (해시)
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,    -- 작성일
    modified_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 수정일
);

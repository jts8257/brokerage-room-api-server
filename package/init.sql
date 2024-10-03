CREATE DATABASE IF NOT EXISTS app_db;

CREATE USER IF NOT EXISTS 'db_user'@'%' IDENTIFIED BY 'db_user_password';
GRANT ALL PRIVILEGES ON app_db.* TO 'db_user'@'%';

FLUSH PRIVILEGES;

USE app_db;

CREATE TABLE IF NOT EXISTS users (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nick_name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(100)
);

INSERT IGNORE INTO users (nick_name, email, password) VALUES
    ('홍길동', 'hong@naver.com', '1234'),
    ('김철수', 'kim@naver.com', '1234'),
    ('우영희', 'woo@naver.com', '1234');

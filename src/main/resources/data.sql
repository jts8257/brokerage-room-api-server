INSERT INTO users (id, nick_name, email, password) VALUES
    (1, '홍길동', 'hong@naver.com', '1234'),
    (2, '김철수', 'kim@naver.com', '1234'),
    (3, '우영희', 'woo@naver.com', '1234')
ON DUPLICATE KEY UPDATE
    nick_name = VALUES(nick_name),
    email = VALUES(email),
    password = VALUES(password);

INSERT INTO transaction_types (id, name, is_deposit_only) VALUES
    (1, '전세', 'Y'),
    (2, '월세', 'N')
ON DUPLICATE KEY UPDATE
    is_deposit_only = VALUES(is_deposit_only);

INSERT INTO room_types (id, name) VALUES
    (1, '원룸'),
    (2, '투룸'),
    (3, '쓰리룸')
ON DUPLICATE KEY UPDATE
    name = VALUES(name);
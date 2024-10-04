CREATE TABLE IF NOT EXISTS users (
    id BIGINT NOT NULL PRIMARY KEY,
    nick_name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS transaction_types (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) UNIQUE,
    is_deposit_only VARCHAR(1) DEFAULT 'N' NOT NULL
);

CREATE TABLE IF NOT EXISTS room_types (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) UNIQUE
);

CREATE TABLE IF NOT EXISTS room_details (
    id BIGINT NOT NULL PRIMARY KEY,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS rooms (
    id BIGINT NOT NULL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    type_id INT NOT NULL,
    address_jibun VARCHAR(200),
    address_road VARCHAR(200),
    address_detail VARCHAR(200),
    detail_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_rooms_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_rooms_type FOREIGN KEY (type_id) REFERENCES room_types (id) ON DELETE CASCADE,
    CONSTRAINT fk_rooms_detail FOREIGN KEY (detail_id) REFERENCES room_details (id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS room_transactions (
    id BIGINT NOT NULL PRIMARY KEY,
    room_id BIGINT NOT NULL,
    transaction_type_id INT NOT NULL,
    rent_monthly DECIMAL(14, 2),
    deposit DECIMAL(16, 2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_room_transactions_room FOREIGN KEY (room_id) REFERENCES rooms (id) ON DELETE CASCADE,
    CONSTRAINT fk_room_transactions_transaction_type FOREIGN KEY (transaction_type_id) REFERENCES transaction_types (id) ON DELETE CASCADE
);
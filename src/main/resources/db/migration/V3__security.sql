CREATE TABLE IF NOT EXISTS users
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR(128) NOT NULL,
    password VARCHAR      NOT NULL,
    role     VARCHAR(32)  NOT NULL
    );

INSERT INTO users (username, password, role)
VALUES ('admin', '$2a$10$10GqyO9ZndGEVx9wSxgN0.KdChIpZjewtbf.VLHYiE2CBctzSlJva', 'ADMIN'),
       ('user', '$2a$10$10GqyO9ZndGEVx9wSxgN0.KdChIpZjewtbf.VLHYiE2CBctzSlJva', 'USER');

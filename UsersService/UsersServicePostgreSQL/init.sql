DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS groups CASCADE;
DROP TABLE IF EXISTS groups_users CASCADE;
DROP TABLE IF EXISTS requests_for_membership CASCADE;

CREATE TABLE IF NOT EXISTS users(
    user_id SERIAL PRIMARY KEY,
    login VARCHAR(50) NOT NULL,
    email VARCHAR(255) UNIQUE,
    phone_number VARCHAR(11),
    introduce VARCHAR(255),
    created_at TIMESTAMP
    );

INSERT INTO users (login, email, phone_number, introduce, created_at)
VALUES
    ('SANAN', 'smak_simov@mail.ru', '89955521357', 'Это собственно Я.\r tg: @maksimovss', CURRENT_TIMESTAMP),
    ('Casper', 'gazeta@gmail.com', '77777777777', 'Vsem piece!', CURRENT_TIMESTAMP),
    ('yshkin@yandex.ru', 'yshkin@yandex.ru', NULL, 'Я КСТА Тоже JAVA DEVELOPER. tg:@N0vaT', CURRENT_TIMESTAMP),
    ('izhanaev@mail.ru', 'izhanaev@mail.ru', NULL, 'iOS Developer. tg: @ilya_zhanaev', CURRENT_TIMESTAMP);

CREATE TABLE IF NOT EXISTS groups (
    group_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    title VARCHAR(255),
    owner_id INTEGER REFERENCES users(user_id) NOT NULL
    );

INSERT INTO groups (name, title, owner_id)
VALUES
    ('Школьники21', 'АНО ПУКОН Школа 21. https://21-school.ru', 1),
    ('Озерная 9', 'юберцы Озерная 9 ГЭНГ',1);


CREATE TABLE IF NOT EXISTS groups_users(
    user_id INTEGER REFERENCES users(user_id) NOT NULL,
    group_id INTEGER REFERENCES groups(group_id) NOT NULL
    );

INSERT INTO groups_users (user_id, group_id)
VALUES
    (1, 1),
    (3, 1),
    (1, 2),
    (4, 1);

CREATE TABLE IF NOT EXISTS requests_for_membership(
    request_id SERIAL PRIMARY KEY,
    group_id INTEGER REFERENCES groups(group_id) NOT NULL,
    user_id INTEGER REFERENCES users(user_id) NOT NULL,
    message VARCHAR(255)
    );

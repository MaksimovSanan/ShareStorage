DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE IF NOT EXISTS users(
user_id SERIAL PRIMARY KEY,
login VARCHAR(50) NOT NULL,
email VARCHAR(255) UNIQUE,
created_at TIMESTAMP
);

INSERT INTO users(login, email) VALUES ('Aboba','aboba@mail.com');
INSERT INTO users(login, email) VALUES ('Tom','tom@mail.com');
INSERT INTO users(login, email) VALUES ('Bob', 'bob@mail.com');
INSERT INTO users(login, email) VALUES ('Katy', 'katy@mail.com');

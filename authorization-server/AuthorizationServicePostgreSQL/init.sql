DROP TABLE IF EXISTS users_roles CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS roles CASCADE;

CREATE TABLE IF NOT EXISTS roles(
                                    role_id serial,
                                    role_name varchar(20) UNIQUE NOT NULL,
    PRIMARY KEY (role_id)
    );

CREATE TABLE IF NOT EXISTS users(
                                    user_id serial,
                                    first_name varchar(20),
    last_name varchar(20),
    email varchar(20) UNIQUE not null,
    password varchar(100) not null,
    PRIMARY KEY (user_id)
    );

CREATE TABLE IF NOT EXISTS users_roles (
                                           user_id integer NOT NULL,
                                           role_id integer NOT NULL,
                                           PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles (role_id) ON UPDATE CASCADE
    );

INSERT INTO roles(role_id, role_name)
VALUES (1, 'ROLE_ADMIN'), (2, 'ROLE_USER');

INSERT INTO users (first_name, last_name, email, password)
VALUES
    (NULL, NULL, 'smak_simov@mail.ru', '$2a$10$Gsjk5M.D2sY0RDjI.nn.b.ZMZY8geUBQgBljsPMQ5CEuVIVVZCFiu'),
    (NULL, NULL, 'gazeta@gmail.com', '$2a$10$XkOF.r12JhkXaqQ0YvnCIujctAWc.YYtWH6fuAHe6wSssolWQbYUK'),
    (NULL, NULL, 'yshkin@yandex.ru', '$2a$10$Uyz35SwqEY4Skzg9fBqaC.9RZEPXuD4RqPOpNUZhIu1LtBohNRLsa'),
    (NULL, NULL, 'izhanaev@mail.ru', '$2a$10$aR0qUF6GdCtLArJTaPDH8.p26Wfr4hdeH4T1tFq/SmeP72RLORhCS');


INSERT INTO users_roles (user_id, role_id)
VALUES
    (1, 2),
    (2, 2),
    (3, 2),
    (4, 2);
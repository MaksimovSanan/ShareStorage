#!/bin/bash

# Создание базы данных "authorized_users"
psql -U sanan -c "CREATE DATABASE authorized_users;"

# Выполнение SQL-скрипта
psql -U sanan -d authorized_users -c "
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
            (NULL, NULL, 'smak_simov@mail.ru', '\$2a\$10\$Gsjk5M.D2sY0RDjI.nn.b.ZMZY8geUBQgBljsPMQ5CEuVIVVZCFiu'),
            (NULL, NULL, 'gazeta@gmail.com', '\$2a\$10\$XkOF.r12JhkXaqQ0YvnCIujctAWc.YYtWH6fuAHe6wSssolWQbYUK'),
            (NULL, NULL, 'yshkin@yandex.ru', '\$2a\$10\$Uyz35SwqEY4Skzg9fBqaC.9RZEPXuD4RqPOpNUZhIu1LtBohNRLsa');

    INSERT INTO users_roles (user_id, role_id)
    VALUES
        (1, 2),
        (2, 2),
        (3, 2);

"

# Создание базы данных "items_service_db"
psql -U sanan -c "CREATE DATABASE users_service_db;"


# Выполнение SQL-скрипта
psql -U sanan -d users_service_db -c "
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
        ('yshkin@yandex.ru', 'yshkin@yandex.ru', NULL, 'Я КСТА Тоже JAVA DEVELOPER. tg:@N0vaT', CURRENT_TIMESTAMP);

    CREATE TABLE IF NOT EXISTS groups (
      group_id SERIAL PRIMARY KEY,
      name VARCHAR(100) NOT NULL,
      title VARCHAR(255),
      owner_id INTEGER REFERENCES users(user_id) NOT NULL
    );

    CREATE TABLE IF NOT EXISTS groups_users(
      user_id INTEGER REFERENCES users(user_id) NOT NULL,
      group_id INTEGER REFERENCES groups(group_id) NOT NULL
    );

    CREATE TABLE IF NOT EXISTS requests_for_membership(
      request_id SERIAL PRIMARY KEY,
      group_id INTEGER REFERENCES groups(group_id) NOT NULL,
      user_id INTEGER REFERENCES users(user_id) NOT NULL,
      message VARCHAR(255)
    );
"


# Создание базы данных "items_service_db"
psql -U sanan -c "CREATE DATABASE items_service_db;"

# Выполнение SQL-скрипта
psql -U sanan -d items_service_db -c "
    DROP TABLE IF EXISTS rental_items CASCADE;

    CREATE TABLE IF NOT EXISTS rental_items(
        item_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
        owner_id INTEGER NOT NULL,
        owner_name VARCHAR(100) NOT NULL,
        title VARCHAR(100) NOT NULL,
        description VARCHAR(250) NOT NULL,
        status INTEGER NOT NULL,
        cost_hour INTEGER,
        cost_day INTEGER
        );

    INSERT INTO rental_items (owner_id, owner_name, title, description, status, cost_hour, cost_day)
    VALUES
        (1, 'SANAN', 'Корм для птиц', 'Однажды я нашел на остановке корм для птиц. Он пролежал у меня дома месяца 3, после чего его пришлось выкинуть, потому что не смог найти кому из моих знакомых он нужен', 0, 0, 0),
        (1, 'SANAN', 'Табуретки ikea', 'Еще у меня на балконе лежат 2 ненужные табуретки(на самом деле не только они)', 0, 0, 0),
        (2, 'gazeta@gmail.com', 'Газета', 'Взял газету в метро у бабушки. Могу отдать', 0, 0, 0),
        (3, 'yshkin@yandex.ru', 'Усилитель слуха', 'Прибор рабочий, сам опробовал, 10 из 10', 0, 0, 0);


    DROP TABLE IF EXISTS rent_contracts CASCADE;

    CREATE TABLE IF NOT EXISTS rent_contracts(
        contract_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
        item_id INTEGER REFERENCES rental_items(item_id) NOT NULL,
        borrower_id INTEGER NOT NULL,
        borrower_name VARCHAR(100) NOT NULL,
        created_at TIMESTAMP NOT NULL,
        updated_at TIMESTAMP,
        reserved_to TIMESTAMP,
        reserved_from TIMESTAMP,
        comment VARCHAR(250),
        status INTEGER NOT NULL
        );

    INSERT INTO rent_contracts (item_id, borrower_id, borrower_name, created_at, updated_at, reserved_to, reserved_from, comment, status)
    VALUES
        (3, 1, 'SANAN', '2024-02-06 20:40:39.37881', NULL, '2024-02-07 23:59:00', '2024-02-06 23:59:00', 'ВЛАААААД, дай ПЖ газету до заввтра почитать', 205),
        (3, 3, 'yshkin@yandex.ru', '2024-02-07 17:05:18.159392', NULL, '2024-02-08 23:59:00', '2024-02-07 23:59:00', 'Никому не верь, отдай газету мне!', 205);
"


psql -U sanan -c "CREATE DATABASE image_server_db;"

# Выполнение SQL-скрипта
psql -U sanan -d image_server_db -c "
    DROP TABLE IF EXISTS users_image CASCADE;

    CREATE TABLE IF NOT EXISTS users_image(
        user_image_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
        user_id INTEGER NOT NULL,
        user_image_path VARCHAR(255)
        );

    INSERT INTO users_image (user_id, user_image_path)
    VALUES
        (2, 'user_2_picForShareStorageAvaVlada.jpeg'),
        (3, 'user_3_picForShareStorageAvaTolya.avif'),
        (1, 'user_1_cropped-p231106173215.jpg');


    DROP TABLE IF EXISTS items_image CASCADE;

    CREATE TABLE IF NOT EXISTS items_image(
        item_image_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
        item_id INTEGER NOT NULL,
        item_image_path VARCHAR(255)
        );

    INSERT INTO items_image (item_id, item_image_path)
    VALUES
        (2, 'item_2_picForShareStorage2.jpeg'),
        (3, 'item_3_picForStaheStorageVlad.jpeg'),
        (4, 'item_4_picForShareStorage4.jpeg'),
        (1, 'item_1_picForShareStorage1.jpeg');

"

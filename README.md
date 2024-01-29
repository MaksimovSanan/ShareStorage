
# Spring Cloud Rent Service RESTfull
## Микросервисное приложение Spring Boot с PostgreSQL в контейнерах Docker

Этот проект демонстрирует настройку приложения Spring Boot с использованием Spring Cloud, двумя микросервисами
с собственными базами данных PostgreSQL,
каждое из которых работает в отдельных контейнерах Docker.
Это достигается с использованием Docker Compose для оркестрации развертывания контейнеров.

## Структура проекта

Структура проекта выглядит следующим образом:

```plaintext
RESTApp/
│
├── EurekaServer
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       └── resources/
│   └── Dockerfile.EurekaServer
│
├── APIGateway
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       └── resources/
│   └── Dockerfile.APIGateway
│
├── WebClient
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       └── resources/
│   └── Dockerfile.WebClient
│
├── UsersService/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       └── resources/
│   ├── Dockerfile.UsersService
│   │
│   └── UsersServicePostgreSQL/
│       ├── Dockerfile.UsersServicePostgreSQL
│       └── init.sql
│
├── ItemsService/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       └── resources/
│   ├── Dockerfile.ItemssService
│   │
│   └── ItemsServicePostgreSQL/
│       ├── Dockerfile.ItemsServicePostgreSQL
│       └── init.sql
│
└── docker-compose.yml
```

- `UsersService/src/`: Содержит исходный код микросервиса для работы с пользователями.
- `UsersService/Dockerfile.UsersService`: Dockerfile для создания образа UsersService.
- `UsersService/UsersServicePostgreSQL/`: Содержит файлы инициализации базы данных PostgreSQL для UsersService.
- `UsersService/UsersServicePostgreSQL/Dockerfile.UsersServicePostgreSQL`: Dockerfile для создания образа PostgreSQL.
- `UsersService/UsersServicePostgreSQL/init.sql`: SQL-скрипт для создания схемы базы данных и вставки начальных данных.
- `ItemsService/src/`: Содержит исходный код микросервиса для работы с товарами.
- `itemsService/Dockerfile.ItemsService`: Dockerfile для создания образа ItemsService.
- `ItemsService/ItemsServicePostgreSQL/`: Содержит файлы инициализации базы данных PostgreSQL для ItemsService.
- `ItemsService/ItemsServicePostgreSQL/Dockerfile.ItemsServicePostgreSQL`: Dockerfile для создания образа PostgreSQL.
- `ItemsService/ItemsServicePostgreSQL/init.sql`: SQL-скрипт для создания схемы базы данных и вставки начальных данных.
- `docker-compose.yml`: Файл конфигурации Docker Compose для оркестрации развертывания обоих контейнеров.

## UsersService

![UsersService](misc/images/UsersService.png)

## ItemsService

![ItemsService](misc/images/ItemsService.png)

## Schema DB

![SchemaDB](misc/images/SchemaDB.png)


## Начало работы

### P.S. Problem(in progress)
С добавление Spring security у меня возникли проблемы с организацией всего проекта в докере.
Работаю над запуском с помощью docker-compose.
Пока запустить получится только на локалке.
Для этого придется создать таблицы postgres самостоятельно и задать переменные в application.properties в модулях security-server, itemsservice, usersservice.
После чего последовательно запустить eurekaserver, itemsservice, usersservice, security-server, webclient.
# :(

### P.S.S:
Для вавшего удобства я подготовил несколько скриптовв для запуска приложения:
1. Создание баз данных и их инициализация:
   ```bash
   sh build-scripts/init_databases.sh
   ```
   
2. Сборка модулей приложения:
   ```bash
   sh build-scripts/build-modules.sh
   ```
   
3. Запуск приложения:
   ```bash
   sh build-scripts/start_services.sh
   ```
   
4. Дождитесь развертывания приложения в контейнерах

5. Доступ к приложению: http://localhost:8080
ps -ef | grep java

Следуйте этим шагам для запуска проекта:

1. Клонируйте репозиторий:

   ```bash
   git clone git@github.com:MaksimovSanan/SpringLessons.git
   cd SpringLessons/SpringREST/RESTApp
   ```

2. Соберите jar файлы каждого микросервиса
   ```bash
   mvnw install
   ```

3. Соберите и запустите Docker-контейнеры:

   ```bash
   docker-compose up -d
   ```

4. Дождитесь развертывания приложения в контейнерах

5. Доступ к приложению: http://localhost:8080

## Настройка

### Приложение Spring Boot

В файле `*/src/main/resources/application.properties` вы можете настроить параметры подключения к базе данных PostgreSQL:

```properties
spring.datasource.url=jdbc:postgresql://{postgres_container}:{5432}/{db_name}
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

### База данных PostgreSQL

В файле `**/**PostgreSQL/Dockerfile.**` вы можете установить переменные среды для PostgreSQL, такие как имя базы данных, пользователя и пароль:

```Dockerfile
ENV POSTGRES_DB={db_name}
ENV POSTGRES_USER=myuser
ENV POSTGRES_PASSWORD=mypassword
```

## Дополнительные замечания

- Приложение Spring Boot доступно по адресу [http://localhost:8080](http://localhost:8080).
- База данных PostgreSQL для UsersService доступна по адресу [http://localhost:5432](http://localhost:5432).
- - База данных PostgreSQL для ItemsService доступна по адресу [http://localhost:5433](http://localhost:5433).
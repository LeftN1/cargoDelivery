DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS cargo_type;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS city;
DROP TABLE IF EXISTS destination;



CREATE TABLE roles
(
    id        INT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE users
(
    id    INT PRIMARY KEY AUTO_INCREMENT,
    login VARCHAR(20) UNIQUE NOT NULL,
    role INT REFERENCES roles(id),
    pass  VARCHAR(30)
);

CREATE TABLE cargo_type
(
    id         INT PRIMARY KEY AUTO_INCREMENT,
    cargo_type VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE orders
(
    id         INT PRIMARY KEY AUTO_INCREMENT,
    cargo_type INT REFERENCES cargo_type (id),
    weight     INT,
    volume     INT,
    date_time  DATETIME
);

CREATE TABLE city
(
    id         INT PRIMARY KEY AUTO_INCREMENT,
    country    VARCHAR(20) UNIQUE NOT NULL,
    district   VARCHAR(20)        NOT NULL,
    city_name  VARCHAR(20)        NOT NULL,
    longtitude FLOAT,
    latitude   FLOAT,
    UNIQUE (country, district, city_name)
);

CREATE TABLE destination
(
    id     INT PRIMARY KEY AUTO_INCREMENT,
    city   INT REFERENCES city (id),
    adress VARCHAR(30)
);
    
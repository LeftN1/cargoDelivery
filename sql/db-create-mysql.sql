DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS destination CASCADE;
DROP TABLE IF EXISTS cities CASCADE;
DROP TABLE IF EXISTS regions CASCADE;
DROP TABLE IF EXISTS countries CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS roles CASCADE;
DROP TABLE IF EXISTS cargo_types CASCADE;


CREATE TABLE roles
(
    role_id        INT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE users
(
    id    INT PRIMARY KEY AUTO_INCREMENT,
    login VARCHAR(20) UNIQUE NOT NULL,
    role  INT,
    pass  VARCHAR(30),
    FOREIGN KEY (role) REFERENCES roles (role_id) ON DELETE CASCADE
);

CREATE TABLE cargo_types
(
    id        INT PRIMARY KEY AUTO_INCREMENT,
    type_name VARCHAR(20) UNIQUE NOT NULL
);



CREATE TABLE countries
(
    id           INT PRIMARY KEY AUTO_INCREMENT,
    country_name VARCHAR(30) UNIQUE NOT NULL
);

CREATE TABLE regions
(
    region_id   INT PRIMARY KEY AUTO_INCREMENT,
    region_name VARCHAR(30) UNIQUE NOT NULL,
    country     INT,
    FOREIGN KEY (country) REFERENCES countries (id) ON DELETE CASCADE
);

CREATE TABLE cities
(
    city_id   INT PRIMARY KEY AUTO_INCREMENT,
    region    INT,
    city_name VARCHAR(30) NOT NULL,
    longitude FLOAT,
    latitude  FLOAT,
    UNIQUE (region, city_name),
    FOREIGN KEY (region) REFERENCES regions (region_id) ON DELETE CASCADE
);

CREATE TABLE destination
(
    id     INT PRIMARY KEY AUTO_INCREMENT,
    city   INT,
    adress VARCHAR(30),
	FOREIGN KEY (city) REFERENCES cities (city_id) ON DELETE CASCADE
);

CREATE TABLE orders
(
    id         INT PRIMARY KEY AUTO_INCREMENT,
    user_id    INT,
    adress     INT,
    cargo_type INT,
    weight     INT,
    volume     INT,
    date_time  TIMESTAMP,
	FOREIGN KEY (user_id)	REFERENCES users (id) ON DELETE CASCADE,
	FOREIGN KEY (adress)	REFERENCES destination (id) ON DELETE CASCADE,
	FOREIGN KEY (cargo_type)	REFERENCES cargo_types (id) ON DELETE CASCADE
);

INSERT INTO roles (role_name)
VALUES 
	('USER'),
    ('MANAGER'),
    ('ADMIN');
    
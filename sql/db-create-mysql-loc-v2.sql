DROP TABLE IF EXISTS delivery_status CASCADE;
DROP TABLE IF EXISTS deliveries CASCADE;
DROP TABLE IF EXISTS destination CASCADE;
DROP TABLE IF EXISTS cities CASCADE;
DROP TABLE IF EXISTS regions CASCADE;
DROP TABLE IF EXISTS countries CASCADE;
DROP TABLE IF EXISTS statuses CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS roles CASCADE;
DROP TABLE IF EXISTS cargo_types CASCADE;
DROP TABLE IF EXISTS translations CASCADE;
DROP TABLE IF EXISTS resources CASCADE;
DROP TABLE IF EXISTS locales CASCADE;

CREATE TABLE locales
(
	id         INT PRIMARY KEY AUTO_INCREMENT,
    locale_name VARCHAR(30)
);

CREATE TABLE resources
(
	id         INT PRIMARY KEY AUTO_INCREMENT
);

CREATE TABLE translations
(
    resource_id INT,
	locale_id INT,
    translation VARCHAR(30),
    UNIQUE(resource_id, locale_id),
    FOREIGN KEY (locale_id) REFERENCES locales (id) ON DELETE CASCADE,
    FOREIGN KEY (resource_id) REFERENCES resources (id) ON DELETE CASCADE
);

CREATE TABLE roles
(
    role_id     INT PRIMARY KEY AUTO_INCREMENT,
    role_name   VARCHAR(20) UNIQUE NOT NULL
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
    type_name INT NOT NULL,
    FOREIGN KEY (type_name) REFERENCES resources (id) ON DELETE CASCADE
);

CREATE TABLE countries
(
    id           INT PRIMARY KEY AUTO_INCREMENT,
    country_name INT NOT NULL,
    FOREIGN KEY (country_name) REFERENCES resources (id) ON DELETE CASCADE
);

CREATE TABLE regions
(
    region_id   INT PRIMARY KEY AUTO_INCREMENT,
    region_name INT NOT NULL,
    country     INT,
    FOREIGN KEY (country) REFERENCES countries (id) ON DELETE CASCADE,
    FOREIGN KEY (region_name) REFERENCES resources (id) ON DELETE CASCADE
);

CREATE TABLE cities
(
    city_id   INT PRIMARY KEY AUTO_INCREMENT,
    region    INT,
    city_name INT NOT NULL,
    longitude FLOAT,
    latitude  FLOAT,
    UNIQUE (region, city_name),
    FOREIGN KEY (region) REFERENCES regions (region_id) ON DELETE CASCADE,
    FOREIGN KEY (city_name) REFERENCES resources (id) ON DELETE CASCADE
);

CREATE TABLE destination
(
    id      INT PRIMARY KEY AUTO_INCREMENT,
    city    INT,
    adress  VARCHAR(30),
	FOREIGN KEY (city) REFERENCES cities (city_id) ON DELETE CASCADE
);

CREATE TABLE statuses
(
	id              INT PRIMARY KEY AUTO_INCREMENT,
    status_name     INT NOT NULL,
    FOREIGN KEY (status_name) REFERENCES resources (id) ON DELETE CASCADE
);

CREATE TABLE deliveries
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
	user_id     INT,
    adress      INT,
    cargo_type  INT,
    weight      INT,
    volume      INT,
    cost	    DOUBLE,
    creation_date  TIMESTAMP,
    FOREIGN KEY (user_id)	 REFERENCES users (id) ON DELETE CASCADE,
	FOREIGN KEY (adress)	 REFERENCES destination (id) ON DELETE CASCADE,
	FOREIGN KEY (cargo_type) REFERENCES cargo_types (id) ON DELETE CASCADE
);

CREATE TABLE delivery_status
(
    delivery_id INT,
    status_id INT,
    date_time TIMESTAMP,
    FOREIGN KEY (status_id) REFERENCES statuses (id) ON DELETE CASCADE,
    FOREIGN KEY (delivery_id) REFERENCES deliveries (id) ON DELETE CASCADE
);

insert into locales (locale_name) values ('en_EN');
insert into locales (locale_name) values ('ru_RU');
insert into locales (locale_name) values ('uk_UA');

INSERT INTO roles (role_name)
VALUES 
	('USER'),
    ('MANAGER'),
    ('ADMIN');
    
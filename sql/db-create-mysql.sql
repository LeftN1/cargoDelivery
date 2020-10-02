DROP TABLE IF EXISTS delivery_status CASCADE;
DROP TABLE IF EXISTS deliveries CASCADE;
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
    id          INT PRIMARY KEY AUTO_INCREMENT,
    locale_name VARCHAR(30)
);

CREATE TABLE resources
(
    id INT PRIMARY KEY AUTO_INCREMENT
);

CREATE TABLE translations
(
    resource_id INT         NOT NULL,
    locale_id   INT         NOT NULL,
    translation VARCHAR(30) NOT NULL,
    UNIQUE (resource_id, locale_id),
    FOREIGN KEY (locale_id) REFERENCES locales (id) ON DELETE CASCADE,
    FOREIGN KEY (resource_id) REFERENCES resources (id) ON DELETE CASCADE
);

CREATE TABLE roles
(
    id        INT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE users
(
    id    INT PRIMARY KEY AUTO_INCREMENT,
    login VARCHAR(20) UNIQUE NOT NULL,
    role  INT,
    pass  VARCHAR(30),
    FOREIGN KEY (role) REFERENCES roles (id) ON DELETE CASCADE
);

CREATE TABLE cargo_types
(
    id               INT PRIMARY KEY AUTO_INCREMENT,
    name_resource_id INT NOT NULL,
    FOREIGN KEY (name_resource_id) REFERENCES resources (id) ON DELETE CASCADE
);

CREATE TABLE countries
(
    id               INT PRIMARY KEY AUTO_INCREMENT,
    name_resource_id INT NOT NULL,
	UNIQUE(name_resource_id),
    FOREIGN KEY (name_resource_id) REFERENCES resources (id) ON DELETE CASCADE
);

CREATE TABLE regions
(
    id               INT PRIMARY KEY AUTO_INCREMENT,
    name_resource_id INT NOT NULL,
    country          INT,
	UNIQUE(name_resource_id),
    FOREIGN KEY (country) REFERENCES countries (id) ON DELETE CASCADE,
    FOREIGN KEY (name_resource_id) REFERENCES resources (id) ON DELETE CASCADE
);

CREATE TABLE cities
(
    id               INT PRIMARY KEY AUTO_INCREMENT,
    region           INT,
    name_resource_id INT NOT NULL,
    longitude        FLOAT,
    latitude         FLOAT,
    UNIQUE(name_resource_id),
    UNIQUE (region, name_resource_id),
    FOREIGN KEY (region) REFERENCES regions (id) ON DELETE CASCADE,
    FOREIGN KEY (name_resource_id) REFERENCES resources (id) ON DELETE CASCADE
);

CREATE TABLE statuses
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    name_resource_id INT NOT NULL,
    UNIQUE(name_resource_id),
    FOREIGN KEY (name_resource_id) REFERENCES resources (id) ON DELETE CASCADE
);

CREATE TABLE deliveries
(
    id            INT PRIMARY KEY AUTO_INCREMENT,
    user_id       INT,
    city_id       INT,
    adress        VARCHAR(20),
    cargo_type    INT,
    weight        INT,
    volume        INT,
    cost          DOUBLE,
    creation_date TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (city_id) REFERENCES cities (id) ON DELETE CASCADE,
    FOREIGN KEY (cargo_type) REFERENCES cargo_types (id) ON DELETE CASCADE
);

CREATE TABLE delivery_status
(
    delivery_id      INT,
    status_id        INT,
    date_time        TIMESTAMP,
    FOREIGN KEY (status_id) REFERENCES statuses (id) ON DELETE CASCADE,
    FOREIGN KEY (delivery_id) REFERENCES deliveries (id) ON DELETE CASCADE
);

insert into locales (locale_name)
values ('en_EN');
insert into locales (locale_name)
values ('ru_RU');
insert into locales (locale_name)
values ('uk_UA');

INSERT INTO roles (role_name)
VALUES ('USER'),
       ('MANAGER'),
       ('ADMIN');
    
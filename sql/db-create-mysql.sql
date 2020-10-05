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
    lang VARCHAR(2),
    country VARCHAR(2),
    UNIQUE (lang, country)
);

CREATE TABLE resources
(
    id INT PRIMARY KEY AUTO_INCREMENT    
);

CREATE TABLE translations
(
    resource_id INT         NOT NULL,
    locale_id   INT         NOT NULL,
    translation VARCHAR(50) NOT NULL,
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
    login VARCHAR(50) UNIQUE NOT NULL,
    role  INT,
    pass  VARCHAR(50),
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
    id            				INT PRIMARY KEY AUTO_INCREMENT,
    user_id       				INT,
    origin_city_id	  			INT,	
    destination_city_id       	INT,
    adress        				VARCHAR(100),
    cargo_type    				INT,
    weight        				INT,
    volume        				INT,
    cost          				DOUBLE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (origin_city_id) REFERENCES cities (id) ON DELETE CASCADE,
    FOREIGN KEY (destination_city_id) REFERENCES cities (id) ON DELETE CASCADE,
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

insert into locales (lang, country)
values ('en','US');

insert into locales (lang, country)
values ('ru','RU');

insert into locales (lang, country)
values ('uk','UA');


INSERT INTO roles (role_name)
VALUES ('USER'),
       ('MANAGER'),
       ('ADMIN');

INSERT INTO resources value (1);
INSERT INTO translations VALUES(1, 1, 'parcel');
INSERT INTO translations VALUES(1, 2, 'посылка');
INSERT INTO translations VALUES(1, 3, 'посилка');
INSERT INTO cargo_types VALUES (1, 1);

INSERT INTO resources value (2);
INSERT INTO translations VALUES(2, 1, 'documents');
INSERT INTO translations VALUES(2, 2, 'документы');
INSERT INTO translations VALUES(2, 3, 'документи');
INSERT INTO cargo_types VALUES (2, 2);

INSERT INTO resources value (3);
INSERT INTO translations VALUES(3, 1, 'wheels');
INSERT INTO translations VALUES(3, 2, 'шины');
INSERT INTO translations VALUES(3, 3, 'шини');
INSERT INTO cargo_types VALUES (3, 3);

INSERT INTO resources value (4);
INSERT INTO translations VALUES(4, 1, 'cargo');
INSERT INTO translations VALUES(4, 2, 'грузы');
INSERT INTO translations VALUES(4, 3, 'вантажі');
INSERT INTO cargo_types VALUES (4, 4);

INSERT INTO resources value (5);
INSERT INTO translations VALUES(5, 1, 'pallets');
INSERT INTO translations VALUES(5, 2, 'паллеты');
INSERT INTO translations VALUES(5, 3, 'палети');
INSERT INTO cargo_types VALUES (5, 5);

INSERT INTO resources value (6);
INSERT INTO translations VALUES(6, 1, 'new');
INSERT INTO translations VALUES(6, 2, 'новая');
INSERT INTO translations VALUES(6, 3, 'нова');
INSERT INTO statuses VALUES (1, 6);

INSERT INTO resources value (7);
INSERT INTO translations VALUES(7, 1, 'processed');
INSERT INTO translations VALUES(7, 2, 'обработано');
INSERT INTO translations VALUES(7, 3, 'опрацьовано');
INSERT INTO statuses VALUES (2, 7);

INSERT INTO resources value (8);
INSERT INTO translations VALUES(8, 1, 'paid');
INSERT INTO translations VALUES(8, 2, 'оплачено');
INSERT INTO translations VALUES(8, 3, 'оплачено');
INSERT INTO statuses VALUES (3, 8);

INSERT INTO resources value (9);
INSERT INTO translations VALUES(9, 1, 'shipped');
INSERT INTO translations VALUES(9, 2, 'отправлено');
INSERT INTO translations VALUES(9, 3, 'відправлено');
INSERT INTO statuses VALUES (4, 9);

INSERT INTO resources value (10);
INSERT INTO translations VALUES(10, 1, 'arrived');
INSERT INTO translations VALUES(10, 2, 'прибыло');
INSERT INTO translations VALUES(10, 3, 'прибуло');
INSERT INTO statuses VALUES (5, 10);

INSERT INTO resources value (11);
INSERT INTO translations VALUES(11, 1, 'recieved');
INSERT INTO translations VALUES(11, 2, 'получено');
INSERT INTO translations VALUES(11, 3, 'отримано');
INSERT INTO statuses VALUES (6, 11);

    
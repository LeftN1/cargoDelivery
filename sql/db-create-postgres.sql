DROP TABLE IF EXISTS roles CASCADE;
DROP TABLE IF EXISTS users CASCADE;

DROP TABLE IF EXISTS cargo_types CASCADE;
DROP TABLE IF EXISTS orders CASCADE;

DROP TABLE IF EXISTS countries CASCADE;
DROP TABLE IF EXISTS regions CASCADE;
DROP TABLE IF EXISTS cities CASCADE;
DROP TABLE IF EXISTS destination CASCADE;


CREATE TABLE roles(
	id SERIAL PRIMARY KEY,
    role_name VARCHAR(20) UNIQUE NOT NULL
    );

CREATE TABLE users(
	id SERIAL PRIMARY KEY,
    login VARCHAR(20) UNIQUE NOT NULL,
	role INT REFERENCES roles(id),
    pass VARCHAR(30)
    );
    
CREATE TABLE cargo_types(
	id SERIAL PRIMARY KEY,
    type_name VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE orders(
	id SERIAL PRIMARY KEY,
    cargo_type INT REFERENCES  cargo_types(id),
    weight INT,
    volume INT,
    date_time TIMESTAMP
);

CREATE TABLE  countries(
	id SERIAL PRIMARY KEY,
	country_name VARCHAR(30) UNIQUE NOT NULL
);

CREATE TABLE  regions(
	id SERIAL PRIMARY KEY,
	region VARCHAR(30) UNIQUE NOT NULL,
	country INT REFERENCES  countries(id)
);

CREATE TABLE cities(
	id SERIAL PRIMARY KEY,
    region INT REFERENCES  regions(id),
    city_name VARCHAR(30) NOT NULL,
    longitude FLOAT,
    latitude FLOAT,
    UNIQUE(region, city_name)
    );

CREATE TABLE destination(
	id SERIAL PRIMARY KEY,
    city INT REFERENCES cities(id),
    adress VARCHAR(30)
    );
	
    
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



CREATE TABLE  countries(
	id SERIAL PRIMARY KEY,
	country_name VARCHAR(30) UNIQUE NOT NULL
);

CREATE TABLE  regions(
	region_id SERIAL PRIMARY KEY,
	region_name VARCHAR(30) UNIQUE NOT NULL,
	country INT REFERENCES  countries(id)
);

CREATE TABLE cities(
	city_id SERIAL PRIMARY KEY,
    region INT REFERENCES  regions(region_id),
    city_name VARCHAR(30) NOT NULL,
    longitude FLOAT,
    latitude FLOAT,
    UNIQUE(region, city_name)
    );

CREATE TABLE destination(
	id SERIAL PRIMARY KEY,
    city INT REFERENCES cities(city_id),
    adress VARCHAR(30)
    );
	
CREATE TABLE orders(
	id SERIAL PRIMARY KEY,
	user_id INT REFERENCES users(id),
	adress INT REFERENCES destination(id),
    cargo_type INT REFERENCES  cargo_types(id),
    weight INT,
    volume INT,
    date_time TIMESTAMP
);
	
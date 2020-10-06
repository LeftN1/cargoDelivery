select * from translations where resource_id=282;

select * from cities where name_resource_id=3;

SELECT locale_id, translation FROM translations WHERE resource_id=3;

SELECT * FROM translations;

select * from resources;

delete from cities where id=1;

select * from cities;

select * from regions;

delete from regions where id=1;

delete from resources where id =3;

select id, region, name_resource_id, longitude, latitude, translation from cities 
join resources on name_resource_id=resources.id
join translations on resource_id=resources.id
where locale_id=2;

select cities.id, region, name_resource_id, longitude, latitude, lang, country, translation from cities 
join resources on name_resource_id=resources.id
join translations on resource_id=resources.id
join locales on locales.id=locale_id
ORDER BY cities.id;

select regions.id, regions.country, name_resource_id, lang, locales.country, translation from regions
join resources on name_resource_id=resources.id
join translations on resource_id=resources.id
join locales on locales.id=locale_id
ORDER BY regions.id;

select regions.id, translation, country from regions
join resources on name_resource_id=resources.id
join translations on resource_id=resources.id;


select distinct cities.id from cities;

select * from users;

select users.id, login, pass, role_name from users
join roles on role=roles.id;

SELECT * FROM cargo_types
join translations on name_resource_id = resource_id;

SELECT * FROM statuses
join translations on name_resource_id = resource_id;

SELECT * FROM statuses
join translations on name_resource_id = resource_id
where name_resource_id=7;

SELECT statuses.id, translation, lang, country FROM statuses
join translations on name_resource_id = resource_id
join locales on locales.id=locale_id
order by statuses.id;

SELECT cargo_types.id, translation, lang, country FROM cargo_types
join translations on name_resource_id = resource_id
join locales on locales.id=locale_id
order by cargo_types.id;

select * from delivery_status;

select status_id, date_time from delivery_status
where delivery_id=2;

insert into deliveries (user_id, city_id, adress, cargo_type, weight, volume, cost)
values (1, 1, 'skdjfhsdfh', 1, 10, 10, 22.2);

select * from deliveries;

select * from deliveries
join users on user_id=users.id;

select * from deliveries
where user_id=1;

delete from regions where id=2;


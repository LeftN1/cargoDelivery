#case 1
# unregistered user can view directions with distance from current city and costs
# can sort and filter information
SELECT city_name FROM cities; 

SELECT city_name, region_name 
FROM cities join regions ON cities.region = regions.region_id
WHERE region_name='Одесская область';

# case 2
# register new user
INSERT INTO users (login, role, pass) 
VALUE ('test', 1, 123);

# case 3
# localized city name

insert into translations (locale_id,default_name,translation) values (1, 'Одесса', 'Odessa');
insert into translations (locale_id,default_name,translation) values (3, 'Одесса', 'Одеса');

select city_name, longitude, latitude,translation from cities
left join translations on city_name=default_name
where locale_id=1 AND city_name='Одесса';

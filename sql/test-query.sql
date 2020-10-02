# ADD City 1
insert into resources value(default);
## return inserted number

##add city
insert into cities (name_resource_id, longitude, latitude)
values (1, 11, 11);

## add translations for resource
insert into translations values
(1, 1, 'Odessa'),
(1, 2, 'Одесса'),
(1, 3, 'Одеса');

# ADD City 2
insert into resources value(default);
## return inserted number

##add city
insert into cities (name_resource_id, longitude, latitude)
values (2, 22, 22);

## add translations for resource
insert into translations values
(2, 1, 'Kyiv'),
(2, 2, 'Киев'),
(2, 3, 'Київ');

# ADD City 3
insert into resources value(default);
## return inserted number

##add city
insert into cities (name_resource_id, longitude, latitude)
values (3, 33, 33);

## add translations for resource
insert into translations values
(3, 1, 'Kharkiv'),
(3, 2, 'Харьков'),
(3, 3, 'Харків');

## update translations for resource
update translations set
translation='.Харків' 
where resource_id=3 AND locale_id=3;

# Select city with locale
select longitude, latitude, translation as city, lang as locale from cities 
join translations on cities.name_resource_id=translations.resource_id
join locales on locale_id = locales.id
where locale_id = 1 and cities.id=2;

# Select all cities with locale
select longitude, latitude, translation as city, lang as locale from cities 
join translations on cities.name_resource_id=translations.resource_id
join locales on locale_id = locales.id
where locale_id = 1;


#getLocaleId()

SELECT id FROM locales WHERE locales.lang='ru';

# ----- get city by any name
	# get rosource id by transl
SELECT resource_id FROM translations
WHERE translation='Алупка'
LIMIT 1;

SELECT * FROM cities 
WHERE name_resource_id=3;

# --------------------------------------------------------------------------------
# register new user
INSERT INTO users (login, role, pass) 
VALUE ('test', 1, 123);

# --------------------------------------------------------------------------------

select * from translations where resource_id=3;

select * from cities where name_resource_id=1;
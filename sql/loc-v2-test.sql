
# ADD City 1
insert into resources value(default);
## return inserted number

##add city
insert into cities (city_name, longitude, latitude)
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
insert into cities (city_name, longitude, latitude)
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
insert into cities (city_name, longitude, latitude)
values (3, 33, 33);

## add translations for resource
insert into translations values
(3, 1, 'Kharkiv'),
(3, 2, 'Харьков'),
(3, 3, 'Харків');

# Select city with locale
select longitude, latitude, translation as city, locale_name as locale from cities 
join translations on city_name=translations.resource_id
join locales on locale_id = locales.id
where locale_id = 3 and city_id=2;

# Select all cities with locale
select longitude, latitude, translation as city, locale_name as locale from cities 
join translations on city_name=translations.resource_id
join locales on locale_id = locales.id
where locale_id = 2;

package com.voroniuk.delivery.dao;

import com.voroniuk.delivery.entity.City;
import com.voroniuk.delivery.entity.Country;
import com.voroniuk.delivery.entity.Region;
import com.voroniuk.delivery.entity.User;

import java.util.List;

public interface DBManager {

    void addUser(User user);

    void addCountry(Country country);

    void addRegion(Region region);

    void addRegions(List<Region> regions);

    void addCity(City city);

    void addCities(List<City> cities);

    List<User> findAllUsers();

    List<City> findAllCities();

    City getCity(String name);

    double findDistance(City cityA, City cityB);

}

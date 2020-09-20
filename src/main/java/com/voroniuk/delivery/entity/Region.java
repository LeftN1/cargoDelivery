package com.voroniuk.delivery.entity;

import java.util.LinkedList;
import java.util.List;

public class Region {
    private int id;
    private Country country;
    private String name;
    private List<City> cities = new LinkedList<>();

    public Region() {

    }

    public Region(String name) {
        this.name = name;
    }

    public Region(Country country, String name) {
        this.country = country;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public void addCity(City city){
        cities.add(city);
    }

}

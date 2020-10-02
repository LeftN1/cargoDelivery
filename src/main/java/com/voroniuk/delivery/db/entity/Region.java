package com.voroniuk.delivery.db.entity;

import java.util.*;

public class Region {
    private int id;
    private Country country;
    private int nameResourceId;
    private Map<Locale, String> name;
    private List<City> cities;

    public Region() {
        name = new HashMap<>();
        cities = new LinkedList<>();
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


    public void addName(Locale locale, String s){
        name.put(locale, s);
    }

    public String getName(Locale locale) {

        if(!name.containsKey(locale)){
            locale = Locale.getDefault();
        }

        return name.get(locale);
    }

    public Map<Locale, String> getNames() {
        return name;
    }



    public void setNames(Map<Locale, String> name) {
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

    public int getNameResourceId() {
        return nameResourceId;
    }

    public void setNameResourceId(int nameResourceId) {
        this.nameResourceId = nameResourceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Region)) return false;
        Region region = (Region) o;
        return name.equals(region.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

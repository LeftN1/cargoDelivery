package com.voroniuk.delivery.db.entity;

import java.util.*;

public class City {
    private int id;
    private Region region;
    private Map<Locale, String> name;
    private double longitude;
    private double latitude;

    public City() {
        name= new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public String getName(Locale locale) {
        if(name.isEmpty()){
            return "Unknown";
        }

        if(!name.containsKey(locale)){
            locale = Locale.getDefault();
        }

        if(!name.containsKey(Locale.getDefault())){
            locale = name.keySet().iterator().next();
        }

        return name.get(locale);
    }

    public Map<Locale, String> getNames() {
        return name;
    }

    public void setName(Map<Locale, String> name) {
        this.name = name;
    }

    public void addName(Locale locale, String s){
        name.put(locale, s);
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City)) return false;
        City city = (City) o;
        return region.equals(city.region) &&
                name.equals(city.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, name);
    }

}

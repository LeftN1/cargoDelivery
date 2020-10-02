package com.voroniuk.delivery.db.entity;

import java.util.*;

public class City {
    private int id;
    private Region region;
    private int nameResourceId;
    private Map<Locale, String> names;
    private double longitude;
    private double latitude;

    public City() {
        names = new HashMap<>();
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
        if (names.isEmpty()) {
            return "Unknown";
        }

        if (!names.containsKey(locale)) {
            locale = Locale.getDefault();
        }

        if (!names.containsKey(Locale.getDefault())) {
            locale = names.keySet().iterator().next();
        }

        return names.get(locale);
    }

    public Map<Locale, String> getNames() {
        return names;
    }

    public void setNames(Map<Locale, String> names) {
        this.names = names;
    }

    public void addName(Locale locale, String s) {
        names.put(locale, s);
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

    public int getNameResourceId() {
        return nameResourceId;
    }

    public void setNameResourceId(int nameResourceId) {
        this.nameResourceId = nameResourceId;
    }

    @Override
    public String toString() {
        return "City{" + getName(Locale.getDefault()) + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City)) return false;
        City city = (City) o;
        return region.equals(city.region) &&
                names.equals(city.names);
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, names);
    }

}

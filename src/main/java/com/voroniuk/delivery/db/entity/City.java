package com.voroniuk.delivery.db.entity;

import java.util.List;
import java.util.Objects;

public class City {
    private int id;
    private Region region;
    private String name;
    private double longitude;
    private double latitude;
    private List<Destination> destinations;

    public City() {
    }

    public City(Region region, String cityName, double longitude, double latitude) {
        this.region = region;
        this.name = cityName;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public City(int id, Region region, String name, double longitude, double latitude) {
        this.id = id;
        this.region = region;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    public String toString() {
        return "City{" +
                "name='" + name + '\'' +
                '}';
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

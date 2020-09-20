package com.voroniuk.delivery.entity;

public class Destination {

    private int id;
    private City city;
    private String adress;

    private Destination() {
    }

    public Destination(City city, String adress) {
        this.city = city;
        this.adress = adress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }
}

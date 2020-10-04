package com.voroniuk.delivery.db.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Delivery implements Serializable {
    private int id;
    private User user;
    private City city;
    private String adress;
    private CargoType type;
    private List<DeliveryStatus> statusList;
    private int weight; // kg
    private int volume; //decimeter ^ 3
    private double cost;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public CargoType getType() {
        return type;
    }

    public void setType(CargoType type) {
        this.type = type;
    }

    public List<DeliveryStatus> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<DeliveryStatus> statusList) {
        this.statusList = statusList;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}

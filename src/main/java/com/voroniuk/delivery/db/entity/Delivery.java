package com.voroniuk.delivery.db.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Delivery implements Serializable {
    private int id;
    private User user;
    private City origin;
    private City destination;
    private String adress;
    private CargoType type;
    private Map<DeliveryStatus, Date> statusMap;
    private int weight; // kg
    private int volume; //decimeter ^ 3
    private int cost;

    public Delivery() {
        statusMap = new HashMap<>();
    }

    public void addStatus(DeliveryStatus status, Date date) {
        statusMap.put(status, date);
    }

    public DeliveryStatus getLastStatus() {
        long last = 0;
        DeliveryStatus status = DeliveryStatus.NEW;

        for (Map.Entry<DeliveryStatus, Date> entry : statusMap.entrySet()) {
            if (entry.getValue().getTime() > last) {
                status = entry.getKey();
                last = entry.getValue().getTime();
            }

        }
        return status;
    }

    public String getLastDateString() {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        return format.format(statusMap.get(getLastStatus()));
    }

    public Date getLastDate() {
        return statusMap.get(getLastStatus());
    }


    public Date getStatusDate(DeliveryStatus status) {
        if (getLastStatus().getId() >= status.getId()) {
            return statusMap.get(status);
        } else {
            return null;
        }
    }

    public String getStatusDateString(DeliveryStatus status) {

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        if (getLastStatus().getId() >= status.getId()) {
            return format.format(getStatusDate(status));
        } else {
            return null;
        }
    }

    public Map<DeliveryStatus, Date> getStatusMap() {
        return statusMap;
    }

    public void setStatusMap(Map<DeliveryStatus, Date> statusMap) {
        this.statusMap = statusMap;
    }

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

    public String getAdress() {
        return adress;
    }

    public void setAddress(String adress) {
        this.adress = adress;
    }

    public CargoType getType() {
        return type;
    }

    public void setType(CargoType type) {
        this.type = type;
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

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public City getOrigin() {
        return origin;
    }

    public void setOrigin(City origin) {
        this.origin = origin;
    }

    public City getDestination() {
        return destination;
    }

    public void setDestination(City destination) {
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "id=" + id +
                ", user=" + user +
                ", origin=" + origin +
                ", destination=" + destination +
                ", adress='" + adress + '\'' +
                ", type=" + type +
                ", statusMap=" + statusMap +
                ", weight=" + weight +
                ", volume=" + volume +
                ", cost=" + cost +
                '}';
    }
}

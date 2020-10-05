package com.voroniuk.delivery.db.entity;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum DeliveryStatus {
    NEW (1),
    PROCESSED (2),
    PAID (3),
    SHIPPED (4),
    ARRIVED (5),
    RECIEVED (6);

    private int id;
    private Map<Locale, String> names;


    DeliveryStatus(int id) {
        names = new HashMap<>();
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName(Locale locale) {
        if (names.isEmpty()) {
            return name();
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

    public static DeliveryStatus getStatusById(int id){
        for (DeliveryStatus ds : DeliveryStatus.values()){
            if(ds.getId() == id){
                return ds;
            }
        }
        return DeliveryStatus.NEW;
    }

}

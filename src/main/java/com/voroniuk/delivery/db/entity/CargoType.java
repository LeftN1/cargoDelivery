package com.voroniuk.delivery.db.entity;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum  CargoType {

    PARCEL(1),
    DOCUMENT(2),
    WHEEL(3),
    CARGO(4),
    PALLET(5);

    private int id;
    private Map<Locale, String> names;

    CargoType(int i) {
        names = new HashMap<>();
        id = i;
    }

    public int getId() {
        return id;
    }

    public static CargoType getTypeById(int id){
        for (CargoType ct : CargoType.values()){
            if(ct.getId() == id){
                return ct;
            }
        }
        return CargoType.PARCEL;
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
}

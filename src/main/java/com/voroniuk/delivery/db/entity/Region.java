package com.voroniuk.delivery.db.entity;

import java.util.*;

public class Region {
    private int id;
    private int countryId;
    private int nameResourceId;
    private Map<Locale, String> names;


    public Region() {
        names = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public void addName(Locale locale, String s){
        names.put(locale, s);
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



    public void setNames(Map<Locale, String> name) {
        this.names = name;
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
        return names.equals(region.names);
    }

    @Override
    public int hashCode() {
        return Objects.hash(names);
    }

    @Override
    public String toString() {
        return getName(Locale.getDefault());
    }
}

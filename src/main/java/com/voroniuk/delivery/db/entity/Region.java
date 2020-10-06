package com.voroniuk.delivery.db.entity;

import java.util.*;

public class Region {
    private int id;
    private int countryId;
    private int nameResourceId;
    private Map<Locale, String> name;


    public Region() {
        name = new HashMap<>();
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

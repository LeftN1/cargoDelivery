package com.voroniuk.delivery.db.entity;

import java.util.*;

public class Country {
    private int id;
    private Map<Locale, String> name;

    private List<Region> regions;
    public Country(){
        name = new HashMap<>();
        regions = new LinkedList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void setName(Map<Locale, String> name) {
        this.name = name;
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

    public void addName(Locale locale, String s){
        name.put(locale, s);
    }

    public List<Region> getRegions() {
        return regions;
    }

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }

    public void addRegion(Region region){
        regions.add(region);
    }
}

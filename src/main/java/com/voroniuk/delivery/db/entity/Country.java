package com.voroniuk.delivery.db.entity;

import java.util.LinkedList;
import java.util.List;

public class Country {
    private int id;
    private String name;

    private List<Region> regions = new LinkedList<>();

    public Country(){
    }

    public Country(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

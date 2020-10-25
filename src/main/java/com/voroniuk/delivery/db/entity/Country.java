package com.voroniuk.delivery.db.entity;

import java.util.*;

/**
 * Country entity
 *
 * @author M. Voroniuk
 */
public class Country {
    private int id;
    private int nameResourceId;
    private Map<Locale, String> name;

    public Country() {
        name = new HashMap<>();
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

    /**
     * Returns name of the country in given language.
     * Names are loaded from database using ResourceDAO.
     * @param locale
     * @return country name
     */
    public String getName(Locale locale) {

        if (!name.containsKey(locale)) {
            locale = Locale.getDefault();
        }

        return name.get(locale);
    }

    public Map<Locale, String> getNames() {
        return name;
    }

    public void addName(Locale locale, String s) {
        name.put(locale, s);
    }

    public int getNameResourceId() {
        return nameResourceId;
    }

    public void setNameResourceId(int nameResourceId) {
        this.nameResourceId = nameResourceId;
    }
}

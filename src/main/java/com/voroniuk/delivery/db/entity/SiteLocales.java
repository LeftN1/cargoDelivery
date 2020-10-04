package com.voroniuk.delivery.db.entity;

import java.util.Locale;

public enum SiteLocales {
    EN(new Locale("en", "US"), "en"),
    RU(new Locale("ru", "RU"), "ru"),
    UA(new Locale("uk", "UA"), "ua");

    private Locale locale;
    private String name;

    SiteLocales(Locale l, String s) {
        locale = l;
        name = s;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getName() {
        return name;
    }



}

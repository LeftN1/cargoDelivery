package com.voroniuk.delivery.db.entity;

import java.util.Locale;

public enum SiteLocales {
    EN(new Locale("en", "US")),
    RU(new Locale("ru", "RU")),
    UA(new Locale("uk", "UA"));

    private Locale locale;

    SiteLocales(Locale l){
        locale = l;
    }

    public Locale getLocale() {
        return locale;
    }
}

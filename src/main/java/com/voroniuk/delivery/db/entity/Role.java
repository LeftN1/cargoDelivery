package com.voroniuk.delivery.db.entity;

public enum Role {
    USER,
    AUTORIZED_USER,
    MANAGER;

    public String value() {
        return name();
    }

}

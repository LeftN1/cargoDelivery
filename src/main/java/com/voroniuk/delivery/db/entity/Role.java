package com.voroniuk.delivery.db.entity;

public enum Role {
    USER,
    MANAGER;

    public String value() {
        return name();
    }

}

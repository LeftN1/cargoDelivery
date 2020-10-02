package com.voroniuk.delivery.db.entity;

public enum Role {
    USER,
    MANAGER,
    ADMIN;

    public String value() {
        return name();
    }

}

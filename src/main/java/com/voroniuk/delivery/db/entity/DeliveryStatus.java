package com.voroniuk.delivery.db.entity;

public enum DeliveryStatus {
    NEW (1),
    PROCESSED (2),
    PAID (3),
    SHIPPED (4),
    ARRIVED (5),
    RECIEVED (6);

    private int id;

    DeliveryStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public DeliveryStatus getStatusById(int id){
        for (DeliveryStatus ds : DeliveryStatus.values()){
            if(ds.getId() == id){
                return ds;
            }
        }
        return DeliveryStatus.NEW;
    }

}

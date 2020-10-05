package com.voroniuk.delivery.db.entity;

public enum  CargoType {

    PARCEL(1),
    DOCUMENT(2),
    WHEEL(3),
    CARGO(4),
    PALLET(5);

    private int id;

    CargoType(int i) {
        id = i;
    }

    public int getId() {
        return id;
    }

    public static CargoType getTypeById(int id){
        for (CargoType ct : CargoType.values()){
            if(ct.getId() == id){
                return ct;
            }
        }
        return CargoType.PARCEL;
    }



}

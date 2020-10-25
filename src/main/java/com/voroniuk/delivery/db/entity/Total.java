package com.voroniuk.delivery.db.entity;

/**
 * Report total entity. Used to hold totals in reports.
 *
 * @author M. Voroniuk
 */

public class Total {

    int totalWeight;
    int totalVolume;
    int totalCost;

    public Total() {
    }

    public int getTotalWeight() {
        return totalWeight;
    }

    public int getTotalVolume() {
        return totalVolume;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void addWeight(int weight) {
        totalWeight += weight;
    }

    public void addVolume(int volume) {
        totalVolume += volume;
    }

    public void addCost(int cost) {
        totalCost += cost;
    }

}

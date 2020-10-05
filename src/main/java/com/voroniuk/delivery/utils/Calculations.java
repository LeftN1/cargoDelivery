package com.voroniuk.delivery.utils;

import com.voroniuk.delivery.db.dao.CityDAO;
import com.voroniuk.delivery.db.entity.City;

public class Calculations {
    public static int getCost(City current, City dest, int weight, int volume) {
        CityDAO cityDAO = new CityDAO();
        double distance = cityDAO.findDistance(current, dest);
        int cost = (int) (10 + distance / 100 * 3 + weight * 1 + volume * 1);
        return cost;
    }

    public static int getVolume(int length, int width, int height) {
        int volume = (int) (length * width * height / 1000);
        return volume > 1 ? volume : 1;
    }
}

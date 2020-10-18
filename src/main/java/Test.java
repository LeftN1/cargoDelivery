import com.voroniuk.delivery.db.dao.*;
import com.voroniuk.delivery.db.entity.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class Test {
    public static void main(String[] args) {

        CityDAO cityDAO = new CityDAO();
        UserDAO userDAO = new UserDAO();
        ResourceDAO resourceDAO = new ResourceDAO();
        OrderDAO orderDAO = new OrderDAO();

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

        City origin;
        City destination;
        String address = "address";
        User user;
        Delivery delivery;

        origin = cityDAO.findCityById(1);
        destination = cityDAO.findCityById(2);
        user = userDAO.findUserById(1);

        delivery = new Delivery();
        delivery.setUser(user);
        delivery.setOrigin(origin);
        delivery.setDestination(destination);
        delivery.setAddress(address);
        delivery.setType(CargoType.CARGO);
        delivery.setWeight(1);
        delivery.setVolume(1);
        delivery.setCost(1);
        orderDAO.saveDelivery(delivery);

        orderDAO.changeCurrentStatus(delivery, DeliveryStatus.PROCESSED, new Date());

        System.out.println(delivery.getStatusMap());

        System.out.println(delivery.getLastStatus());

        Delivery founded = orderDAO.findDeliveryById(delivery.getId());

        System.out.println(founded.getStatusMap());
        System.out.println(founded.getLastStatus());

    }
}

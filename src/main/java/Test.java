import com.voroniuk.delivery.db.dao.*;
import com.voroniuk.delivery.db.entity.Delivery;
import com.voroniuk.delivery.db.entity.DeliveryStatus;

import java.util.Date;


public class Test {
    public static void main(String[] args) {

        CityDAO cityDAO = new CityDAO();
        UserDAO userDAO = new UserDAO();
        ResourceDAO resourceDAO = new ResourceDAO();
        OrderDAO orderDAO = new OrderDAO();

//        Delivery d = orderDAO.findDeliveriesByStatus(DeliveryStatus.NEW).get(0);
//        orderDAO.addStatus(d, DeliveryStatus.PROCESSED, new Date());

        System.out.println(DeliveryStatus.NEW == DeliveryStatus.NEW);

    }
}

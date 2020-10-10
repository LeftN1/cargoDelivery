import com.voroniuk.delivery.db.dao.*;
import com.voroniuk.delivery.db.entity.*;
import org.apache.commons.codec.digest.DigestUtils;

import java.text.SimpleDateFormat;
import java.util.*;


public class Test {
    public static void main(String[] args) {

        CityDAO cityDAO = new CityDAO();
        UserDAO userDAO = new UserDAO();
        ResourceDAO resourceDAO = new ResourceDAO();
        OrderDAO orderDAO = new OrderDAO();

//        Delivery d = orderDAO.findDeliveriesByStatus(DeliveryStatus.PROCESSED).get(0);
//        orderDAO.addStatus(d, DeliveryStatus.ARRIVED, new Date());


    }
}

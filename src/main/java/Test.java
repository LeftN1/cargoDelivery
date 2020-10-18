import com.voroniuk.delivery.db.dao.*;
import com.voroniuk.delivery.db.entity.Delivery;
import com.voroniuk.delivery.db.entity.DeliveryStatus;

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

        int count = orderDAO.countReportDeliveries(DeliveryStatus.RECIEVED, 0, 0,0, new Date(0), new Date(0));
        System.out.println("count: " + count);

        List<Delivery> deliveries = orderDAO.reportDeliveriesByStatusAndCityIdAndDate(DeliveryStatus.RECIEVED, 0, 0, new Date(0), new Date(0), 0, 10);

        System.out.println(deliveries.size());

        for (Delivery delivery : deliveries){
            System.out.println("id: " + delivery.getId() + "   date: "+delivery.getStatusDateString(DeliveryStatus.NEW));
        }


    }
}

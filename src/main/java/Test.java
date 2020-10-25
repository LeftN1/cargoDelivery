import com.voroniuk.delivery.db.dao.*;
import com.voroniuk.delivery.db.entity.*;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class Test {
    public static void main(String[] args) throws ParseException {

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

        List<Delivery> deliveries = orderDAO.reportDeliveriesByStatusAndCityIdAndDate(DeliveryStatus.NEW, 0, 0, new Date(0), new Date(0), 0, 100);

        for (Delivery d : deliveries) {
            String sDate = d.getStatusDateString(DeliveryStatus.NEW);
            Date date = format.parse(sDate);
            System.out.println(sDate + "   " + date);
        }

    }
}

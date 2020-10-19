import com.voroniuk.delivery.db.dao.*;
import com.voroniuk.delivery.db.entity.*;

import java.text.ParseException;
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
        user = userDAO.findUserById(2);

        Date dMin = new Date(0);
        Date dMax = new Date(0);

        try {
            dMin = format.parse("10.09.2020");
            dMax = format.parse("10.10.2020");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long lMin = dMin.getTime();
        long lMax = dMax.getTime();
        long delta = lMax - lMin;


        for (int i = 0; i < 10; i++) {

            long rand = (long) (Math.random() * delta + lMin);

            System.out.println(format.format(new Date(rand)));
        }


    }
}

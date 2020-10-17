import com.voroniuk.delivery.db.dao.*;
import com.voroniuk.delivery.db.entity.Delivery;
import com.voroniuk.delivery.db.entity.DeliveryStatus;
import com.voroniuk.delivery.db.entity.Role;
import com.voroniuk.delivery.db.entity.User;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class Test {
    public static void main(String[] args) {

        CityDAO cityDAO = new CityDAO();
        UserDAO userDAO = new UserDAO();
        ResourceDAO resourceDAO = new ResourceDAO();
        OrderDAO orderDAO = new OrderDAO();

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

        User user = userDAO.findUserByLogin("bbb");
        System.out.println(user.getRole()== Role.MANAGER);

    }
}

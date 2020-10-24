import com.voroniuk.delivery.db.dao.*;
import com.voroniuk.delivery.db.entity.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

        String name = "hello.txt";

        File file = new File("src\\main\\webapp\\reports\\"+name);
        file.delete();
    }
}

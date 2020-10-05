import com.voroniuk.delivery.db.dao.CityDAO;
import com.voroniuk.delivery.db.dao.ResourceDAO;
import com.voroniuk.delivery.db.dao.UserDAO;
import com.voroniuk.delivery.db.entity.*;
import org.apache.commons.codec.digest.DigestUtils;

import java.text.SimpleDateFormat;
import java.util.*;


public class Test {
    public static void main(String[] args) {

        CityDAO cityDAO = new CityDAO();
        UserDAO userDAO = new UserDAO();
        ResourceDAO resourceDAO = new ResourceDAO();

        Date date = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy H:m:s");

        System.out.println(formatter.format(date));
        System.out.println(date.getTime());

    }
}

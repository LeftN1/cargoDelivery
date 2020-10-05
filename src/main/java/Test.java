import com.voroniuk.delivery.db.dao.CityDAO;
import com.voroniuk.delivery.db.dao.ResourceDAO;
import com.voroniuk.delivery.db.dao.UserDAO;
import com.voroniuk.delivery.db.entity.*;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;
import java.util.Locale;


public class Test {
    public static void main(String[] args) {

        CityDAO cityDAO = new CityDAO();
        UserDAO userDAO = new UserDAO();
        ResourceDAO resourceDAO = new ResourceDAO();



        resourceDAO.loadCargoTypes();
        resourceDAO.loadStatuses();

        System.out.println(DeliveryStatus.NEW.getName(SiteLocale.RU.getLocale()));
        System.out.println(CargoType.CARGO.getName(SiteLocale.UA.getLocale()));


    }
}

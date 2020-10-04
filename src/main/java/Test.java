import com.voroniuk.delivery.db.dao.CityDAO;
import com.voroniuk.delivery.db.dao.UserDAO;
import com.voroniuk.delivery.db.entity.City;
import com.voroniuk.delivery.db.entity.SiteLocales;
import com.voroniuk.delivery.db.entity.User;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;


public class Test {
    public static void main(String[] args) {

        CityDAO cityDAO = new CityDAO();
        UserDAO userDAO = new UserDAO();

        List<City> cities = cityDAO.findAllCities();

        System.out.println(cities);


        System.out.println(cities.get(0).getName(SiteLocales.UA.getLocale()));

        System.out.println(SiteLocales.EN);

        User user = userDAO.findUserByLogin("333");
        System.out.println(user.getRole().name());

        String hash = DigestUtils.md5Hex("hello");

        System.out.println(hash);

        System.out.println(hash.equals(DigestUtils.md5Hex("hello")));

    }
}

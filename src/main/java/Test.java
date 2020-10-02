import com.voroniuk.delivery.db.dao.CityDAO;
import com.voroniuk.delivery.db.entity.City;

import java.util.Locale;

public class Test {
    public static void main(String[] args) {

        CityDAO cityDAO = new CityDAO();

        City od = cityDAO.findCityByName("Одесса");

        Locale locale = new Locale("ru", "RU");

        System.out.println(od.getNames());
        System.out.println(od.getName(locale));
        System.out.println(od.getLongitude());

    }
}

import com.voroniuk.delivery.db.dao.CityDAO;
import com.voroniuk.delivery.db.entity.City;
import com.voroniuk.delivery.db.entity.SiteLocales;

import java.util.List;


public class Test {
    public static void main(String[] args) {

        CityDAO cityDAO = new CityDAO();

        List<City> cities = cityDAO.findAllCities();

        System.out.println(cities);


        System.out.println(cities.get(0).getName(SiteLocales.UA.getLocale()));

    }
}

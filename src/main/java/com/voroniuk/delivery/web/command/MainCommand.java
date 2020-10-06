package com.voroniuk.delivery.web.command;

import com.voroniuk.delivery.Path;
import com.voroniuk.delivery.db.dao.CityDAO;
import com.voroniuk.delivery.db.entity.City;
import com.voroniuk.delivery.db.entity.Region;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class MainCommand extends Command {

    private static final Logger LOG = Logger.getLogger(MainCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        LOG.debug("Main command starts");

        CityDAO cityDAO = new CityDAO();
        City currentCity = (City) req.getSession().getAttribute("currentCity");

        String cityInp = req.getParameter("cityInp");

        String region = req.getParameter("region");
        int regionId = 0;

        if(region!=null){
            regionId = Integer.parseInt(region);
        }


        if (cityInp == null) {
            currentCity = cityDAO.findCityByName("Odessa");
            LOG.trace("Initializing current city as: " + currentCity);
        } else {
            City finded = cityDAO.findCityByName(cityInp);
            if (finded != null) {
                currentCity = finded;
                LOG.trace("Change current city to: " + currentCity);
            }
        }

        LOG.debug("Save currentCity to session: " + currentCity);
        req.getSession().setAttribute("currentCity", currentCity);


        if (req.getSession().getAttribute("locale") == null) {
            req.getSession().setAttribute("locale", Locale.getDefault());
        }

        Map<City, String> distances = new HashMap<>();

        List<City> cityList = new LinkedList<>();

        cityList = (LinkedList<City>) req.getServletContext().getAttribute("cities");

        if (regionId != 0) {
            cityList = getCitiesByRegion(cityList, regionId);
        }

        for (City city : cityList) {
            String dist = String.format("%.2f", cityDAO.findDistance(currentCity, city));
            distances.put(city, dist);
        }

        req.setAttribute("distances", distances);

        req.setAttribute("cityList", cityList);

        req.setAttribute("regionId" , regionId);

        String forward = Path.PAGE__MAIN;

        LOG.debug("Main command ends forward to " + forward);
        return forward;
    }

    List<City> getCitiesByRegion(List<City> cityList ,int id){
        return cityList.stream().filter(i -> i.getRegionId()==id).collect(Collectors.<City>toList());
    }

}

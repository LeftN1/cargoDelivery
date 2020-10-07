package com.voroniuk.delivery.web.command;

import com.voroniuk.delivery.Path;
import com.voroniuk.delivery.db.dao.CityDAO;
import com.voroniuk.delivery.db.entity.City;
import com.voroniuk.delivery.db.entity.Region;
import com.voroniuk.delivery.utils.Calculations;
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
        Integer savedRegionId = (Integer) req.getSession().getAttribute("regionId");

        String cityInp = req.getParameter("cityInp");
        String region = req.getParameter("region");

        int weight;
        int length;
        int width;
        int height;
        int voulume;

        try {
            weight = Integer.parseInt(req.getParameter("weight"));
            length = Integer.parseInt(req.getParameter("length"));
            width = Integer.parseInt(req.getParameter("width"));
            height = Integer.parseInt(req.getParameter("height"));
        } catch (NumberFormatException e) {
            weight = 1;
            length = 10;
            width = 10;
            height = 10;
        }
        voulume = Calculations.getVolume(length,width, height);


        int regionId = 0;


        if (region != null) {
            regionId = Integer.parseInt(region);
        } else if (savedRegionId != null) {
            regionId = savedRegionId;
        }


        if (cityInp != null) {
            City finded = cityDAO.findCityByName(cityInp);
            if (finded != null) {
                currentCity = finded;
                LOG.trace("Change current city to: " + currentCity);
            }
        }

        if (currentCity == null) {
            currentCity = cityDAO.findCityByName("Odessa");
            LOG.trace("Initializing current city as: " + currentCity);
        }


        LOG.debug("Save currentCity to session: " + currentCity);
        req.getSession().setAttribute("currentCity", currentCity);


        if (req.getSession().getAttribute("locale") == null) {
            req.getSession().setAttribute("locale", Locale.getDefault());
        }

        Map<City, String> distances = new HashMap<>();
        Map<City, Integer> costs = new HashMap<>();

        List<City> cityList = new LinkedList<>();

        cityList = (LinkedList<City>) req.getServletContext().getAttribute("cities");

        if (regionId != 0) {
            cityList = getCitiesByRegion(cityList, regionId);
        }

        for (City city : cityList) {
            String dist = String.format("%.2f", cityDAO.findDistance(currentCity, city));
            distances.put(city, dist);
            costs.put(city, Calculations.getCost(currentCity, city, weight, voulume));
        }

        req.setAttribute("distances", distances);
        req.setAttribute("costs", costs);

        req.setAttribute("cityList", cityList);

        req.setAttribute("weight", weight);
        req.setAttribute("length", length);
        req.setAttribute("width", width);
        req.setAttribute("height", height);
        req.setAttribute("volume", voulume);


        req.getSession().setAttribute("regionId", regionId);

        String forward = Path.PAGE__MAIN;

        LOG.debug("Main command ends forward to " + forward);
        return forward;
    }

    List<City> getCitiesByRegion(List<City> cityList, int id) {
        return cityList.stream().filter(i -> i.getRegionId() == id).collect(Collectors.<City>toList());
    }

}

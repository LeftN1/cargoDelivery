package com.voroniuk.delivery.web.command;

import com.voroniuk.delivery.Path;
import com.voroniuk.delivery.db.dao.CityDAO;
import com.voroniuk.delivery.db.entity.City;
import com.voroniuk.delivery.db.entity.Region;
import com.voroniuk.delivery.utils.Calculations;
import com.voroniuk.delivery.utils.Utils;
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

        String sortReq = req.getParameter("sort");
        String orderReq = req.getParameter("order");

        if (sortReq != null && orderReq != null) {
            req.getSession().setAttribute("sort", sortReq);
            req.getSession().setAttribute("order", orderReq);
        }

        String sort = (String) req.getSession().getAttribute("sort");
        String order = (String) req.getSession().getAttribute("order");


        String sWeight = req.getParameter("weight");
        String sLength = req.getParameter("length");
        String sWidth = req.getParameter("width");
        String sHeight = req.getParameter("height");

        int weight;
        int length;
        int width;
        int height;
        int voulume;

        try {
            weight = sWeight != null ? Integer.parseInt(sWeight) : ((int) req.getSession().getAttribute("weight"));
            length = sLength != null ? Integer.parseInt(sLength) : ((int) req.getSession().getAttribute("length"));
            width = sWidth != null ? Integer.parseInt(sWidth) : ((int) req.getSession().getAttribute("width"));
            height = sHeight != null ? Integer.parseInt(sHeight) : ((int) req.getSession().getAttribute("height"));
        } catch (NumberFormatException | NullPointerException e) {
            weight = 1;
            length = 10;
            width = 10;
            height = 10;
        }
        voulume = Calculations.getVolume(length, width, height);


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

        Locale locale = (Locale) req.getSession().getAttribute("locale");

        if (locale == null) {
            locale = Locale.getDefault();
            req.getSession().setAttribute("locale", locale);
        }


        Map<City, Integer> distances = new HashMap<>();
        Map<City, Integer> costs = new HashMap<>();

        List<City> cityList;

        cityList = (LinkedList<City>) req.getServletContext().getAttribute("cities");


        if (regionId != 0) {
            cityList = getCitiesByRegion(cityList, regionId);
        }

        for (City city : cityList) {
            //String dist = String.format("%.2f", cityDAO.findDistance(currentCity, city));
            int dist = (int) cityDAO.findDistance(currentCity, city);
            distances.put(city, dist);
            costs.put(city, Calculations.getCost(currentCity, city, weight, voulume));
        }

        //SORTING
        cityList = sortCities(cityList, distances, sort, order, locale);

        int pageNo;
        int pageSize = 10;
        int totalPages = (int) Math.ceil((double) cityList.size() / pageSize);

        pageNo = Utils.getPageNoFromRequest(req, "page", totalPages);

        cityList = getCititesLimit(cityList, (pageNo - 1) * pageSize, pageSize);


        req.setAttribute("pageNo", pageNo);
        req.setAttribute("totalPages", totalPages);

        req.setAttribute("distances", distances);
        req.setAttribute("costs", costs);

        req.setAttribute("cityList", cityList);

        req.getSession().setAttribute("weight", weight);
        req.getSession().setAttribute("length", length);
        req.getSession().setAttribute("width", width);
        req.getSession().setAttribute("height", height);
        req.getSession().setAttribute("volume", voulume);


        req.getSession().setAttribute("regionId", regionId);

        String forward = Path.PAGE__MAIN;

        LOG.debug("Main command ends forward to " + forward);
        return forward;
    }

    List<City> getCitiesByRegion(List<City> cityList, int id) {
        return cityList.stream().filter(i -> i.getRegionId() == id).collect(Collectors.<City>toList());
    }

    List<City> getCititesLimit(List<City> cityList, int start, int offset) {
        return cityList.stream().sorted().skip(start).limit(offset).collect(Collectors.<City>toList());
    }


    List<City> sortCities(List<City> cityList, Map<City, Integer> distances, String sortBy, String order, Locale locale) {

        if (sortBy == null) {
            sortBy = "city";
        }

        if (order == null) {
            order = "asc";
        }

        switch (sortBy) {
            case ("distance"):
                return sortCitiesByDistance(cityList, distances, order);
            case ("city"):
            default:
                return sortCitiesByName(cityList, order, locale);
        }
    }

    private List<City> sortCitiesByDistance(List<City> cityList, Map<City, Integer> distances, String order) {

        Comparator<City> cityDistanceComparator = Comparator.comparingInt(distances::get);

        if (order.equals("desc")) {
            return cityList.stream().sorted(cityDistanceComparator.reversed()).collect(Collectors.<City>toList());
        }
        return cityList.stream().sorted(cityDistanceComparator).collect(Collectors.<City>toList());

    }


    List<City> sortCitiesByName(List<City> cityList, String order, Locale locale) {

        Comparator<City> cityNameComparator = (Comparator.comparing(o -> o.getName(locale)));

        if (order.equals("desc")) {
            return cityList.stream().sorted(cityNameComparator.reversed()).collect(Collectors.<City>toList());
        }
        return cityList.stream().sorted(cityNameComparator).collect(Collectors.<City>toList());


    }


}

package com.voroniuk.delivery.web.command;

import com.voroniuk.delivery.Path;
import com.voroniuk.delivery.db.dao.CityDAO;
import com.voroniuk.delivery.db.entity.City;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


public class MainCommand extends Command {

    private static final Logger LOG = Logger.getLogger(MainCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        LOG.debug("Main command starts");

        CityDAO cityDAO = new CityDAO();

        City currentCity = cityDAO.findCityByName("Odessa");

        req.getSession().setAttribute("currentCity", currentCity);
        currentCity = (City) req.getSession().getAttribute("currentCity");

        if(req.getSession().getAttribute("locale")==null){
            req.getSession().setAttribute("locale", Locale.getDefault());
        }




        Map<City, String> distances = new HashMap<>();
        List<City> cityList = (LinkedList<City>) req.getServletContext().getAttribute("cities");

        for (City city: cityList){
            String dist = String.format("%.2f", cityDAO.findDistance(currentCity, city));
            distances.put(city, dist);
        }

        req.getSession().setAttribute("distances", distances);



        String forward = Path.PAGE__MAIN;

        LOG.debug("Main command ends forward to " + forward);
        return forward;
    }
}

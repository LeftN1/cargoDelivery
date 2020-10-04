package com.voroniuk.delivery.web.command;

import com.voroniuk.delivery.Path;
import com.voroniuk.delivery.db.dao.CityDAO;
import com.voroniuk.delivery.db.entity.City;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MakeOrderCommand extends Command {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        CityDAO cityDAO = new CityDAO();

        String forward = null;

        if(req.getParameter("calculate")!=null) {
            City currentCity = (City) req.getSession().getAttribute("currentCity");
            City destination = cityDAO.findCityById(Integer.parseInt(req.getParameter("cityInp")));

            double cost = cityDAO.findDistance(destination, currentCity);
            req.setAttribute("cost", cost);
            forward = Path.PAGE__USER_ACCOUNT;
        }


        return forward;
    }
}

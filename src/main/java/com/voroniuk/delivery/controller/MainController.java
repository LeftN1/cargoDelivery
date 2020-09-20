package com.voroniuk.delivery.controller;


import com.voroniuk.delivery.dao.DBManager;
import com.voroniuk.delivery.dao.DBManagerImpl;
import com.voroniuk.delivery.entity.City;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.HashMap;

import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/cool")
public class MainController extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
        log("Method inti -----  =)");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
        resp.getWriter().write("Method service\n");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DBManager manager = DBManagerImpl.getInstance();

        City a = manager.getCity("Одесса");
//        City a = manager.getCity("Киев");

        Map<String, String> distMap = new LinkedHashMap<>();

        for (City city : manager.findAllCities()) {
            if (!city.equals(a)) {
                distMap.put(city.getName(), String.format("%.1f", manager.findDistance(a, city)));
            }
        }

        req.setAttribute("point_a", a.getName());
        req.setAttribute("distmap", distMap);
        getServletContext().getRequestDispatcher("/main.jsp").forward(req, resp);

    }
}

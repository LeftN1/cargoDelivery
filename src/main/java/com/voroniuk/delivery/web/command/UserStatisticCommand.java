package com.voroniuk.delivery.web.command;

import com.voroniuk.delivery.Path;
import com.voroniuk.delivery.db.dao.OrderDAO;
import com.voroniuk.delivery.db.dao.UserDAO;
import com.voroniuk.delivery.db.entity.Role;
import com.voroniuk.delivery.db.entity.User;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

public class UserStatisticCommand extends Command {
    private static final Logger LOG = Logger.getLogger(UserStatisticCommand.class);
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        LOG.debug("Command starts");
        UserDAO userDAO = new UserDAO();
        OrderDAO orderDAO = new OrderDAO();

        Map<User, Integer> statistic = new HashMap<>();

        List<User> allUsers = userDAO.findAllUsers();

        int count = 0;
        for (User user : allUsers){

            if(user.getRole().equals(Role.USER)){

               statistic.put(user, orderDAO.countDeliveriesByUser(user));
            }

        }
        Comparator<Map.Entry<User, Integer>> cmp = Map.Entry.comparingByValue();

        Map<User, Integer> res = statistic.entrySet().stream().sorted(cmp.reversed()).collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        req.getSession().setAttribute("statistic", res);

        String forward = Path.PAGE__STATISTIC;

        LOG.debug("Command finished");

        return forward;
    }
}

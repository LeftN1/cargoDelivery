package com.voroniuk.delivery.web.listener;


import org.apache.log4j.Logger;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Listen HttpSession events, count total active sessions
 *
 * @author M. Voroniuk
 */

public class SessionListener implements HttpSessionListener {
    private static final Logger LOG = Logger.getLogger(SessionListener.class);

    private static int totalSessions;

    public static int getTotalSessions() {
        return totalSessions;
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        String id = se.getSession().getId();

        totalSessions++;

        LOG.debug("Session " + id + " created. (total " + totalSessions + ")");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        String id = se.getSession().getId();

        totalSessions--;
        LOG.debug("Session " + id + " destroyed. (total " + totalSessions + ")");
    }
}

package com.voroniuk.delivery.web.filter;

import com.voroniuk.delivery.Path;

import com.voroniuk.delivery.db.entity.Role;
import com.voroniuk.delivery.db.entity.User;
import org.apache.log4j.Logger;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;


/**
 * Security filter.
 *
 * 
 */
public class CommandAccessFilter implements Filter {

	private static final Logger LOG = Logger.getLogger(CommandAccessFilter.class);

	// commands access	
	private static Map<Role, List<String>> accessMap = new HashMap<Role, List<String>>();
	private static List<String> commons = new ArrayList<String>();
	private static List<String> outOfControl = new ArrayList<String>();
	
	public void destroy() {
		LOG.debug("Filter destruction starts");
		// do nothing
		LOG.debug("Filter destruction finished");
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		LOG.debug("Filter starts");
		
		if (accessAllowed(request)) {
			LOG.debug("Filter finished");
			chain.doFilter(request, response);
		} else {
			String errorMessasge = "You do not have permission to access the requested resource";
			
			request.setAttribute("errorMessage", errorMessasge);
			LOG.trace("Set the request attribute: errorMessage --> " + errorMessasge);
			
			request.getRequestDispatcher(Path.PAGE__ERROR_PAGE)
					.forward(request, response);
		}
	}
	
	private boolean accessAllowed(ServletRequest request) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;

		String commandName = request.getParameter("command");
		if (commandName == null || commandName.isEmpty())
			return false;
		
		if (outOfControl.contains(commandName))
			return true;
		
		HttpSession session = httpRequest.getSession(false);
		if (session == null) 
			return false;

		User user = (User) session.getAttribute("user");
		if (user == null)
			return false;

		Role userRole = user.getRole();

		LOG.debug("User role = " + userRole + ". Trying get command = " + commandName);
		
		return accessMap.get(userRole).contains(commandName)
				|| commons.contains(commandName);
	}

	public void init(FilterConfig fConfig) throws ServletException {
		LOG.debug("Filter initialization starts");
		
		// roles
		accessMap.put(Role.MANAGER, asList(fConfig.getInitParameter("manager")));
		accessMap.put(Role.USER, asList(fConfig.getInitParameter("user")));
		LOG.trace("Access map --> " + accessMap);

		// commons
		commons = asList(fConfig.getInitParameter("common"));
		LOG.trace("Common commands --> " + commons);

		// out of control
		outOfControl = asList(fConfig.getInitParameter("out-of-control"));
		LOG.trace("Out of control commands --> " + outOfControl);
		
		LOG.debug("Filter initialization finished");
	}
	
	/**
	 * Extracts parameter values from string.
	 * 
	 * @param str
	 *            parameter values string.
	 * @return list of parameter values.
	 */
	private List<String> asList(String str) {
		List<String> list = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(str);
		while (st.hasMoreTokens()) list.add(st.nextToken());
		return list;		
	}
	
}
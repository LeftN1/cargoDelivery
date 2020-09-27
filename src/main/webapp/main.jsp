<%@ page import="com.voroniuk.delivery.db.entity.City" %>
<%@ page import="com.voroniuk.delivery.db.dao.CityDAO" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html>

<head>

    <title>Main page</title>

</head>

<body>
<h2>Cargo Delivery Service</h2>
<%
    CityDAO cityDAO = new CityDAO();
%>

<c:choose>

    <c:when test="${pageContext.session.getAttribute('role')==null}">
        <div>
            <form name="loginForm" action="/controller" method="post">
                <input type="hidden" name="command" value="login">
                <input type="text" size="20" name="login">
                <input type="password" size="20" name="password">
                <input type="submit" value="login">
                <a href="/controller?command=register">Registration</a>
            </form>
        </div>
    </c:when>
    <c:otherwise>
        <form name="logout" action="/controller" method="post">
            <input type="hidden" name="command" value="logout">
            <input type="submit" value="logout">
        </form>
    </c:otherwise>
</c:choose>

<hr>
Session Id: ${pageContext.session.id}
<br>
Role: ${pageContext.session.getAttribute("role")}
<div>
    <form action="main.jsp" method="post">
        <p>
            Choose your city:
            <select name="current">
                <%
                    City currentCity = cityDAO.findAllCities().get(0);

                    String current = request.getParameter("current") != null ? request.getParameter("current") : currentCity.getName();

                    for (City city : cityDAO.findAllCities()) {

                        out.println("<option " + (current.equals(city.getName()) ? "selected=true" : " ") + ">");
                        out.println(city.getName());
                        out.println("</option>");
                    }
                %>
            </select>
        </p>
        <p><input type="submit" value="choose"></p>
    </form>
</div>
<div>
    <%
        currentCity = cityDAO.getCity(current);
        out.println("Distance from " + currentCity.getName() + " to :<br>");
        out.println("<table>");

        for (City city : cityDAO.findAllCities()) {
            String dist = String.format("%.2f", cityDAO.findDistance(currentCity, city));
            out.println("<tr>");
            out.println("<td>" + city.getName() + "</td>");
            out.println("<td>" + dist + "</td>");
            out.println("<tr>");
        }
        out.println("</table>");
    %>
</div>

</body>
</html>
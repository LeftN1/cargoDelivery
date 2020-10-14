<%@ page import="com.voroniuk.delivery.Path" %><%--
  Created by IntelliJ IDEA.
  User: user
  Date: 06.10.2020
  Time: 16:05
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<c:set var="title" value="Report" />
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body>

<fmt:setLocale value="${locale.getLanguage()}"/>

<a href="/controller?command=main"><fmt:message key="all.label.account"/></a>

<h3><fmt:message key="report.label.report_generator"/></h3>

<form name="report" method="get" action="controller">
    <input type="hidden" name="command" value="report">
    <table>
        <tr>
            <td>
                <fmt:message key="main.label.choose_current_city"/>
            </td>
            <td>
                <select name="origin">
                    <option value="0">---<fmt:message key="main.option.any_city"/>---</option>
                    <c:forEach var="city" items="${applicationScope.cities}">
                        <option value="${city.getId()}" ${city.getId()==sessionScope.originId?" selected":" "}>${city.getName(locale)}</option>
                    </c:forEach>
                </select>
            </td>
        </tr>
        <tr>
            <td>
                <fmt:message key="main.label.choose_destination_city"/>
            </td>
            <td>
                <select name="destination">
                    <option value="0">---<fmt:message key="main.option.any_city"/>---</option>
                    <c:forEach var="city" items="${applicationScope.cities}">
                        <option value="${city.getId() }" ${city.getId()==sessionScope.destinationId?" selected":" "}>${city.getName(locale)}</option>
                    </c:forEach>
                </select>
            </td>
        </tr>
        <tr>
            <td>
                <fmt:message key="report.label.start_date"/>
            </td>
            <td>
                <input type="date" name="start_date" value="${start}">
            </td>
        </tr>
        <tr>
            <td>
                <fmt:message key="report.label.end_date"/>
            </td>
            <td>
                <input type="date" name="end_date" value="${end}">
            </td>
        </tr>
        <tr>
            <td>
                <fmt:message key="all.label.status"/>
            </td>
            <td>
                <select name="status">
                    <c:forEach var="status" items="${applicationScope.statuses}">
                        <option value="${status.getId()}" ${status.getId()==sessionScope.status.getId()?" selected" : ""} >${status.getName(locale)}</option>
                    </c:forEach>
                </select>
            </td>
        </tr>
        <tr>
            <td>
                <fmt:message key="manager.button.report_by_date"/> <input type="radio" name="type" value="by_date" checked>
            </td>
            <td>
                <fmt:message key="manager.button.report_by_city"/> <input type="radio" name="type" value="by_city" >
            </td>
        </tr>
        <tr>
            <td>
                <input type="submit" value="<fmt:message key="manager.button.report"/> "/>
            </td>
        </tr>
    </table>
</form>

Current date: ${currentDate}
<table>
    <tr>
        <th><fmt:message key="all.label.id"/></th>
        <th><fmt:message key="all.label.origin"/></th>
        <th><fmt:message key="all.label.destination"/></th>
        <th><fmt:message key="all.label.adress"/></th>
        <th><fmt:message key="all.label.cargo_type"/></th>
        <th><fmt:message key="all.label.weight"/></th>
        <th><fmt:message key="all.label.volume"/></th>
        <th><fmt:message key="all.label.cost"/></th>
        <th><fmt:message key="all.label.status"/></th>
        <th><fmt:message key="all.label.date"/></th>
    </tr>

    <c:forEach var="delivery" items="${deliveries}">
        <tr>
            <td>${delivery.getId()}</td>
            <td>${delivery.getOrigin().getName(locale)}</td>
            <td>${delivery.getDestination().getName(locale)}</td>
            <td>${delivery.getAdress()}</td>
            <td>${delivery.getType().getName(locale)}</td>
            <td>${delivery.getWeight()}</td>
            <td align="right">${delivery.getVolume()}</td>
            <td align="right">${delivery.getCost()}</td>
            <td>${delivery.getLastStatus().getName(locale)}</td>
            <td>${delivery.getLastDate()}</td>
        </tr>
    </c:forEach>
    <tr>
        <td>Total</td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td>${totalWeight}</td>
        <td align="right">${totalVolume}</td>
        <td align="right">${totalCost}</td>
        <td></td>
        <td></td>
    </tr>

</table>

<c:set var="current_page" value="<%=Path.COMMAND__REPORT%>" />
<%@ include file="/WEB-INF/jspf/pagination.jspf" %>

</body>
</html>

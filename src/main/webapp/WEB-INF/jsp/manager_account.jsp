<%@ include file="/WEB-INF/jspf/page.jspf" %>
<%@ include file="/WEB-INF/jspf/taglib.jspf" %>

<html>
<c:set var="title" value="Manager account" />
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<body>

<fmt:setLocale value="${locale.getLanguage()}"/>

<a href="/controller?command=main"><fmt:message key="account.anchor.main_page"/></a>
<br>
<a href="/controller?command=report"><fmt:message key="account.anchor.reports"/></a>

<form name="filter" method="get" action="controller">
    <input type="hidden" name="command" value="manager_account">
    <table>
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
                <input type="submit" value="<fmt:message key="manager.button.filter"/> "/>
            </td>
        </tr>
    </table>

</form>


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
       <!-- <th><fmt:message key="all.label.delete"/></th>
        <th><fmt:message key="all.label.edit"/></th>
        <th><fmt:message key="all.label.confirm"/></th>
        -->
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
            <!--<td><a href="/controller?command=delete&delivery_id=${delivery.getId()}" onclick="allert('click')"><fmt:message key="all.label.delete"/></a></td>-->
            <td><a href="#" onclick="if (confirm('<fmt:message key="manager.alert.confirm_delete"/>')){location.href='/controller?command=delete&delivery_id=${delivery.getId()}'}else {}"><fmt:message key="all.label.delete"/></a></td>
            <td><a href="#"><fmt:message key="all.label.edit"/></a></td>
            <td><c:if test="${delivery.getLastStatus().getId() == 1}">
                <a href="/controller?command=bill&delivery_id=${delivery.getId()}"><fmt:message key="all.label.confirm"/></a>
            </c:if></td>
            <td><c:if test="${delivery.getLastStatus().getId() == 3}">
                <a href="/controller?command=send&delivery_id=${delivery.getId()}"><fmt:message key="all.label.send"/></a>
            </c:if></td>
            <td><c:if test="${delivery.getLastStatus().getId() == 4}">
                <a href="/controller?command=arrived&delivery_id=${delivery.getId()}"><fmt:message key="all.label.arrived"/></a>
            </c:if></td>
            <td><c:if test="${delivery.getLastStatus().getId() == 5}">
                <a href="/controller?command=give_out&delivery_id=${delivery.getId()}"><fmt:message key="all.label.give_out"/></a>
            </c:if></td>
        </tr>
    </c:forEach>


</table>

<c:set var="current_page" value="<%=Path.COMMAND__MANAGER_ACCOUNT%>" />
<%@ include file="/WEB-INF/jspf/pagination.jspf" %>

</body>
</html>

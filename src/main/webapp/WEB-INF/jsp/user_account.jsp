
<%@ include file="/WEB-INF/jspf/page.jspf" %>
<%@ include file="/WEB-INF/jspf/taglib.jspf" %>


<c:set var="title" value="CDS" />
<c:set var="current" value="account"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>


<p><fmt:message key="all.label.user"/>: ${sessionScope.user.getLogin()}</p>

<h3>Create new delivery:</h3>
<form name="order" action="/controller" method="post">
    <input type="hidden" name="command" value="makeOrder">
    <table>
        <tr>
            <td width="200">
                <fmt:message key="main.label.choose_current_city"/>
            </td>
            <td>
                <input type="text" size="50" list="cityList" name="current"
                       value="${lastCurrent==null?sessionScope.currentCity.getName(locale):lastCurrent.getName(locale)}">
                <!--
                <select name="current">
                    <c:forEach var="city" items="${applicationScope.cities}">
                        <option value="${city.getId()}" ${city.getId()==sessionScope.currentCity.getId()?" selected":""}>${city.getName(locale)}</option>
                    </c:forEach>
                </select>
                -->
            </td>
        </tr>
        <tr>
            <td>
                <fmt:message key="main.label.choose_destination_city"/>
            </td>
            <td>
                <input type="text" size="50" list="cityList" name="cityInp" value="${destination.getName(locale)}">
                <datalist id="cityList">
                    <c:forEach var="city" items="${applicationScope.cities}">
                        <option>${city.getName(locale)}</option>
                    </c:forEach>
                </datalist>
            </td>
        </tr>
        <tr>
            <td>
                <fmt:message key="all.label.adress"/>
            </td>
            <td>
                <input type="text" size="50" name="adress" value="${adress}">
            </td>
        </tr>
        <tr>
            <td>
                <fmt:message key="all.label.cargo_type"/>
            </td>
            <td>
                <select name="type">
                    <c:forEach var="type" items="${applicationScope.cargoTypes}">
                        <option ${cType==type?" selected":""} value="${type.getId()}">${type.getName(locale)}</option>
                    </c:forEach>
                </select>
            </td>
        </tr>
        <tr>
            <td>
                <fmt:message key="all.label.weight"/>
            </td>
            <td>
                <input type="number" size="5" name="weight" min="1" value="${weight}">
            </td>
        </tr>
        <tr>
            <td>
                <fmt:message key="all.label.length"/>
            </td>
            <td>
                <input type="number" size="5" name="length" min="1" value="${length}">
            </td>
        </tr>
        <tr>
            <td>
                <fmt:message key="all.label.width"/>
            </td>
            <td>
                <input type="number" size="5" name="width" min="1" value="${width}">
            </td>
        </tr>
        <tr>
            <td>
                <fmt:message key="all.label.height"/>
            </td>
            <td>
                <input type="number" size="5" name="height" min="1" value="${height}">
            </td>
        </tr>
        <tr>
            <td>
                <fmt:message key="all.label.cost"/>
            </td>
            <td>
                ${cost}
            </td>
        </tr>
    </table>
    <input type="submit" name="order" value="<fmt:message key="user.button.make_order"/>"/>
    <input type="submit"  name="calculate" value="<fmt:message key="main.button.calculate"/>"/>
</form>
<hr>

<form name="filter" method="get" action="controller">
    <input type="hidden" name="command" value="user_account">
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
            <td>
                <input type="submit" value="<fmt:message key="manager.button.filter"/> "/>
            </td>
        </tr>
    </table>
</form>

<h4>Delivery list:</h4>

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
    </tr>

    <c:forEach var="delivery" items="${deliveries}">
        <tr>
            <td>${delivery.getId()}</td>
            <td>${delivery.getOrigin().getName(locale)}</td>
            <td>${delivery.getDestination().getName(locale)}</td>
            <td>${delivery.getAdress()}</td>
            <td>${delivery.getType().getName(locale)}</td>
            <td>${delivery.getWeight()}</td>
            <td>${delivery.getVolume()}</td>
            <td>${delivery.getCost()}</td>
            <td>${delivery.getLastStatus().getName(locale)}</td>
            <c:if test="${delivery.getLastStatus().getId() == 2}">
            <td>
                <form class="tdform" action="/controller" method="get">
                    <input type="hidden" name="command" value="pay">
                    <input type="hidden" name="delivery_id" value="${delivery.getId()}">
                    <input type="submit" value="<fmt:message key="all.href.pay"/>">
                </form>
            </td>
            </c:if>
        </tr>
    </c:forEach>

</table>

<c:set var="current_page" value="<%=Path.COMMAND__USER_ACCOUNT%>" />
<%@ include file="/WEB-INF/jspf/pagination.jspf" %>

<%@ include file="/WEB-INF/jspf/bottom.jspf" %>

<%@ page import="com.voroniuk.delivery.Path" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!doctype html>
<html>

<head>
    <title>Main page</title>
</head>

<body>
<fmt:setLocale value="${sessionScope.locale.getLanguage()}"/>
<h2>Cargo Delivery Service</h2>

<div style="color: red">${msg}</div>

<c:choose>
    <c:when test="${sessionScope.user==null}">
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
        <form name="account" action="/controller" method="post">
            <input type="hidden" name="command" value="account">
            <input type="submit" value="account">
        </form>
        <form name="logout" action="/controller" method="post">
            <input type="hidden" name="command" value="logout">
            <input type="submit" value="logout">
        </form>
    </c:otherwise>
</c:choose>

<form name="langForm" action="/controller" method="post">
    <input type="hidden" name="command" value="changeLocale">
    <select name="choosenLang">
        <c:forEach var="item" items="${applicationScope.locales}">
            <option ${item.getLocale()==locale? " selected":""}>${item.name()}</option>
        </c:forEach>
        </option>
    </select>
    <input type="submit" value=<fmt:message key="main.button.change"/>>
</form>




<!--
<hr>
<c:out value="lang : ${sessionScope.locale.getLanguage()}"/>
<br>
Session Id: ${pageContext.session.id}
<br>
Role: ${sessionScope.user.getRole()}
<br>
Current city: ${sessionScope.currentCity}
<br>
Current region: ${sessionScope.regionId}
<hr>
-->


<form name="changeCurrent" action="controller" method="post">
    <table>
        <tr>
            <td width="200"><label><fmt:message key="main.label.choose_current_city"/></label></td>
            <input type="hidden" name="command" value="main">
            <td><input type="text" size="30" list="cityList" name="cityInp"
                       value="${sessionScope.currentCity.getName(locale)}"></td>
            <datalist id="cityList">
                <c:forEach var="city" items="${applicationScope.cities}">
                    <option>${city.getName(locale)}</option>
                </c:forEach>
            </datalist>
        </tr>
        <tr>
            <td><label><fmt:message key="main.label.filter_by_region"/></label></td>
            <td><select name="region">
                <option value="0">---<fmt:message key="main.option.all_regions"/>---</option>
                <c:forEach var="region" items="${applicationScope.regions}">
                    <option value="${region.getId()}" ${region.getId()==sessionScope.regionId?" selected" : ""} >${region.getName(locale)}</option>
                </c:forEach>
            </select></td>
        </tr>
        <tr>
            <td><label><fmt:message key="all.label.weight"/></label></td>
            <td><input type="number" size="5" name="weight" value="${sessionScope.weight}"></td>
        </tr>
        <tr>
            <td><label><fmt:message key="all.label.length"/></label></td>
            <td><input type="number" size="5" name="length" value="${sessionScope.length}"></td>
        </tr>
        <tr>
            <td><label><fmt:message key="all.label.width"/></label></td>
            <td><input type="number" size="5" name="width" value="${sessionScope.width}"></td>
        </tr>
        <tr>
            <td><label><fmt:message key="all.label.height"/></label></td>
            <td><input type="number" size="5" name="height" value="${sessionScope.height}"></td>
        </tr>
        <tr>
            <td><input type="submit" value="<fmt:message key="main.button.calculate"/>"></td>
        </tr>
    </table>
</form>


<br><fmt:message key="main.label.parcel_weight"/> ${weight} <fmt:message key="all.label.kg"/>
<br><fmt:message key="main.label.parcel_volume"/> ${volume} <fmt:message key="all.label.dm3"/>

<p><fmt:message key="main.label.delivery_cost_from_city"/> ${sessionScope.currentCity.getName(locale)}:</p>
<table>
    <tr>
        <th><fmt:message key="all.label.city"/><a style="text-decoration: none" href="<%=Path.COMMAND__MAIN%>&sort=city&order=asc">&#9650;</a><a style="text-decoration: none" href="<%=Path.COMMAND__MAIN%>&sort=city&order=desc">&#9660;</a> </th>
        <th><fmt:message key="all.label.distance"/><a style="text-decoration: none" href="<%=Path.COMMAND__MAIN%>&sort=distance&order=asc">&#9650;</a><a style="text-decoration: none" href="<%=Path.COMMAND__MAIN%>&sort=distance&order=desc">&#9660;</a></th>
        <th><fmt:message key="all.label.cost"/></th>
    </tr>
    <c:forEach var="city" items="${cityList}">
        <tr>
            <td width="200">
                    ${city.getName(locale)}
            </td>
            <td width="150" align="center">
                    ${distances.get(city)}
            </td>
            <td width="100" align="center">
                    ${costs.get(city)}
            </td>
        </tr>
    </c:forEach>
</table>

<c:if test="${pageNo>2}">
    <a href="<%=Path.COMMAND__MAIN%>&page=1"><fmt:message key="all.href.first"/></a>
    ...
</c:if>

<c:forEach var="i" begin="${pageNo-2>1?pageNo-2:1}" end="${pageNo+2<totalPages?pageNo+2:totalPages}">
    <c:choose>
        <c:when test="${i==pageNo}">
            <c:set var="ref" value="[${i}]"/>
        </c:when>
        <c:otherwise>
            <c:set var="ref" value="${i}"/>
        </c:otherwise>
    </c:choose>
    <a href="<%=Path.COMMAND__MAIN%>&page=${i}">${ref}</a>
</c:forEach>

<c:if test="${totalPages-pageNo>2}">
    ...
    <a href="<%=Path.COMMAND__MAIN%>&page=${totalPages}"><fmt:message key="all.href.last"/></a>
</c:if>

<!-- Access filter test  -->
<!--
<br>
<a href="<%=Path.COMMAND__USER_ACCOUNT%>">User access</a>
<br>
<a href="<%=Path.COMMAND__MANAGER_ACCOUNT%>">Manager access</a>
-->

</body>
</html>
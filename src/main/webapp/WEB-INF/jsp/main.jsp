<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!doctype html>
<html>

<head>
    <title>Main page</title>
</head>

<body>
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
    <input type="submit" value="change">
</form>

<c:out value="lang : ${sessionScope.locale.getLanguage()}"/>
<fmt:setLocale value="${sessionScope.locale.getLanguage()}"/>

<hr>
Session Id: ${pageContext.session.id}
<br>
Role: ${sessionScope.user.getRole()}
<hr>


<form name="changeCurrent" action="controller" method="post">
    <label><fmt:message key="main.label.choose_current_city"/></label>
    <input type="hidden" name="command" value="main">
    <input type="text" size="30" list="cityList" name="cityInp"
           value="${sessionScope.currentCity.getName(locale)}">
    <datalist id="cityList">
        <c:forEach var="city" items="${applicationScope.cities}">
            <option>${city.getName(locale)}</option>
        </c:forEach>
    </datalist>
    <input type="submit" value="<fmt:message key="main.button.change"/>">
<br>
    <label><fmt:message key="main.label.filter_by_region"/></label>
    <select name="region">
        <option value="0" >---<fmt:message key="main.option.all_regions"/>---</option>
        <c:forEach var="region" items="${applicationScope.regions}">
            <option value="${region.getId()}" >${region.getName(locale)}</option>
        </c:forEach>
    </select>
    <input type="submit" value="<fmt:message key="main.button.change"/>">
</form>

<p>Distance from ${sessionScope.currentCity.getName(locale)} to:</p>

<table>
    <c:forEach var="city" items="${cityList}">
        <tr>
            <td>
                    ${city.getName(locale)}
            </td>
            <td>
                    ${distances.get(city)}
            </td>
        </tr>
    </c:forEach>
</table>


</body>
</html>
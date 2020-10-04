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

<form name="langForm" action="/controller" method="post">
    <input type="hidden" name="command" value="changeLocale">
    <select name="choosenLang">
        <c:forEach var="item" items="${applicationScope.locales}">
            <option>${item.getName()}</option>
        </c:forEach>
        </option>
    </select>
    <input type="submit" value="change">
</form>

<c:out value="lang : ${sessionScope.language}"/>
<fmt:setLocale value="${sessionScope.language}" />

<hr>
Session Id: ${pageContext.session.id}
<br>
Role: ${pageContext.session.getAttribute("role")}




</body>
</html>
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
<head>
    <title>Title</title>
</head>
<body>

<fmt:setLocale value="${locale.getLanguage()}"/>

<a href="/controller?command=main"><fmt:message key="account.anchor.main_page"/></a>

<h4>Delivery list:</h4>

<table>
    <tr>
        <th><fmt:message key="all.label.id"/></th>
        <th><fmt:message key="all.label.origin"/> </th>
        <th><fmt:message key="all.label.destination"/> </th>
        <th><fmt:message key="all.label.adress"/> </th>
        <th><fmt:message key="all.label.cargo_type"/> </th>
        <th><fmt:message key="all.label.weight"/> </th>
        <th><fmt:message key="all.label.volume"/> </th>
        <th><fmt:message key="all.label.cost"/> </th>
        <th><fmt:message key="all.label.status"/> </th>
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
        </tr>
    </c:forEach>


</table>

<c:if test="${pageNo>2}">
    <a href="<%=Path.COMMAND__MANAGER_ACCOUNT%>&page=1"><fmt:message key="all.href.first"/></a>
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
    <a href="<%=Path.COMMAND__MANAGER_ACCOUNT%>&page=${i}">${ref}</a>
</c:forEach>

<c:if test="${totalPages-pageNo>2}">
    ...
    <a href="<%=Path.COMMAND__MANAGER_ACCOUNT%>&page=${totalPages}"> <fmt:message key="all.href.last"/> </a>
</c:if>

</body>
</html>

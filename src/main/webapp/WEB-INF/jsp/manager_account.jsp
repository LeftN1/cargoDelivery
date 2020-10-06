<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 06.10.2020
  Time: 16:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<table>
    <tr>
        <th>id</th>
        <th>Origin</th>
        <th>Destination</th>
        <th>Adress</th>
        <th>Type</th>
        <th>Weight</th>
        <th>Volume</th>
        <th>Cost</th>
        <th>Status</th>
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
        </tr>
    </c:forEach>

</table>

</body>
</html>

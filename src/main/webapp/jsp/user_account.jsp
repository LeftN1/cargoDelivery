<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h3>Create new delivery:</h3>
<form name="order" action="/controller" method="post">
    <input type="hidden" name="command" value="makeOrder">
    <table>
        <tr>
            <td>
                Destination city:
            </td>

            <td>
                <input type="text" size="50" list="cityList" name="cityInp">

                <datalist id="cityList">
                    <c:forEach var="city" items="${applicationScope.cities}">
                        <option value="${city.getId()}">${city.getName(locale)}</option>
                    </c:forEach>
                </datalist>
            </td>
        </tr>
        <tr>
            <td>
                Adress:
            </td>
            <td>
                <input type="text" size="50" name="adress">
            </td>
        </tr>
        <tr>
            <td>Cargo type: </td>
            <td>
                <select name="type">
                    <c:forEach var="type" items="${applicationScope.cargoTypes}">
                        <option>${type.name()}</option>
                    </c:forEach>
                </select>
            </td>
        </tr>
        <tr>
            <td>
                Weight:
            </td>
            <td>
                <input type="number" size="5" name="weight"> kg
            </td>
        </tr>
        <tr>
            <td>
                Volume:
            </td>
            <td>
                <input type="number" size="5" name="volume"> dm3
            </td>
        </tr>
        <tr>
            <td>
                Cost:
            </td>
            <td>
                ${cost}
            </td>
        </tr>
    </table>
    <input type="submit" name="order" value="make order">
    <input type="submit" name="calculate" value="calculate">

</form>

</body>
</html>

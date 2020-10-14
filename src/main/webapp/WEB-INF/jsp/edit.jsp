
<%@ include file="/WEB-INF/jspf/page.jspf" %>
<%@ include file="/WEB-INF/jspf/taglib.jspf" %>


<html>

<c:set var="title" value="Edit" />
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body>
<fmt:setLocale value="${locale.getLanguage()}"/>

<a href="/controller?command=account"><fmt:message key="all.label.account"/></a>

<p>User: ${sessionScope.user.getLogin()}</p>
<p>Locale : ${sessionScope.locale.getLanguage()}</p>
<h3><fmt:message key="manger.label.edit_delivery"/></h3>
<form name="order" action="/controller" method="post">
    <input type="hidden" name="command" value="save">
    <input type="hidden" name="delivery_id" value="${delivery_id}">
    <table>
        <tr>
            <td width="200">
                <fmt:message key="main.label.choose_current_city"/>
            </td>
            <td>
                <input type="text" size="50" list="cityList" name="origin" value="${origin.getName(locale)}">
            </td>
        </tr>
        <tr>
            <td>
                <fmt:message key="main.label.choose_destination_city"/>
            </td>
            <td>
                <input type="text" size="50" list="cityList" name="destination" value="${destination.getName(locale)}">
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
                <input type="number" size="5" name="weight" value="${weight}">
            </td>
        </tr>
        <tr>
            <td>
                <fmt:message key="all.label.volume"/>
            </td>
            <td>
                <input type="number" size="5" name="volume" value="${volume}">
            </td>
        </tr>
        <tr>
            <td>
                <fmt:message key="all.label.cost"/>
            </td>
            <td>
                <input type="number" size="5" name="cost" value="${cost}">
            </td>
        </tr>
    </table>
    <input type="submit" name="order" value="<fmt:message key="manager.edit.button.save"/>"/>
</form>


</body>
</html>

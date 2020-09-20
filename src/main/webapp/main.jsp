<%@ page import="java.util.List" %>
<%@ page import="com.voroniuk.delivery.entity.User" %>
<%@ page import="java.util.Map" %>
<!doctype html>
<html>

<head>

    <title>Main page</title>

</head>

<body>
<h2>Cargo Delivery Service</h2>

<hr>

<%
       Map<String, String> distMap = (Map<String, String>) request.getAttribute("distmap");
%>

<h3>Distance from <%=request.getAttribute("point_a")%> to:</h3>
<table>

    <%  for (String city : distMap.keySet()) {%>

        <tr>
            <td><%=city%></td>
            <td><%=distMap.get(city)%></td>
        </tr>

    <%}%>

</table>

</body>
</html>
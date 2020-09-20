<%@ page import="java.util.List" %>
<%@ page import="com.voroniuk.delivery.entity.User" %>
<!doctype html>
<html>

<head>

    <title>Main page</title>

</head>

<body>
<h2>Cargo Delivery Service</h2>

<hr>

<%
    String distance = (String) request.getAttribute("distance");
%>

<%=distance%>

</body>
</html>
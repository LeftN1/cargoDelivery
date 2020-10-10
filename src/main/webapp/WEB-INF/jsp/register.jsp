<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 27.09.2020
  Time: 13:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<fmt:setLocale value="${locale.getLanguage()}"/>

<a href="/controller?command=main"><fmt:message key="account.anchor.main_page"/></a>

<h2><fmt:message key="register.label.register_page"/></h2>

<form action="/controller" method="post">
    <input type="hidden" name="command" value="register">
    <table>
        <tr><td><fmt:message key="all.label.login"/> </td> <td><input type="text" name="login" size="20" value="<%=request.getParameter("login") != null ? request.getParameter("login") : ""%>"></td></tr>
        <tr><td><fmt:message key="all.label.password"/></td> <td><input type="password" name="password" size="20"></td></tr>
        <tr><td><fmt:message key="register.label.confirm"/></td> <td><input type="password" name="confirm" size="20"></td></tr>
    </table>

    <input type="submit" name="register" value="<fmt:message key="register.button.register"/>">
</form>

<div style="color: red">
    <% if (request.getAttribute("msg") != null) {
        out.println(request.getAttribute("msg"));
    }
    %>
</div>

</body>
</html>

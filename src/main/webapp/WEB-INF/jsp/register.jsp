
<%@ include file="/WEB-INF/jspf/page.jspf" %>
<%@ include file="/WEB-INF/jspf/taglib.jspf" %>

<c:set var="title" value="Register"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

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

<%@ include file="/WEB-INF/jspf/bottom.jspf" %>
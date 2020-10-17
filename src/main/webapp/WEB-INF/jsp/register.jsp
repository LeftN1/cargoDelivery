<%@ include file="/WEB-INF/jspf/page.jspf" %>
<%@ include file="/WEB-INF/jspf/taglib.jspf" %>

<c:set var="title" value="Register"/>
<c:set var="current" value="register"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>


<div class="container border border-light">
    <h3><fmt:message key="register.label.register_page"/></h3>
    <form action="/controller" method="post">
        <input type="hidden" name="command" value="register">

        <div class="form-group row">
            <div class="col-md-2">
                <label><fmt:message key="all.label.login"/></label>
            </div>
            <div class="col-md-3">
                <input class="form-control" type="text" name="login" size="20"
                       value="<%=request.getParameter("login") != null ? request.getParameter("login") : ""%>"/>
            </div>
        </div>

        <div class="form-group row">
            <div class="col-md-2">
                <label><fmt:message key="all.label.password"/></label>
            </div>
            <div class="col-md-3">
                <input class="form-control" type="password" name="password" size="20">
            </div>
        </div>

        <div class="form-group row">
            <div class="col-md-2">
                <label><fmt:message key="register.label.confirm"/></label>
            </div>
            <div class="col-md-3">
                <input class="form-control" type="password" name="confirm" size="20">
            </div>
        </div>

        <div class="form-group row">
            <div class="col-md-2">
                <input class="btn btn-primary" type="submit" name="register"
                       value="<fmt:message key="register.button.register"/>">
            </div>
        </div>
    </form>

    <div style="color: red">
        <% if (request.getAttribute("msg") != null) {
            out.println(request.getAttribute("msg"));
        }
        %>
    </div>

</div>

<%@ include file="/WEB-INF/jspf/bottom.jspf" %>
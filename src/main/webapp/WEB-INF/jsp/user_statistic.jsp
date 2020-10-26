<%@ include file="/WEB-INF/jspf/page.jspf" %>
<%@ include file="/WEB-INF/jspf/taglib.jspf" %>


<c:set var="title" value="CDS"/>
<c:set var="current" value="user_account"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>


<div class="container border border-light">
    <h3><fmt:message key="user.label.user_statistic"/></h3>


    <table class="table table-striped table-bordered table-condensed table-sm">
        <tr>
            <th><fmt:message key="all.label.user"/></th>
            <th><fmt:message key="all.label.count"/></th>
        </tr>

        <c:forEach var="user" items="${statistic.keySet()}">
            <tr>
                <td>${user.getLogin()}</td>
                <td>${statistic.get(user)}</td>
            </tr>
        </c:forEach>

    </table>

    <c:set var="current_page" value="<%=Path.COMMAND__USER_ACCOUNT%>"/>
    <%@ include file="/WEB-INF/jspf/pagination.jspf" %>

</div>


<%@ include file="/WEB-INF/jspf/bottom.jspf" %>

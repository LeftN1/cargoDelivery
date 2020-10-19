<%@ include file="/WEB-INF/jspf/page.jspf" %>
<%@ include file="/WEB-INF/jspf/taglib.jspf" %>


<c:set var="title" value="Manager account"/>
<c:set var="current" value="manager_account"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<div class="container border border-light">
    <form name="filter" method="get" action="controller">
        <input type="hidden" name="command" value="manager_account">

        <div class="form-group row">
            <div class="col-md-4">
                <fmt:message key="all.label.status"/>
            </div>
            <div class="col-md-6">
                <select class="custom-select" name="status">
                    <c:forEach var="status" items="${applicationScope.statuses}">
                        <option value="${status.getId()}" ${status.getId()==sessionScope.status.getId()?" selected" : ""} >${status.getName(locale)}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="form-group row">
            <div class="col-md-4">
                <fmt:message key="main.label.choose_current_city"/>
            </div>
            <div class="col-md-6">
                <select class="custom-select" name="origin">
                    <option value="0">---<fmt:message key="main.option.any_city"/>---</option>
                    <c:forEach var="city" items="${applicationScope.cities}">
                        <option value="${city.getId()}" ${city.getId()==sessionScope.originId?" selected":" "}>${city.getName(locale)}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="form-group row">
            <div class="col-md-4">
                <fmt:message key="main.label.choose_destination_city"/>
            </div>
            <div class="col-md-6">
                <select class="custom-select" name="destination">
                    <option value="0">---<fmt:message key="main.option.any_city"/>---</option>
                    <c:forEach var="city" items="${applicationScope.cities}">
                        <option value="${city.getId() }" ${city.getId()==sessionScope.destinationId?" selected":" "}>${city.getName(locale)}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="form-group row">
            <div class="col-md-4">
                <input class="btn btn-primary" type="submit" value="<fmt:message key="manager.button.filter"/> "/>
            </div>
        </div>


    </form>


    <table class="table table-striped table-bordered table-condensed table-sm">
        <tr>
            <th><fmt:message key="all.label.id"/></th>
            <th><fmt:message key="all.label.origin"/></th>
            <th><fmt:message key="all.label.destination"/></th>
            <th><fmt:message key="all.label.adress"/></th>
            <th><fmt:message key="all.label.cargo_type"/></th>
            <th><fmt:message key="all.label.weight"/></th>
            <th><fmt:message key="all.label.volume"/></th>
            <th><fmt:message key="all.label.cost"/></th>
            <th><fmt:message key="all.label.status"/></th>
            <th><fmt:message key="all.label.date"/></th>
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
                <td>${delivery.getLastDateString()}</td>
                <td>
                    <input class="btn btn-outline-danger" type="button" onclick="if (confirm('<fmt:message
                            key="manager.alert.confirm_delete"/>')){location.href='/controller?command=delete&delivery_id=${delivery.getId()}'}else {}"
                           value="<fmt:message key="all.label.delete"/>"/>
                </td>
                <td>
                    <form class="tdform" action="/controller" method="get">
                        <input type="hidden" name="command" value="edit"/>
                        <input type="hidden" name="delivery_id" value="${delivery.getId()}"/>
                        <input class="btn btn-outline-primary" type="submit" value="<fmt:message key="all.label.edit"/>">
                    </form>
                </td>
                <c:if test="${delivery.getLastStatus().getId() == 1}">
                    <td height="20">
                        <form class="tdform" action="/controller" method="get">
                            <input type="hidden" name="command" value="bill"/>
                            <input type="hidden" name="delivery_id" value="${delivery.getId()}"/>
                            <input class="btn btn-outline-primary" type="submit" value="<fmt:message key="all.label.confirm"/>"/>
                        </form>
                    </td>
                </c:if>
                <c:if test="${delivery.getLastStatus().getId() == 3}">
                    <td>
                        <form class="tdform" action="/controller" method="get">
                            <input type="hidden" name="command" value="send"/>
                            <input type="hidden" name="delivery_id" value="${delivery.getId()}"/>
                            <input class="btn btn-outline-primary" type="submit" value="<fmt:message key="all.label.send"/>"/>
                        </form>
                    </td>
                </c:if>
                <c:if test="${delivery.getLastStatus().getId() == 4}">
                    <td>
                        <form class="tdform" action="/controller" method="get">
                            <input type="hidden" name="command" value="arrived"/>
                            <input type="hidden" name="delivery_id" value="${delivery.getId()}"/>
                            <input class="btn btn-outline-primary" type="submit" value="<fmt:message key="all.label.arrived"/>"/>
                        </form>
                    </td>
                </c:if>
                <c:if test="${delivery.getLastStatus().getId() == 5}">
                    <td>
                        <form class="tdform" action="/controller" method="get">
                            <input type="hidden" name="command" value="give_out"/>
                            <input type="hidden" name="delivery_id" value="${delivery.getId()}"/>
                            <input class="btn btn-outline-primary" type="submit" value="<fmt:message key="all.label.give_out"/>"/>
                        </form>
                    </td>
                </c:if>
            </tr>
        </c:forEach>
    </table>

    <c:set var="current_page" value="<%=Path.COMMAND__MANAGER_ACCOUNT%>"/>
    <%@ include file="/WEB-INF/jspf/pagination.jspf" %>
</div>

<%@ include file="/WEB-INF/jspf/bottom.jspf" %>

<%@ include file="/WEB-INF/jspf/page.jspf" %>
<%@ include file="/WEB-INF/jspf/taglib.jspf" %>

<c:set var="title" value="Report"/>
<c:set var="current" value="report"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<div class="container border border-light">
    <h3><fmt:message key="report.label.report_generator"/></h3>

    <form name="report" method="get" action="controller">
        <input type="hidden" name="command" value="report">

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
                    <fmt:message key="report.label.start_date"/>
                </div>
                <div class="col-md-6">
                    <input class="form-control" type="date" name="start_date" value="${start}">
                </div>
            </div>

            <div class="form-group row">
                <div class="col-md-4">
                    <fmt:message key="report.label.end_date"/>
                </div>
                <div class="col-md-6">
                    <input class="form-control" type="date" name="end_date" value="${end}">
                </div>
            </div>

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
                <div class="col-md-3">
                    <fmt:message key="manager.button.report_by_date"/> <input type="radio" name="type" value="by_date"
                ${type.equals("by_date")? " checked": ""}>
                </div>
                <div class="col-md-3">
                    <fmt:message key="manager.button.report_by_city"/> <input type="radio" name="type" value="by_city"
                ${type.equals("by_city")? " checked": ""}>
                </div>
            </div>
            <div class="form-group row">
                <div class="col-md-4">
                    <input class="btn btn-primary" type="submit" value="<fmt:message key="manager.button.report"/> "/>
                </div>
            </div>
    </form>

    <a href="/controller?command=download"><fmt:message key="report.ancor.download_xls"/></a>

    <table class="table table-striped table-bordered table-sm">
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
            <th><fmt:message key="all.label.chosen_status_date"/>${sessionScope.status.getName(locale)}</th>
            <th><fmt:message key="all.label.last_date"/></th>
        </tr>

        <c:forEach var="entry" items="${report}">
            <tr>
                <td><b>${entry.key}</b></td>
            </tr>
            <c:forEach var="delivery" items="${entry.value}">
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
                    <td>${delivery.getStatusDateString(sessionScope.status)}</td>
                    <td>${delivery.getLastDateString()}</td>
                </tr>
            </c:forEach>
            <tr>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td><u><i><fmt:message key="report.label.total"/></i></u></td>
                <td><b>${totals.get(entry.key).getTotalWeight()}</b></td>
                <td align="right"><b>${totals.get(entry.key).getTotalVolume()}</b></td>
                <td align="right"><b>${totals.get(entry.key).getTotalCost()}</b></td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
        </c:forEach>


    </table>
</div>
<%@ include file="/WEB-INF/jspf/bottom.jspf" %>

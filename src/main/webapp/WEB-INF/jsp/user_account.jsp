<%@ include file="/WEB-INF/jspf/page.jspf" %>
<%@ include file="/WEB-INF/jspf/taglib.jspf" %>


<c:set var="title" value="CDS"/>
<c:set var="current" value="user_account"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>


<div class="container border border-light">
    <h3><fmt:message key="user.label.create_new_delivery"/></h3>

    <form name="order" action="/controller" method="post">
        <input type="hidden" name="command" value="makeOrder">
        <div class="form-group row">
            <div class="form-group col-md-7">
                <div class="form-group row">

                    <!-- CHOOSE ORIGIN CITY -->
                    <div class="col-md-5">
                        <label><fmt:message key="main.label.choose_current_city"/></label>
                    </div>
                    <div class="col-md-7">
                        <input class="form-control" type="text" list="cityList" name="current"
                               value="${lastCurrent==null?sessionScope.currentCity.getName(locale):lastCurrent.getName(locale)}">
                        <small class="text-danger">${origin_city_msg}</small>
                    </div>

                </div>

                <!-- CHOOSE DESTINATION CITY -->
                <div class="form-group row">
                    <div class="col-md-5">
                        <fmt:message key="main.label.choose_destination_city"/>
                    </div>
                    <div class="col-md-7">
                        <input class="form-control" type="text" list="cityList" name="cityInp"
                               value="${destination.getName(locale)}">
                        <datalist id="cityList">
                            <c:forEach var="city" items="${applicationScope.cities}">
                                <option>${city.getName(locale)}</option>
                            </c:forEach>
                        </datalist>
                        <small class="text-danger">${destination_city_msg}</small>
                    </div>
                </div>

                <!-- INPUT ADRESS -->
                <div class="form-group row">
                    <div class="col-md-5">
                        <fmt:message key="all.label.adress"/>
                    </div>
                    <div class="col-md-7">
                        <input class="form-control" type="text" name="address" value="${address}">
                        <small class="text-danger">${address_msg}</small>
                    </div>
                </div>

                <!-- CARGO TYPE -->
                <div class="form-group row">
                    <div class="col-md-5">
                        <fmt:message key="all.label.cargo_type"/>
                    </div>
                    <div class="col-md-7">
                        <select class="custom-select" name="type">
                            <c:forEach var="type" items="${applicationScope.cargoTypes}">
                                <option ${cType==type?" selected":""}
                                        value="${type.getId()}">${type.getName(locale)}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <!-- COST -->
                <div class="form-group row">
                    <div class="col-md-5">
                        <fmt:message key="all.label.cost"/>
                    </div>
                    <div class="col-md-2">
                        <strong>${cost}</strong>
                    </div>
                </div>
                <div class="form-group row">
                    <div class="col-md-5">
                        <input class="btn btn-primary" type="submit" name="order"
                               value="<fmt:message key="user.button.make_order"/>"/>
                    </div>
                    <div class="col-md-5">
                        <input class="btn btn-info" type="submit" name="calculate"
                               value="<fmt:message key="main.button.calculate"/>"/>
                    </div>
                </div>
            </div>
            <div class="form-group col">


            </div>
            <div class="form-group col-md-3">

                <!-- WEIGHT -->
                <div class="form-group row">
                    <div class="col-md-5">
                        <fmt:message key="all.label.weight"/>
                    </div>
                    <div class="col-md-7">
                        <input class="form-control" type="number" name="weight" min="1" value="${weight}">
                    </div>
                </div>

                <!-- LENGTH -->
                <div class="form-group row">
                    <div class="col-md-5">
                        <fmt:message key="all.label.length"/>
                    </div>
                    <div class="col-md-7">
                        <input class="form-control" type="number" name="length" min="1" value="${length}">
                    </div>
                </div>

                <!-- WIDTH -->
                <div class="form-group row">
                    <div class="col-md-5">
                        <fmt:message key="all.label.width"/>
                    </div>
                    <div class="col-md-7">
                        <input class="form-control" type="number" name="width" min="1" value="${width}">
                    </div>
                </div>

                <!-- HEIGHT -->
                <div class="form-group row">
                    <div class="col-md-5">
                        <fmt:message key="all.label.height"/>
                    </div>
                    <div class="col-md-7">
                        <input class="form-control" type="number" name="height" min="1" value="${height}">
                    </div>
                </div>
            </div>
            <div class="form-group col">
            </div>
        </div>
    </form>


    <hr>
    <h4><fmt:message key="user.label.my_deliveries"/></h4>


    <form name="filter" method="get" action="controller">
        <input type="hidden" name="command" value="user_account">

        <div class="form-group row">
            <div class="col-md-1">
                <label><fmt:message key="all.label.status"/></label>
            </div>
            <div class="col-md-2">
                <select class="custom-select" name="status">
                    <c:forEach var="status" items="${applicationScope.statuses}">
                        <option value="${status.getId()}" ${status.getId()==sessionScope.status.getId()?" selected" : ""} >${status.getName(locale)}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-md-2">
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
        </tr>

        <c:forEach var="delivery" items="${deliveries}">
            <tr>
                <td>${delivery.getId()}</td>
                <td>${delivery.getOrigin().getName(locale)}</td>
                <td>${delivery.getDestination().getName(locale)}</td>
                <td>${delivery.getAdress()}</td>
                <td>${delivery.getType().getName(locale)}</td>
                <td>${delivery.getWeight()}</td>
                <td>${delivery.getVolume()}</td>
                <td>${delivery.getCost()}</td>
                <td>${delivery.getLastStatus().getName(locale)}</td>
                <c:if test="${delivery.getLastStatus().getId() == 2}">
                    <td>
                        <form class="tdform" action="/controller" method="get">
                            <input type="hidden" name="command" value="pay">
                            <input type="hidden" name="delivery_id" value="${delivery.getId()}">
                            <input class="btn btn-outline-primary" type="submit" value="<fmt:message key="all.href.pay"/>">
                        </form>
                    </td>
                </c:if>
            </tr>
        </c:forEach>

    </table>

    <c:set var="current_page" value="<%=Path.COMMAND__USER_ACCOUNT%>"/>
    <%@ include file="/WEB-INF/jspf/pagination.jspf" %>

</div>


<%@ include file="/WEB-INF/jspf/bottom.jspf" %>

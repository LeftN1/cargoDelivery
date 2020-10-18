<%@ include file="/WEB-INF/jspf/page.jspf" %>
<%@ include file="/WEB-INF/jspf/taglib.jspf" %>


<c:set var="title" value="CDS"/>
<c:set var="current" value="main"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>


<div class="container border border-light">
    <div class="row">
        <div class="col-md-4">
            <c:if test="${sessionScope.user==null}">
                <form name="loginForm" action="/controller" method="post">
                    <input type="hidden" name="command" value="login"/>
                    <div class="form-group row">
                        <div class="col-md-4">
                            <label><fmt:message key="all.label.login"/> </label>
                        </div>
                        <div class="col">
                            <input class="form-control" type="text" name="login"/>
                            <small class="text-danger">${msg}</small>
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-md-4">
                            <label><fmt:message key="all.label.password"/> </label>
                        </div>
                        <div class="col">
                            <input class="form-control" type="password" name="password"/>
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-md-3">
                            <input type="submit" class="btn btn-primary"
                                   value="<fmt:message key="all.label.login"/> "/>
                        </div>
                        <div class="col-md-3">
                            <input type="button" class="btn btn-info"
                                   value="<fmt:message key="register.button.register"/>"
                                   onclick='location.href="/controller?command=register"'/>
                        </div>
                    </div>
                </form>
            </c:if>
        </div>
    </div>


    <div class="row">

        <div class="col-md-6">
            <br>
            <br>
            <form name="changeCurrent" action="controller" method="post">
                <div class="form-group row">
                    <div class="col-md-6">
                        <label><fmt:message key="main.label.choose_current_city"/></label>
                    </div>
                    <div class="col-md-6">
                        <input class="form-control" type="text" size="30" list="cityList" name="cityInp"
                               value="${sessionScope.currentCity.getName(locale)}">
                    </div>
                    <datalist id="cityList">
                        <c:forEach var="city" items="${applicationScope.cities}">
                            <option>${city.getName(locale)}</option>
                        </c:forEach>
                    </datalist>
                    <input type="hidden" name="command" value="main">
                </div>

                <div class="form-group row">
                    <div class="col-md-6">
                        <label><fmt:message key="main.label.filter_by_region"/></label>
                    </div>
                    <div class="col-md-6">
                        <select class="custom-select" name="region">
                            <option value="0">---<fmt:message key="main.option.all_regions"/>---</option>
                            <c:forEach var="region" items="${applicationScope.regions}">
                                <option value="${region.getId()}" ${region.getId()==sessionScope.regionId?" selected" : ""} >${region.getName(locale)}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="form-group row">
                    <div class="col-md-6">
                        <label><fmt:message key="all.label.weight"/></label>
                    </div>
                    <div class="col-md-6">
                        <input class="form-control" type="number" min="1" name="weight" value="${sessionScope.weight}">
                    </div>
                </div>

                <div class="form-group row">
                    <div class="col-md-6">
                        <label><fmt:message key="all.label.length"/></label>
                    </div>
                    <div class="col-md-6">
                        <input class="form-control" type="number" min="1" name="length" value="${sessionScope.length}">
                    </div>
                </div>

                <div class="form-group row">
                    <div class="col-md-6">
                        <label><fmt:message key="all.label.width"/></label>
                    </div>
                    <div class="col-md-6">
                        <input class="form-control" type="number" min="1" name="width" value="${sessionScope.width}">
                    </div>
                </div>

                <div class="form-group row">
                    <div class="col-md-6">
                        <label><fmt:message key="all.label.height"/></label></td>
                    </div>
                    <div class="col-md-6">
                        <input class="form-control" type="number" min="1" name="height" value="${sessionScope.height}">
                    </div>
                </div>
                <input type="submit" class="btn btn-primary"
                       value="<fmt:message key="main.button.calculate"/>">
            </form>


            <br><fmt:message key="main.label.parcel_weight"/> ${weight} <fmt:message key="all.label.kg"/>
            <br><fmt:message key="main.label.parcel_volume"/> ${volume} <fmt:message key="all.label.dm3"/>
        </div>

        <div class="col-md-6">
            <p><fmt:message key="main.label.delivery_cost_from_city"/>
                <strong> ${sessionScope.currentCity.getName(locale)} </strong> : </p>
            <table class="table table-striped table-bordered table-condensed">
                <thead>
                <tr>
                    <th class="col-md-8" scope="col"><fmt:message key="all.label.city"/>
                        <a style="text-decoration: none" href="<%=Path.COMMAND__MAIN%>&sort=city&order=asc">&#9650;</a>
                        <a style="text-decoration: none" href="<%=Path.COMMAND__MAIN%>&sort=city&order=desc">&#9660;</a>
                    </th>

                    <th scope="col"><fmt:message key="all.label.distance"/>
                        <a style="text-decoration: none" href="<%=Path.COMMAND__MAIN%>&sort=distance&order=asc">&#9650;</a>
                        <a style="text-decoration: none" href="<%=Path.COMMAND__MAIN%>&sort=distance&order=desc">&#9660;</a>
                    </th>
                    <th scope="col"><fmt:message key="all.label.cost"/></th>

                </tr>

                </thead>

                <tbody>
                <c:forEach var="city" items="${cityList}">
                    <tr>
                        <td>
                                ${city.getName(locale)}
                        </td>
                        <td>
                                ${distances.get(city)}
                        </td>
                        <th scope="row">
                                ${costs.get(city)}
                        </th>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <c:set var="current_page" value="<%=Path.COMMAND__MAIN%>"/>
            <%@ include file="/WEB-INF/jspf/pagination.jspf" %>
        </div>
    </div>
</div>


<%@ include file="/WEB-INF/jspf/bottom.jspf" %>

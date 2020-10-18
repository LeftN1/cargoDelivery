<%@ include file="/WEB-INF/jspf/page.jspf" %>
<%@ include file="/WEB-INF/jspf/taglib.jspf" %>

<c:set var="title" value="Edit"/>
<c:set var="current" value="edit"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<div class="container border border-light">
    <h3><fmt:message key="manger.label.edit_delivery"/></h3>
    <form name="order" action="/controller" method="post">
        <input type="hidden" name="command" value="save">
        <input type="hidden" name="delivery_id" value="${delivery_id}">

        <div class="form-group row">
            <div class="col-md-3">
                <fmt:message key="main.label.choose_current_city"/>
            </div>
            <div class="col-md-3">
                <input class="form-control" type="text" size="50" list="cityList" name="origin" value="${origin.getName(locale)}">
            </div>
        </div>
        <div class="form-group row">
            <div class="col-md-3">
                <fmt:message key="main.label.choose_destination_city"/>
            </div>
            <div class="col-md-3">
                <input class="custom-select" type="text" size="50" list="cityList" name="destination" value="${destination.getName(locale)}">
                <datalist id="cityList">
                    <c:forEach var="city" items="${applicationScope.cities}">
                        <option>${city.getName(locale)}</option>
                    </c:forEach>
                </datalist>
            </div>
        </div>
        <div class="form-group row">
            <div class="col-md-3">
                <fmt:message key="all.label.adress"/>
            </div>
            <div class="col-md-3">
                <input class="form-control" type="text" size="50" name="adress" value="${adress}">
            </div>
        </div>
        <div class="form-group row">
            <div class="col-md-3">
                <fmt:message key="all.label.cargo_type"/>
            </div>
            <div class="col-md-3">
                <select class="custom-select" name="type">
                    <c:forEach var="type" items="${applicationScope.cargoTypes}">
                        <option ${cType==type?" selected":""}
                                value="${type.getId()}">${type.getName(locale)}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="form-group row">
            <div class="col-md-3">
                <fmt:message key="all.label.weight"/>
            </div>
            <div class="col-md-3">
                <input class="form-control" type="number" name="weight" min="1" value="${weight}">
            </div>
        </div>
        <div class="form-group row">
            <div class="col-md-3">
                <fmt:message key="all.label.volume"/>
            </div>
            <div class="col-md-3">
                <input class="form-control" type="number" name="volume" min="1" value="${volume}">
            </div>
        </div>
        <div class="form-group row">
            <div class="col-md-3">
                <fmt:message key="all.label.cost"/>
            </div>
            <div class="col-md-3">
                <input class="form-control" type="number" name="cost" min="1" value="${cost}">
            </div>
        </div>
        <div class="form-group row">
            <div class="col-md-3">
                <input type="submit" name="order" value="<fmt:message key="manager.edit.button.save"/>"/>
            </div>
        </div>
    </form>
</div>

</body>
</html>

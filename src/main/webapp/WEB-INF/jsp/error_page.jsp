<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
    <title>Error page</title>
</head>
<body>
<fmt:setLocale value="${sessionScope.locale.getLanguage()}"/>
        <h3>Error page</h3>
        <h3><fmt:message key="error.label.something_went_wrong"/> </h3>
        <p><c:out value="${errorMessage}"/></p>
</body>
</html>

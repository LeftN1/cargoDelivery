<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
    <title>Error page</title>
</head>
<body>
        <h3>Error page</h3>
        <p><c:out value="${errorMessage}"/></p>
</body>
</html>

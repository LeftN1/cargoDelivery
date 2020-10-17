<%@ include file="/WEB-INF/jspf/page.jspf" %>
<%@ include file="/WEB-INF/jspf/taglib.jspf" %>


<c:set var="title" value="Error page" />
<%@ include file="/WEB-INF/jspf/head.jspf" %>


        <h3><fmt:message key="error.label.error_page"/> </h3>
        <h3><fmt:message key="error.label.something_went_wrong"/> </h3>
        <p><c:out value="${errorMessage}"/></p>

<%@ include file="/WEB-INF/jspf/bottom.jspf" %>
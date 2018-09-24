<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri ="http://java.sun.com/jstl/core_rt" %>

<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="<c:url value="/resources/css/stylesheet.css" />">
    <title>Tu Teoria | <c:out value="${area.name}" escapeXml="true"/></title>
</head>

<body class="staticSearchResults">

    <%@ include file="navbar.jsp" %>

    <div class="content">
        <div class="filter-panel">
        </div>

        <div class="search-results">
            <h3><spring:message code="area.message" arguments="${area.name}" htmlEscape="true"/></h3>
            <c:if test="${results.size() == 0}">
                <h4><spring:message code="no.results"/></h4>
            </c:if>
            <%@ include file="courseSearch.jsp" %>
        </div>
    </div>
    </body>

    </html>
</body>
</html>

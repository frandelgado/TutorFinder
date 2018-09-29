<%@ taglib prefix="c" uri ="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value="/resources/css/stylesheet.css" />" rel="stylesheet">

    <title>Tu Teoria | <spring:message code="search.results" /> </title>
</head>

<body class="staticSearchResults">

<%@ include file="navbar.jsp" %>

<div class="content">
    <div class="filter-panel">
    </div>

    <div class="search-results">
        <h3 class="search-data"><c:if test="${search.length() >= 1 }"><spring:message code="search.message" arguments="${search}" htmlEscape="true"/></c:if></h3>
        <c:choose>
            <c:when test="${pagedResults.results.size() == 0}">
                <h1><spring:message code="no.results"/></h1>
            </c:when>
            <c:when test="${type == 'professor'}">
                <%@ include file="professorSearch.jsp" %>
            </c:when>
            <c:when test="${type == 'course'}">
                <%@ include file="courseSearch.jsp" %>
            </c:when>
            <c:when test="${type == 'area'}">
                <%@ include file="areaSearch.jsp" %>
            </c:when>
        </c:choose>

        <div class="paged-result-buttons">
            <c:url value="/searchResults?search=${param.search}&type=${type}&page=${page - 1}" var="previous"/>
            <c:url value="/searchResults?search=${param.search}&type=${type}&page=${page + 1}" var="next"/>

            <c:if test="${page > 1}">
                <a href="${previous}" class="previous round">&#8249;</a>
            </c:if>
            <c:if test="${pagedResults.hasNext}">
                <a href="${next}" class="next round">&#8250;</a>
            </c:if>
        </div>

    </div>
</div>
</body>

</html>
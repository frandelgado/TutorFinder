<%@ taglib prefix="c" uri ="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value = "/resources/css/fonts.css" />" rel='stylesheet'>
    <link rel="stylesheet" href="<c:url value="/resources/css/stylesheet.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/navbar.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/responsive.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/search.css" />">
    <link href="<c:url value="/resources/css/select2.min.css" />" rel="stylesheet" />
    <script src="<c:url value="/resources/js/jquery-3.3.1.min.js" />"></script>
    <script src="<c:url value="/resources/js/select2.min.js" />"></script>
    <script src="<c:url value="/resources/js/dropdownClick.js" />"></script>
    <script src="<c:url value="/resources/js/searchForm.js" />"></script>

    <title>Tu Teoria | <spring:message code="search.results" /> </title>
</head>

<body class="body reservations">


<%@ include file="navbar.jsp" %>

<div class="content">
    <div class="search-results">
        <h3 class="search-data"><spring:message code="yourReservations" htmlEscape="true"/></h3>
            <c:if test="${reservations.size() == 0}">
                <h1><spring:message code="no.reservations"/></h1>
            </c:if>
            <c:forEach var="reservation" items="${reservations}">
                <div class="search-course-result">
                    <a class="conversation-link" href = "<c:url value="/" />"></a>
                    <a class="search-result-img"><img class="search-result-picture" src="<c:url value="data:image/jpeg;base64,${reservation.course.subject.area.image}"/>"/></a>
                    <%--TODO: add buttons--%>
                    <a class="search-result-title">
                        <c:out value="${reservation.course.subject.area.name} - ${reservation.course.subject.name}" escapeXml="true" /></a>
                    <a class="search-result-professor" >
                        <c:out value="${reservation.course.professor.name}" escapeXml="true" /></a>
                    <a class="search-result-specs"><spring:message code="course.specs" arguments="${reservation.course.price}" htmlEscape="true" /></a>
                    <a class="search-result-description"><c:out value="${reservation.course.description}" escapeXml="true" /></a>
                    <a class="search-result-rating"><spring:message code="rating.title" arguments="${reservation.course.price}" htmlEscape="true" /></a>
                </div>
            </c:forEach>
        <div class="paged-result-buttons">
            <c:url value="/reservations?page=${page - 1}" var="previous"/>
            <c:url value="/reservations?page=${page + 1}" var="next"/>

            <c:if test="${page > 1}">
                <a href="${previous}" class="previous round">&#8249;</a>
            </c:if>
            <c:if test="${hasNext}">
                <a href="${next}" class="next round">&#8250;</a>
            </c:if>
        </div>
    </div>
</div>
</body>

</html>
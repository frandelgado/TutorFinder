<%@ taglib prefix="c" uri ="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<c:set var="localeCode" value="es-AR" />

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
                <div class="reservation-course-result">
                    <div class="search-result-img">
                        <div class="button-2 relative" type="submit" value="<spring:message code="reserve"/>">
                            <a class="class-button" href="<c:url value="/createCourse" />"></a>
                            <spring:message code="files"/>
                        </div>
                    </div>
                    <%--TODO: add buttons--%>
                    <a class="search-result-title">
                        <c:out value="${reservation.course.subject.area.name} - ${reservation.course.subject.name}" escapeXml="true" /></a>
                    <a class="search-result-professor" >
                        <c:out value="${reservation.course.professor.name}" escapeXml="true" /></a>
                    <a class="search-result-specs"><spring:message code="course.specs" arguments="${reservation.course.price}" htmlEscape="true" /></a>
                    <a class="search-result-description"><spring:message code="reservation.day" htmlEscape="true" /><joda:format value="${reservation.startTime}" style="L-" locale="${localeCode}"/><br/><spring:message code="reservation.from" htmlEscape="true" /><joda:format value="${reservation.startTime}" style="-S" locale="${localeCode}"/> <spring:message code="reservation.to" htmlEscape="true" /><joda:format value="${reservation.endTime}" style="-S" locale="${localeCode}"/></a>
                    <a class="search-result-status">
                        <c:choose>
                            <c:when test="${reservation.status == 0}">
                                <spring:message code="reservation.accepted" arguments="${reservation.status}" htmlEscape="true" />
                            </c:when>
                            <c:when test="${reservation.status == 1}">
                                <spring:message code="reservation.canceled" arguments="${reservation.status}" htmlEscape="true" />
                            </c:when>
                            <c:otherwise>
                                <spring:message code="reservation.pending" arguments="${reservation.status}" htmlEscape="true" />
                            </c:otherwise>
                        </c:choose>
                    </a>
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
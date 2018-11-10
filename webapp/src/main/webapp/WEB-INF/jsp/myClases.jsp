<%@ taglib prefix="c" uri ="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value = "/resources/css/fonts.css" />" rel='stylesheet'>
    <link href="<c:url value="/resources/css/stylesheet.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/select2.min.css" />" rel="stylesheet" />
    <link rel="stylesheet" href="<c:url value="/resources/css/navbar.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/responsive.css" />">
    <script src="<c:url value="/resources/js/jquery-3.3.1.min.js" />"></script>
    <script src="<c:url value="/resources/js/select2.min.js" />"></script>
    <script src="<c:url value="/resources/js/dropdownClick.js" />"></script>
    <script src="<c:url value="/resources/js/searchForm.js" />"></script>

    <title>Tu Teoria | <spring:message code="search.results" /> </title>
</head>

<body class="body">
<c:url value="/searchResults" var="postPath"/>
<form:form cssClass="staticSearchResults" modelAttribute="searchForm" action="${postPath}" method="post">
<div class="navbar">
    <a href="<c:url value="/" />" class="logo-box">
        <img alt="Tu Teoria" class="logo" src="<c:url value="/resources/images/logo_invert.jpg" />" />
    </a>

    <div class="search-bar">
        <spring:message code="search" var="searchPlaceholder"/>
        <form:input class="search-input" type="search" path="search" name="search" placeholder="${searchPlaceholder}"/>
        <div class="search-dropdown">
            <form:select class="select-search-type" path="type" name="type">
                <form:option value="" selected="true" disabled="true"><spring:message code="search.category" /> </form:option>
                <form:option value="professor"><spring:message code="professor" /> </form:option>
                <form:option value="course"><spring:message code="course.title" /> </form:option>
                <form:option value="area"><spring:message code="area" /></form:option>
            </form:select>
        </div>
        <button type="submit" class="search-button">
            <img class="search-img" src="<c:url value="/resources/images/search.png" />" />
        </button>
    </div>

    <div class="navbar-buttons">
        <c:choose>
            <c:when test="${currentUser != null}">
                <div class="navbar-button dropdown" id="dropdown">
                    <a class="dropdown-button" id="dropdown-button"><c:out value="${currentUser.name} " escapeXml="true"/></a>
                    <div class="dropdown-content" id="dropdown-content">
                        <c:choose>
                            <c:when test="${currentUserIsProfessor == true}">
                                <a href="<c:url value="/Profile" />" class="navbar-button"><spring:message code="profile.title"/></a>
                                <!--<a>Modificar</a>-->
                            </c:when>
                            <c:otherwise>
                                <a href="<c:url value="/registerAsProfessor" />" class="navbar-button"><spring:message code="register.professor"/></a>
                            </c:otherwise>
                        </c:choose>
                        <a href="<c:url value="/Conversations" />" class="navbar-button"><spring:message code="conversations.title"/></a>
                        <a href="<c:url value="/Conversations" />" class="navbar-button"><spring:message code="reservation"/></a>
                        <a href="<c:url value="/Conversations" />" class="navbar-button"><spring:message code="myclsases"/></a>
                        <a href="<c:url value="/logout" />" class="navbar-button"><spring:message code="user.logout"/></a>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <a href="<c:url value="/register" />" class="navbar-button"><spring:message code="register"/></a>
                <a href="<c:url value="/login" />" class="navbar-button"><spring:message code="login"/></a>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<div class="content">
    <div class="search-results">
        <c:if test="${search.length() >= 1 }"><h3 class="search-data"><spring:message code="search.message" arguments="${search}" htmlEscape="true"/></h3></c:if>
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
            <c:url value="/searchResults?search=${param.search}&type=${type}&page=${page - 1}&minPrice=${param.minPrice}&maxPrice=${param.maxPrice}&day=${param.day}&startHour=${param.startHour}&endHour=${param.endHour}" var="previous"/>
            <c:url value="/searchResults?search=${param.search}&type=${type}&page=${page + 1}&minPrice=${param.minPrice}&maxPrice=${param.maxPrice}&day=${param.day}&startHour=${param.startHour}&endHour=${param.endHour}" var="next"/>

            <c:if test="${page > 1}">
                <a href="${previous}" class="previous round">&#8249;</a>
            </c:if>
            <c:if test="${pagedResults.hasNext}">
                <a href="${next}" class="next round">&#8250;</a>
            </c:if>
        </div>
    </div>
</div>
</form:form>
</body>

</html>
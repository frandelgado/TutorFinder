<%@ taglib prefix="c" uri ="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value=" /resources/css/stylesheet.css " />" rel="stylesheet">
    <!--
<link rel="stylesheet" type="text/css" href="./css/stylesheet.css">
<link rel="stylesheet" href="./css/bootstrap.min.css">
-->
    <title>Tu Teoria | <spring:message code="search.results" /> </title>
</head>

<body class="staticSearchResults">

<div class="navbar">
    <a class="logo-box" href="<c:url value=" / "/>">
        <img alt="Tu Teoria" class="logo" src="<c:url value=" /resources/images/logo_invert.jpg " />" />
    </a>

    <div class="search-bar">
        <form role="search" action="<c:url value="/searchResults" />" class="search-bar">
            <input class="search-input" type="search" name="search" placeholder="<spring:message code="search"/>"/>
            <div class="dropdown">
                <select class="select-search-type" name="type">
                    <option value="" selected disabled><spring:message code="search.category" /> </option>
                    <option value="professor"><spring:message code="professor" /> </option>
                    <option value="course"><spring:message code="course.title" /> </option>
                    <option value="area"><spring:message code="area" /></option>
                </select>
            </div>
            <button type="submit" class="search-button">
                <img class="search-img" src="<c:url value="https://static.thenounproject.com/png/337699-200.png" />" />
            </button>
        </form>
    </div>

    <div class="navbar-buttons">
        <a href="<c:url value="/Profile" />" class="navbar-button"><spring:message code="profile.title"/></a>
        <a href="<c:url value="/logout" />" class="navbar-button"><spring:message code="user.logout"/></a>
    </div>
</div>

<div class="content">
    <div class="filter-panel">
        <!--<div class="area-filter">
            <a>Only tutors from your area?</a>
            <input type="checkbox" />
        </div>
        <div class="area-filter">
            <a>Include creepy guys?</a>
            <input type="checkbox" />
        </div>-->
    </div>

    <div class="search-results">
        <h3 class="search-data"><spring:message code="search.message" arguments="${search}" htmlEscape="true"/></h3>
        <c:choose>
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
    </div>
</div>
</body>

</html>
<%@ taglib prefix="c" uri ="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value="/resources/css/stylesheet.css" />" rel="stylesheet">

    <title>Tu Teoria | <spring:message code="search.results" /> </title>
</head>

<body class="body">
<c:url value="/searchResults" var="postPath"/>
<form:form cssClass="staticSearchResults" modelAttribute="searchForm" action="${postPath}" method="post">
<div class="navbar">
    <a href="<c:url value="/" />" class="logo-box">
        <img alt="Tu Teoria" class="logo" src="<c:url value="/resources/images/logo_invert.jpg" />" />
    </a>

    <%--<div class="search-bar">--%>
        <%--<form role="search" action="<c:url value="/searchResults" />" class="search-bar">--%>
            <%--<input class="search-input" type="search" name="search" placeholder="<spring:message code="search"/>"/>--%>
            <%--<div class="dropdown">--%>
                <%--<select class="select-search-type" name="type">--%>
                    <%--<option value="" selected disabled><spring:message code="search.category" /> </option>--%>
                    <%--<option value="professor"><spring:message code="professor" /> </option>--%>
                    <%--<option value="course"><spring:message code="course.title" /> </option>--%>
                    <%--<option value="area"><spring:message code="area" /></option>--%>
                <%--</select>--%>
            <%--</div>--%>
            <%--<button type="submit" class="search-button">--%>
                <%--<img class="search-img" src="<c:url value="https://static.thenounproject.com/png/337699-200.png" />" />--%>
            <%--</button>--%>
        <%--</form>--%>
    <%--</div>--%>

    <div class="search-bar">
        <spring:message code="search" var="searchPlaceholder"/>
        <form:input class="search-input" type="search" path="search" name="search" placeholder="${searchPlaceholder}"/>
        <div class="dropdown">
            <form:select class="select-search-type" path="type" name="type">
                <form:option value="" selected="true" disabled="true"><spring:message code="search.category" /> </form:option>
                <form:option value="professor"><spring:message code="professor" /> </form:option>
                <form:option value="course"><spring:message code="course.title" /> </form:option>
                <form:option value="area"><spring:message code="area" /></form:option>
            </form:select>
        </div>
        <button type="submit" class="search-button">
            <img class="search-img" src="https://static.thenounproject.com/png/337699-200.png" />
        </button>
    </div>

    <div class="navbar-buttons">
        <c:choose>
            <c:when test="${currentUser != null}">
                <a href="<c:url value="/Conversations" />" class="navbar-button"><spring:message code="conversations.title"/></a>
                <c:choose>
                    <c:when test="${currentUserIsProfessor == true}">
                        <a href="<c:url value="/Profile" />" class="navbar-button"><spring:message code="profile.title"/></a>
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value="/registerAsProfessor" />" class="navbar-button"><spring:message code="register.professor"/></a>
                    </c:otherwise>
                </c:choose>
                <a href="<c:url value="/logout" />" class="navbar-button"><spring:message code="user.logout"/></a>
            </c:when>
            <c:otherwise>
                <a href="<c:url value="/register" />" class="navbar-button"><spring:message code="register"/></a>
                <a href="<c:url value="/login" />" class="navbar-button"><spring:message code="login"/></a>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<div class="content">
    <div class="filter-panel">

        <h2>Filtros</h2>
        <div>
            <h3><spring:message code="search.dayHeader"/></h3>
            <div>
                <form:select cssClass="select-subject" path="day">
                    <form:option selected="selected" value=""><spring:message code="select.day"/></form:option>
                    <form:option value="1"><spring:message code ="day.monday"/></form:option>
                    <form:option value="2"><spring:message code ="day.tuesday"/></form:option>
                    <form:option value="3"><spring:message code ="day.wednesday"/></form:option>
                    <form:option value="4"><spring:message code ="day.thursday"/></form:option>
                    <form:option value="5"><spring:message code ="day.friday"/></form:option>
                    <form:option value="6"><spring:message code ="day.saturday"/></form:option>
                    <form:option value="7"><spring:message code ="day.sunday"/></form:option>
                </form:select>
                <form:errors cssClass="error-text" path="day" element="p"/>
            </div>
            <h3><spring:message code="search.hourHeader"/></h3>
            <div>
                <form:label cssClass="label" path="startHour"><spring:message code="schedule.form.startHour"/></form:label>
                <form:select cssClass="select-subject" path="startHour">
                    <form:option selected="selected" value=""><spring:message code="select.startHour"/></form:option>
                    <c:forEach var="hour" begin="0" end="23" >
                        <form:option value="${hour}">${hour}:00</form:option>
                    </c:forEach>
                </form:select>
                <form:errors cssClass="error-text" path="startHour" element="p"/>
            </div>
            <div>
                <form:label cssClass="label" path="endHour"><spring:message code="schedule.form.endHour"/></form:label>
                <form:select cssClass="select-subject" path="endHour">
                    <form:option selected="selected" value=""><spring:message code="select.endHour"/></form:option>
                    <c:forEach var="hour" begin="1" end="24" >
                        <form:option value="${hour}">${hour}:00</form:option>
                    </c:forEach>
                </form:select>
                <form:errors cssClass="error-text" path="endHour" element="p"/>
            </div>
        </div>
        <div>
            <h3><spring:message code="search.priceHeader"/></h3>
            <div>
                <form:label path="minPrice"><spring:message code="search.label.minPrice"/></form:label>
                <form:input cssClass="input-request-filter" type="number" step="0.01" path="minPrice"/>
                <form:errors cssClass="error-text" path="minPrice" element="p"/>
            </div>
            <div>
                <form:label path="maxPrice"><spring:message code="search.label.maxPrice"/></form:label>
                <form:input cssClass="input-request-filter" type="number" step="0.01" path="maxPrice"/>
                <form:errors cssClass="error-text" path="maxPrice" element="p"/>
            </div>
        </div>
        <button class="button-2 center" type="submit" class="search-button">
            <spring:message code="button.filter"/>
        </button>
    </div>
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
            <c:url value="/searchResults?search=${param.search}&type=${type}&page=${page - 1}
            &minPrice=${param.minPrice}&maxPrice=${param.maxPrice}&day=${param.day}
            &startHour=${param.startHour}&endHour=${param.endHour}" var="previous"/>
            <c:url value="/searchResults?search=${param.search}&type=${type}&page=${page + 1}
            &minPrice=${param.minPrice}&maxPrice=${param.maxPrice}&day=${param.day}
            &startHour=${param.startHour}&endHour=${param.endHour}" var="next"/>

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
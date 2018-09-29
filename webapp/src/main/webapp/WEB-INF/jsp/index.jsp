<%@ taglib prefix="c" uri ="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value="/resources/css/stylesheet.css" />" rel="stylesheet">
    <script src="<c:url value="/resources/js/searchForm.js" />"></script>
    <title>Tu Teoria | <spring:message code="index.title"/></title>
</head>

<body class="staticHome">
<div class="navbar">
    <a href="<c:url value="/" />" class="logo-box">
        <img alt="Tu Teoria" class="logo" src="<c:url value="/resources/images/logo_invert.jpg" />" />
    </a>

    <div class="search-bar"></div>

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
    <%@ include file="statusMessages.jsp"%>
    <c:url value="/searchResults" var="postPath"/>
    <form:form modelAttribute="SearchForm" action="${postPath}" method="post">
    <div class="search-box">
        <img alt="Tu Teoria" class="search-logo" src="<c:url value="/resources/images/logo.png" />" />
        <div class="search-bar">
            <spring:message code="search" var="searchPlaceholder"/>
            <form:input class="search-input" type="search" path="search" placeholder="${searchPlaceholder}"/>
            <div class="dropdown">
                <form:select cssClass="select-searchbar-type" path="type" name="type">
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
    </div>
    <div class="filter-panel">
        <div>
            <h1><spring:message code="search.timeHeader"/></h1>
            <div>
                <form:label cssClass="label" path="day"><spring:message code="schedule.form.day"/></form:label>
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
            <h1><spring:message code="search.priceHeader"/></h1>
            <div>
                <form:label path="minPrice"><spring:message code="search.label.minPrice"/></form:label>
                <form:input cssClass="input-request" type="number" step="0.01" path="minPrice"/>
                <form:errors cssClass="error-text" path="minPrice" element="p"/>
            </div>
            <div>
                <form:label path="maxPrice"><spring:message code="search.label.maxPrice"/></form:label>
                <form:input cssClass="input-request" type="number" step="0.01" path="maxPrice"/>
                <form:errors cssClass="error-text" path="maxPrice" element="p"/>
            </div>
        </div>
    </div>
    </form:form>
</div>
<div class="footer">
</div>
</body>

</html>
<%@ taglib prefix="c" uri ="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value="/resources/css/stylesheet.css" />" rel="stylesheet">
    <title>Tu Teoria | <spring:message code="profile.title" /> </title>
</head>

<body class="staticProfile">

<%@ include file="navbar.jsp" %>

<div class="content">
    <%--<img class="profile-picture" alt="Profile picture" src="<c:url value="/resources/images/logo_invert.jpg" />" />--%>
    <h1 class="profile-name"><c:out value="${professor.name} ${professor.lastname}" escapeXml="true"/></h1>
    <h3 class="profile-description"><c:out value="${professor.description}" escapeXml="true"/></h3>
    <div class="classes">
        <div class="class add-class">
            <a class="class-button" href="/createCourse"></a>
            +
        </div>
        <c:forEach var="course" items="${courses}">
            <div class="class">
                <a class="class-button" href="<c:url value="/Course/?professor=${course.professor.id}&subject=${course.subject.id}" />"></a>
                <div class="class-title"><c:out value="${course.subject.name}" escapeXml="true"/></div>
                <div class="class-description"><c:out value="${course.description}" escapeXml="true"/></div>
            </div>
        </c:forEach>
    </div>
    <%-- FORM INCOMPLETO --%>
    <%--<form:form cssClass="form" modelAttribute="newScheduleForm" action="${}" method="post">
        <div>
            <form:label cssClass="label" path="add_day"><spring:message code="profile.add_day"/></form:label>
            <form:input cssClass="input-request" type="text" path="add_day"/>
            <form:errors cssClass="formError" path="add_day" element="p"/>
        </div>
        <div>
            <form:label cssClass="label" path="add_time"><spring:message code="profile.add_time"/></form:label>
            <form:input cssClass="input-request" type="number" step="15" path="add_time"/>
            <form:errors cssClass="formError" path="add_time" element="p"/>
        </div>
        <div>
            <form:label cssClass="label" path="add_duration"><spring:message code="profile.add_duration"/></form:label>
            <form:input cssClass="input-request" type="number" step="1" path="add_duration"/>
            <form:errors cssClass="formError" path="add_duration" element="p"/>
        </div>
        <div class="button-container">
            <input class="button-2" type="submit" value="<spring:message code="profile.add_schedule"/>"/>
        </div>
    </form:form>
    --%>
    <div class="schedule">
        <a class="schedule-divider"><spring:message code="schedule_divider"/></a>
        <a class="monday"><spring:message code="day.monday"/></a>
        <a class="tuesday"><spring:message code="day.tuesday"/></a>
        <a class="wednesday"><spring:message code="day.wednesday"/></a>
        <a class="thursday"><spring:message code="day.thursday"/></a>
        <a class="friday"><spring:message code="day.friday"/></a>
        <a class="saturday"><spring:message code="day.saturday"/></a>
        <a class="sunday"><spring:message code="day.sunday"/></a>
    </div>
</div>
<div class="footer">
</div>
</body>

</html>
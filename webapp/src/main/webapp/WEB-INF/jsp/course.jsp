<%@ taglib prefix="c" uri ="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value = "/resources/css/fonts.css" />" rel='stylesheet'>
    <link rel="stylesheet" href="<c:url value="/resources/css/stylesheet.css" />">
    <script src="<c:url value="/resources/js/jquery-3.3.1.min.js" />"></script>
    <script src="<c:url value="/resources/js/dropdownClick.js" />"></script>
    <title>Tu Teoria | <spring:message code="course.title"/></title>
</head>

<body class="staticClass">

<%@ include file="navbar.jsp" %>

<div class="content course">
    <div class="class-profile">
        <h1 class="title center-text"><c:out value="${course.subject.area.name} - ${course.subject.name}" escapeXml="true"/></h1>
        <h4 class="description center-text"><c:out value="${course.description}" escapeXml="true"/></h4>
        <div class="profile">
            <a class="profile-picture" href="<c:url value="/Professor/${course.professor.id}"/>">
                <img class="profile-picture" alt="Profile picture" src="<c:url value="data:image/jpeg;base64,${course.professor.picture}" />"/>
            </a>
            <div class="profile-name"><c:out value="${course.professor.name}" escapeXml="true"/> <c:out value = "${course.professor.lastname}" escapeXml="true"/></div>
            <div class="profile-description"><c:out value="${course.professor.description}" escapeXml="true"/></div>
        </div>
    </div>
    <div class="contact">
        <div class="button-container">
            <h2 class="label"><spring:message code="course.contact"/></h2>
        </div>
    <c:url value="/sendMessage" var="postPath"/>
    <form:form cssClass="form" modelAttribute="messageForm" action="${postPath}" method="post">
        <form:hidden path="professorId" />
        <form:hidden path="subjectId" />
        <div>
            <form:label cssClass="label" path="body"><spring:message code="contact.body"/></form:label>
            <form:textarea cssClass="input-request chat-box" type="text" path="body" rows="5" cols="5"/>
            <form:errors cssClass="error-text" path="body" element="p"/>
        </div>
        <div>
            <form:hidden path="extraMessage"/>
            <form:errors cssClass="success-text" path="extraMessage" element="p"/>
        </div>
        <div class="button-container">
            <input class="button-2" type="submit" value="<spring:message code="send"/>"/>
        </div>
    </form:form>
    </div>

    <div class="schedule">
        <h2><spring:message code="schedule.title"/></h2>
        <input class="button-2" type="button" value="<spring:message code="reservations.action"/>"/>
        <p><spring:message code="schedule.description" /></p>
        <%@ include file="schedule.jsp"%>
    </div>
</div>
<div class="footer">
</div>
</body>

</html>
<%@ taglib prefix="c" uri ="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="<c:url value="/resources/css/stylesheet.css" />">
    <title>Tu Teoria | <spring:message code="course.title"/></title>
</head>

<body class="staticClass">

<%@ include file="navbar.jsp" %>

<div class="content">
    <h1 class="title"><c:out value="${course.subject.area.name} - ${course.subject.name}" escapeXml="true"/></h1>
    <h4 class="description">${course.description}</h4>
    <hr/>
    <div class="profile">
        <a href="<c:url value="/Professor/${course.professor.id}"/>">
            <img class="profile-picture" alt="Profile picture" src="<c:url value="/resources/images/logo_invert.jpg" />"/>
        </a>
        <div class="profile-name">${course.professor.name}</div>
        <div class="profile-description">${course.professor.description}</div>
    </div>
    <hr/>
    <button class="contact-button">Contactar</button>
    <div class="button-container">
        <h2 class="label">Register</h2>
    </div>
    </p>
    <c:url value="/create" var="postPath"/>
    <form:form cssClass="form" modelAttribute="contactForm" action="${postPath}" method="post">
        <div>
            <form:label cssClass="label" path="name"><spring:message code="contact.subject"/></form:label>
            <form:input cssClass="input-request" type="text" path="subject"/>
            <form:errors cssClass="formError" path="name" element="p"/>
        </div>
        <div>
            <form:label cssClass="label" path="lastname"><spring:message code="contact.body"/></form:label>
            <form:input cssClass="input-request" type="text" path="body"/>
            <form:errors cssClass="formError" path="lastname" element="p"/>
        </div>
        <div class="button-container">
            <input class="button-2" type="submit" value="<spring:message code="register"/>"/>
        </div>
    </form:form>
</div>
<div class="footer">
</div>
</body>

</html>
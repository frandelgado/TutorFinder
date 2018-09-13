<%@ taglib prefix="c" uri ="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%><html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="<c:url value="/resources/css/stylesheet.css" />">
    <title>Tu Teoria | Class</title>
</head>

<body class="staticClass">

<div class="navbar">
    <a href="<c:url value="/" />" class="logo-box">
        <img alt="Tu Teoria" class="logo" src="<c:url value="/resources/images/logo_invert.jpg" />" />
    </a>
    <!-- 
    <div class="navbar-buttons">
        <button class="navbar-button"><spring:message code="register"/></button>
        <button class="navbar-button"><spring:message code="login"/></button>
    </div>
    -->
</div>

<div class="content">
    <h1 class="title"><c:out value="${course.subject.name} - ${course.subject.name}" escapeXml="true"/></h1>
    <h4 class="description">${course.description}</h4>
    <hr/>
    <div class="profile">
        <a href="perfilDelTipo">
            <img class="profile-picture" alt="Profile picture" src="<c:url value="/resources/images/logo_invert.jpg" />"/>
        </a>
        <div class="profile-name">${course.professor.name}</div>
        <div class="profile-description">${course.professor.description}</div>
    </div>
    <hr/>
    <button class="contact-button">Contactar</button>
</div>
<div class="footer">
</div>
</body>

</html>
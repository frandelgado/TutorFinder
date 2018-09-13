<%@ taglib prefix="c" uri ="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%><html>

<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value="/resources/css/stylesheet.css" />" rel="stylesheet">
    <title>Tu Teoria | <spring:message code="index.title"/></title>
</head>

<body class="staticHome">

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
    <div class="search-box">
        <img alt="Tu Teoria" class="search-logo" src="<c:url value="/resources/images/logo.png" />" />
        <div class="search-bar">
            <form role="search" action="<c:url value="/searchResults" />">
                <input class="search-input" type="search" name="search" placeholder="<spring:message code="search"/>"/>
                <button type="submit" class="search-button">
                    <img class="search-img" src="https://static.thenounproject.com/png/337699-200.png" />
                </button>
            </form>
        </div>
    </div>
</div>
<div class="footer">
</div>
</body>

</html>
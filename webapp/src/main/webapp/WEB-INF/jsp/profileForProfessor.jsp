<%@ taglib prefix="c" uri ="http://java.sun.com/jstl/core_rt" %>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value="/resources/css/stylesheet.css" />" rel="stylesheet">
    <title>Tu Teoria | Profile</title>
</head>

<body class="staticProfile">

<div class="navbar">
    <a href="<c:url value="/" />" class="logo-box">
        <img alt="Tu Teoria" class="logo" src="<c:url value="/resources/images/logo_invert.jpg" />" />
    </a>
    <!--
    <div class="navbar-buttons">
        <button class="navbar-button">Register</button>
        <button class="navbar-button">Log In</button>
    </div>
    -->
</div>

<div class="content">
    <%--<img class="profile-picture" alt="Profile picture" src="<c:url value="/resources/images/logo_invert.jpg" />" />--%>
    <h1 class="profile-name">Raul</h1>
    <h3 class="profile-description">Ingeniero Naval - Master en Fisica Aplicada</h3>
    <div class="classes">
        <div class="class add-class">
            <a class="class-button" href="linkALaClase"></a>
            +
        </div>
        <div class="class">
            <a class="class-button" href="linkALaClase"></a>
            <div class="class-title">Matematica</div>
            <div class="class-description">La clase de Matematica de Raul esta orientada a alumnos de primaria aprendiendo
                supersimetria
            </div>
        </div>
    </div>
</div>
<div class="footer">
</div>
</body>

</html>
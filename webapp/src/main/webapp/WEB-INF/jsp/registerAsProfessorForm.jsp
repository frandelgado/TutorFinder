<%@ taglib prefix="c" uri ="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href='https://fonts.googleapis.com/css?family=Lato' rel='stylesheet'>
    <link rel="stylesheet" href="<c:url value="/resources/css/stylesheet.css" />">
    <script src="<c:url value="/resources/js/jquery-3.3.1.min.js"/>" ></script>
    <script src="<c:url value="/resources/js/registerAsProfessor.js"/>" ></script>
    <title>Tu Teoria | <spring:message code="register"/></title>
</head>

<body class="register">

<div class="navbar">
    <a href="<c:url value="/" />" class="logo-box">
        <img alt="Tu Teoria" class="logo" src="<c:url value="/resources/images/logo_invert.jpg" />" />
    </a>
</div>

<div class="content">
    <div class="button-container">
        <h2 class="label"><spring:message code="register.professor" /> </h2>
    </div>
    <c:url value="/registerAsProfessor" var="postPath" />
    <form:form cssClass="form" modelAttribute="registerAsProfessorForm" action="${postPath}" enctype="multipart/form-data" method="post">
        <div>
            <form:label cssClass="label" path="description"><spring:message code="register.description"/></form:label>
            <form:textarea cssClass="input-request" type="text" path="description" rows="5" cols="5"/>
            <form:errors cssClass="error-text" path="description" element="p"/>
        </div>
        <div id="pictureDiv">
            <form:label cssClass="label" path="picture"><spring:message code="register.picture"/></form:label>
            <form:input cssClass="input-request" type="file" path="picture" accept="image/jpeg, image/png" id="picture"/>
            <form:errors cssClass="error-text" path="picture" element="p"/>
            <p id="pictureError" class="error-text"><spring:message code="FileSize.registerAsProfessorForm.picture" /></p>
        </div>

        <div class="button-container">
            <input class="button-2" id="registerAsProfessorSubmit" type="submit" value="<spring:message code="register"/>"/>
        </div>
    </form:form>
</div>
<div class="footer">
</div>
</body>

</html>
<%@ taglib prefix="c" uri ="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="<c:url value="/resources/css/stylesheet.css" />">
    <title>Tu Teoria | <spring:message code="register"/></title>
</head>

<body class="register">

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
    <div class="button-container">
        <h2 class="label"><spring:message code="register" /> </h2>
    </div>
    <c:url value="/create" var="postPath"/>
    <form:form cssClass="form" modelAttribute="registerForm" action="${postPath}" method="post">
        <div>
            <form:label cssClass="label" path="name"><spring:message code="user.name"/></form:label>
            <form:input cssClass="input-request" type="text" path="name"/>
            <form:errors cssClass="formError" path="name" element="p"/>
        </div>
        <div>
            <form:label cssClass="label" path="lastname"><spring:message code="user.lastname"/></form:label>
            <form:input cssClass="input-request" type="text" path="lastname"/>
            <form:errors cssClass="formError" path="lastname" element="p"/>
        </div>
        <div>
            <form:label cssClass="label" path="email"><spring:message code="user.email"/></form:label>
            <form:input cssClass="input-request" type="text" path="email"/>
            <form:errors cssClass="formError" path="email" element="p"/>
        </div>
        <div>
            <form:label cssClass="label" path="username"><spring:message code="user.username"/></form:label>
            <form:input cssClass="input-request" type="text" path="username"/>
            <form:errors cssClass="formError" path="username" element="p"/>
        </div>
        <div>
            <form:label cssClass="label" path="password"><spring:message code="user.password"/></form:label>
            <form:input cssClass="input-request" type="password" path="password"/>
            <form:errors cssClass="formError" path="password" element="p"/>
        </div>
        <div>
            <form:label cssClass="label" path="repeatPassword"><spring:message code="user.repeat_password"/></form:label>
            <form:input cssClass="input-request" type="password" path="repeatPassword"/>
            <form:errors cssClass="formError" path="repeatPassword" element="p"/>
        </div>
        <div>
            <form:label cssClass="label" path="description"><spring:message code="register.description"/></form:label>
            <form:textarea cssClass="input-request" type="text" path="description"/>
            <form:errors cssClass="formError" path="description" element="p"/>
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
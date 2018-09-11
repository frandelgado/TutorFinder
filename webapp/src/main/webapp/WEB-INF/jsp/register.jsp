<%@ taglib prefix="c" uri ="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="<c:url value="/resources/css/stylesheet.css" />">
    <title>Tu Teoria | Class</title>
</head>

<body class="register">

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
    <c:url value="/register" var="postPath"/>
    <h2>Register</h2>
    <form:form cssClass="register-form" modelAttribute="registerForm" action="${postPath}" method="post">
        <div>
            <form:label path="name">Username: </form:label>
            <form:input type="text" path="name"/>
            <form:errors cssClass="formError" path="name" element="p"/>
        </div>
        <div>
            <form:label path="lastname">Lastname: </form:label>
            <form:input type="text" path="lastname"/>
            <form:errors cssClass="formError" path="lastname" element="p"/>
        </div>
        <div>
            <form:label path="email">Email: </form:label>
            <form:input type="text" path="email"/>
            <form:errors cssClass="formError" path="email" element="p"/>
        </div>
        <div>
            <form:label path="username">Username: </form:label>
            <form:input type="text" path="username"/>
            <form:errors cssClass="formError" path="username" element="p"/>
        </div>
        <div>
            <form:label path="password">Password: </form:label>
            <form:input type="password" path="password"/>
            <form:errors cssClass="formError" path="password" element="p"/>
        </div>
        <div>
            <form:label path="repeatPassword">Repeat password: </form:label>
            <form:input type="password" path="repeatPassword"/>
            <form:errors cssClass="formError" path="repeatPassword" element="p"/>
        </div>
        <div>
            <form:label path="description">Description: </form:label>
            <form:input type="text" path="description"/>
            <form:errors cssClass="formError" path="description" element="p"/>
        </div>
        <div>
            <input type="submit" value="Register"/>
        </div>
    </form:form>
</div>
<div class="footer">
</div>
</body>

</html>
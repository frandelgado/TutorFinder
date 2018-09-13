<%@ taglib prefix="c" uri ="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="<c:url value="/resources/css/stylesheet.css" />">
    <title>Tu Teoria | Login</title>
</head>

<body>
    <div class="content">
        <div class="button-container">
          <h1 class="label"><spring:message code="login"/></h1>
        </div>
        <c:url value="/login" var="loginUrl" />
        <form class="form" action="${loginUrl}" method="post" enctype="application/x-www-form-urlencoded">
            <div>
                <label class="label" for="username"><spring:message code="user.username"/></label>
                <input class="input-request" id="username" name="username" type="text"/>
            </div>
            <div>
                <label class="label" for="password"><spring:message code="user.password"/></label>
                <input class="input-request" id="password" name="password" type="password"/>
            </div>
            <div>
                <label class="label" ><input name="rememberme" type="checkbox"/><spring:message code="remember_me"/></label>
            </div>
            <div class="button-container">
                <input class="button-2" type="submit" value="<spring:message code="login"/>" />
            </div>
        </form>
    </div>
</body>

</html>


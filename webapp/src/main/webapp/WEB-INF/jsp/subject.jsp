<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri ="http://java.sun.com/jstl/core_rt" %>

<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="<c:url value="/resources/css/stylesheet.css" />">
    <title>Tu Teoria | <c:out value="${subject.name}" escapeXml="true"/></title>
</head>
<body>
<p><spring:message code="subject.message" arguments="${subject.name}" htmlEscape="true"/></p>
</body>
</html>

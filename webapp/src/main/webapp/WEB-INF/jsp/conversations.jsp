<%@ taglib prefix="c" uri ="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="<c:url value="/resources/css/stylesheet.css" />">
    <title>Tu Teoria | <spring:message code="conversations.title"/></title>
</head>

<body>

<%@ include file="navbar.jsp" %>

<div class="content">
    <c:forEach var="conversation" items="${conversations}">
        <div>
            <a href = "<c:url value="Conversations/?id=${conversation.id}" />"><c:out value="${conversation.subject.name}" /></a>
            <h1><c:out value="${conversation.professor.name} - ${conversation.user.name}" /></h1>
        </div>
    </c:forEach>
</div>
</body>

</html>
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

<body class="conversations">

<%@ include file="navbar.jsp" %>

<div class="content">
    <p class="help-message"><spring:message code="conversations.help"/></p>
    <div class="inbox-message">
        <hr>
        <p><spring:message code="conversations.inbox"/></p>
        <hr>
    </div>
    <c:forEach var="conversation" items="${conversations}">
        <div class="chat">
            <a class="conversation-link" href = "<c:url value="/Conversation?id=${conversation.id}" />"/>
            <a class="conversation-subject-name"><c:out value="${conversation.subject.name}" /></a>
            <h2 class="conversation-participants">
                <c:out value="${conversation.professor.name} - ${conversation.user.name}" />
            </h2>
            <h6 class="conversation-last-time"><c:out value="${conversation.latestMessage.toDateTime()}" /></h6>
        </div>
    </c:forEach>
</div>
</body>

</html>
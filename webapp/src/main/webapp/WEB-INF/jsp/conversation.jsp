<%@ taglib prefix="c" uri ="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="<c:url value="/resources/css/stylesheet.css" />">
    <title>Tu Teoria | <spring:message code="conversations.title"/></title>
</head>

<body class="conversation">

<%@ include file="navbar.jsp" %>

<div class="content">

    <div class="conversation-messages">
        <c:forEach var="message" items="${conversation.messages}">
            <div class="message">
                <h5 class="message-text"><c:out value="${message.sender.name}: ${message.text}" /></h5>
                <h6 class="message-time"><c:out value="A las: ${message.created.toDateTime()}" /></h6>
            </div>
        </c:forEach>
    </div>

    <div class="form-container">
        <c:url value="/Conversation" var="postPath"/>
        <form:form cssClass="form" modelAttribute="messageForm" action="${postPath}" method="post">
            <form:hidden path="conversationId" />
            <div>
                <form:label cssClass="label" path="body"><spring:message code="contact.body"/></form:label>
                <form:input cssClass="input-request" type="text" path="body"/>
                <form:errors cssClass="error-text" path="body" element="p"/>
            </div>
            <div>
                <form:hidden path="extraMessage" />
                <form:errors cssClass="error-text" path="extraMessage" element="p"/>
            </div>
            <div class="button-container">
                <input class="button-2" type="submit" value="<spring:message code="Contact"/>"/>
            </div>
        </form:form>
    </div>
</div>
</body>

</html>
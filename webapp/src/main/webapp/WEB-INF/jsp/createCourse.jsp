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

<%@ include file="navbar.jsp" %>

<div class="content">
    <div class="button-container">
        <h2 class="label"><spring:message code="course.create"></spring:message></h2>
    </div>
    <c:url value="/createCourse" var="postPath"/>
    <form:form cssClass="form" modelAttribute="CourseForm" action="${postPath}" method="post">
        <div>
            <form:label cssClass="label" path="subjectId"><spring:message code="course.subject"/></form:label>
            <form:select path="subjectId">
                <form:option selected="selected" value="">------</form:option>
                <c:forEach var="subject" items="${subjects}">
                    <form:option value="${subject.id}">${subject.name}</form:option>
                </c:forEach>
            </form:select>
            <form:errors cssClass="formError" path="subjectId" element="p"/>
        </div>
        <div>
            <form:label cssClass="label" path="description"><spring:message code="register.description"/></form:label>
            <form:input cssClass="input-request" type="textarea" path="description"/>
            <form:errors cssClass="formError" path="description" element="p"/>
        </div>
        <div>
            <form:label cssClass="label" path="price"><spring:message code="course.price"/></form:label>
            <form:input cssClass="input-request" type="number" step="0.01" path="price"/>
            <form:errors cssClass="formError" path="price" element="p"/>
        </div>
        <div class="button-container">
            <input class="button-2" type="submit" value="<spring:message code="create"/>"/>
        </div>
    </form:form>
</div>
<div class="footer">
</div>
</body>

</html>
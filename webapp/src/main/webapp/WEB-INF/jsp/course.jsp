<%@ taglib prefix="c" uri ="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page isELIgnored="false" %>

<html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="<c:url value="/resources/css/stylesheet.css" />">
    <title>Tu Teoria | <spring:message code="course.title"/></title>
</head>

<body class="staticClass">

<%@ include file="navbar.jsp" %>

<div class="content">
    <h1 class="title"><c:out value="${course.subject.area.name} - ${course.subject.name}" escapeXml="true"/></h1>
    <h4 class="description">${course.description}</h4>
    <hr/>
    <div class="profile">
        <a href="<c:url value="/Professor/${course.professor.id}"/>">
            <img class="profile-picture" alt="Profile picture" src="<c:url value="/resources/images/logo_invert.jpg" />"/>
        </a>
        <div class="profile-name">${course.professor.name}</div>
        <div class="profile-description">${course.professor.description}</div>
    </div>
    <hr/>
    <div class="button-container">
        <h2 class="label"><spring:message code="course.contact"/></h2>
    </div>
    </p>
    <c:url value="/sendMessage" var="postPath"/>
    <form:form cssClass="form" modelAttribute="messageForm" action="${postPath}" method="post">
        <form:hidden path="professorId" />
        <form:hidden path="subjectId" />
        <div>
            <form:label cssClass="label" path="body"><spring:message code="contact.body"/></form:label>
            <form:textarea cssClass="input-request" type="text" path="body" rows="5" cols="5"/>
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

    <div class="schedule">
        <table style-="width:100%">
            <tr>
                <th>
                    <a class="schedule-divider"><spring:message code="schedule_divider"/></a>
                </th>
                <th>
                    <a class="monday"><spring:message code="day.monday"/></a>
                </th>
                <th>
                    <a class="tuesday"><spring:message code="day.tuesday"/></a>
                </th>
                <th>
                    <a class="wednesday"><spring:message code="day.wednesday"/></a>
                </th>
                <th>
                    <a class="thursday"><spring:message code="day.thursday"/></a>
                </th>
                <th>
                    <a class="friday"><spring:message code="day.friday"/></a>
                </th>
                <th>
                    <a class="saturday"><spring:message code="day.saturday"/></a>
                </th>
                <th>
                    <a class="sunday"><spring:message code="day.sunday"/></a>
                </th>
            </tr>
            <c:forEach var="i" begin="1" end="24">
                <tr>
                    <td>${i}:00</td>
                    <td>
                        <c:if test="${schedule.monday.contains(i)}">
                            <a>Tickerino</a>
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${schedule.tuesday.contains(i)}">
                            <a>Tickerino</a>
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${schedule.wednesday.contains(i)}">
                            <a>Tickerino</a>
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${schedule.thursday.contains(i)}">
                            <a>Tickerino</a>
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${schedule.friday.contains(i)}">
                            <a>Tickerino</a>
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${schedule.saturday.contains(i)}">
                            <a>Tickerino</a>
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${schedule.sunday.contains(i)}">
                            <a>Tickerino</a>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>
<div class="footer">
</div>
</body>

</html>
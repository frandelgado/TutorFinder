<%@ taglib prefix="c" uri ="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page isELIgnored="false" %>

<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value="/resources/css/stylesheet.css" />" rel="stylesheet">
    <title>Tu Teoria | <spring:message code="profile.title" /> </title>
</head>

<body class="staticProfile">

<%@ include file="navbar.jsp" %>

<div class="content">
    <%--<img class="profile-picture" alt="Profile picture" src="<c:url value="/resources/images/logo_invert.jpg" />" />--%>
    <h1 class="profile-name"><c:out value="${professor.name} ${professor.lastname}" escapeXml="true"/></h1>
    <h3 class="profile-description"><c:out value="${professor.description}" escapeXml="true"/></h3>
    <div class="classes">
        <div class="class add-class">
            <a class="class-button" href="<c:url value="/createCourse" />"></a>
            +
        </div>
        <c:forEach var="course" items="${courses}">
            <div class="class">
                <a class="class-button" href="<c:url value="/Course/?professor=${course.professor.id}&subject=${course.subject.id}" />"></a>
                <div class="class-title"><c:out value="${course.subject.name}" escapeXml="true"/></div>
                <div class="class-description"><c:out value="${course.description}" escapeXml="true"/></div>
            </div>
        </c:forEach>
    </div>
    <%-- FORM INCOMPLETO --%>
    <%--<form:form cssClass="form" modelAttribute="newScheduleForm" action="${}" method="post">
        <div>
            <form:label cssClass="label" path="add_day"><spring:message code="profile.add_day"/></form:label>
            <form:input cssClass="input-request" type="text" path="add_day"/>
            <form:errors cssClass="formError" path="add_day" element="p"/>
        </div>
        <div>
            <form:label cssClass="label" path="add_time"><spring:message code="profile.add_time"/></form:label>
            <form:input cssClass="input-request" type="number" step="15" path="add_time"/>
            <form:errors cssClass="formError" path="add_time" element="p"/>
        </div>
        <div>
            <form:label cssClass="label" path="add_duration"><spring:message code="profile.add_duration"/></form:label>
            <form:input cssClass="input-request" type="number" step="1" path="add_duration"/>
            <form:errors cssClass="formError" path="add_duration" element="p"/>
        </div>
        <div class="button-container">
            <input class="button-2" type="submit" value="<spring:message code="profile.add_schedule"/>"/>
        </div>
    </form:form>
    --%>
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
        <div>
            <h1>Add available time</h1>
            <c:url value="/CreateTimeSlot" var="postPath"/>
            <form:form cssClass="form" modelAttribute="ScheduleForm" action="${postPath}" method="post">
                <div>
                    <form:label cssClass="label" path="day"><spring:message code="schedule.form.day"/></form:label>
                    <form:select cssClass="select-subject" path="day">
                        <form:option selected="selected" value=""><spring:message code="select.day"/></form:option>
                        <form:option value="1"><spring:message code ="day.monday"/></form:option>
                        <form:option value="2"><spring:message code ="day.tuesday"/></form:option>
                        <form:option value="3"><spring:message code ="day.wednesday"/></form:option>
                        <form:option value="4"><spring:message code ="day.thursday"/></form:option>
                        <form:option value="5"><spring:message code ="day.friday"/></form:option>
                        <form:option value="6"><spring:message code ="day.saturday"/></form:option>
                        <form:option value="7"><spring:message code ="day.sunday"/></form:option>
                    </form:select>
                    <form:errors cssClass="error-text" path="day" element="p"/>
                </div>
                <div>
                    <form:label cssClass="label" path="startHour"><spring:message code="schedule.form.startHour"/></form:label>
                    <form:select cssClass="select-subject" path="startHour">
                        <form:option selected="selected" value=""><spring:message code="select.startHour"/></form:option>
                        <c:forEach var="hour" begin="0" end="23" >
                            <form:option value="${hour}">${hour}:00</form:option>
                        </c:forEach>
                    </form:select>
                    <form:errors cssClass="error-text" path="startHour" element="p"/>
                </div>
                <div>
                    <form:label cssClass="label" path="endHour"><spring:message code="schedule.form.endHour"/></form:label>
                    <form:select cssClass="select-subject" path="endHour">
                        <form:option selected="selected" value=""><spring:message code="select.endHour"/></form:option>
                        <c:forEach var="hour" begin="1" end="24" >
                            <form:option value="${hour}">${hour}:00</form:option>
                        </c:forEach>
                    </form:select>
                    <form:errors cssClass="error-text" path="endHour" element="p"/>
                </div>
                <div class="button-container">
                    <input class="button-2" type="submit" value="<spring:message code="addTimeslot"/>"/>
                </div>
            </form:form>
        </div>
    </div>
</div>
<div class="footer">
</div>
</body>

</html>
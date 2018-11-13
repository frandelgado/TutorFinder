<%@ taglib prefix="c" uri ="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value = "/resources/css/fonts.css" />" rel='stylesheet'>
    <link rel="stylesheet" href="<c:url value="/resources/css/stylesheet.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/course.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/navbar.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/search.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/responsive.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/conversations.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/schedule.css" />">
    <script src="<c:url value="/resources/js/jquery-3.3.1.min.js" />"></script>
    <script src="<c:url value="/resources/js/dropdownClick.js" />"></script>
    <title>Tu Teoria | <spring:message code="course.title"/></title>
</head>

<body class="body">
<%@ include file="navbar.jsp" %>

<div class="content my-reservation">
    <div class="search-results">
        <h3 class="search-data"><spring:message code="yourReservations" htmlEscape="true"/></h3>
        <c:if test="${courseFiles.size() == 0}">
            <h1><spring:message code="no.reservations"/></h1>
        </c:if>
        <c:forEach var="file" items="${courseFiles}">
            <div class="reservation-course-result">
                <div class="search-result-img">
                    <div class="button-2 relative" type="submit">
                        <a class="class-button" href="<c:url value="/downloadFile?courseFile=${file.id}" />"></a>
                        <spring:message code="download"/>
                    </div>
                    <c:if test="${param.professor == currentUser.id}">
                        <div class="button-2 relative" type="submit">
                            <a class="class-button" href="<c:url value="/deleteFile?courseFile=${file.id}&professor=${param.professor}&subject=${param.subject}" />"></a>
                            <spring:message code="delete"/>
                        </div>
                    </c:if>
                </div>
                <a class="search-result-title">
                    <c:out value="${file.name}" escapeXml="true" /></a>
                <a class="search-result-professor" >
                    <spring:message code="reservation.professor" arguments="${file.type}" htmlEscape="true" /></a>
                <a class="search-result-specs"></a>
                <a class="search-result-description"><spring:message code="reservation.day" arguments="${file.description}" htmlEscape="true" /></a>
                <a class="search-result-status"></a>

            </div>
        </c:forEach>
    </div>
    <c:if test="${param.professor == currentUser.id}">
        <div class="panel panel-default">
            <div class="panel-heading"><span class="lead">Upload New Document</span></div>
            <div class="uploadcontainer">
                <c:url value="/uploadFile?professor=${param.professor}&subject=${param.subject}" var="postPath"/>
                <form:form method="POST" modelAttribute="uploadClassFileForm" enctype="multipart/form-data" class="form-horizontal" action="${postPath}">

                    <div class="row">
                        <div class="form-group col-md-12">
                            <label class="col-md-3 control-lable" for="file">Upload a document</label>
                            <div class="col-md-7">
                                <form:input type="file" path="file" id="file" class="form-control input-sm"/>
                                <div class="has-error">
                                    <form:errors path="file" class="help-inline"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-md-12">
                            <label class="col-md-3 control-lable" for="description">Description</label>
                            <div class="col-md-7">
                                <form:input type="text" path="description" id="description" class="form-control input-sm"/>
                            </div>

                        </div>
                    </div>

                    <div class="row">
                        <div class="button-container">
                            <input class="button-2" type="submit" value="<spring:message code="upload"/>"/>
                        </div>
                    </div>

                </form:form>
            </div>
        </div>
    </c:if>
</div>
</div>
</body>
</html>

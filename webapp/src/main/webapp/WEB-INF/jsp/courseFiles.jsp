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

<body>
<%@ include file="navbar.jsp" %>

<div class="generic-container">
    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="lead">List of Documents </span></div>
        <div class="tablecontainer">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>No.</th>
                    <th>File Name</th>
                    <th>Type</th>
                    <th>Description</th>
                    <th width="100"></th>
                    <th width="100"></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${courseFiles}" var="file" varStatus="counter">
                    <tr>
                        <td>${counter.index + 1}</td>
                        <td>${file.name}</td>
                        <td>${file.type}</td>
                        <td>${file.description}</td>
                        <td><a href="<c:url value='/downloadFile?courseFile=${file.id}' />" class="btn btn-success custom-width">download</a></td>
                        <td><a href="<c:url value='/deleteFile?courseFile=${file.id}&professor=${param.professor}&subject=${param.subject}' />" class="btn btn-danger custom-width">delete</a></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
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
    <div class="well">
        <%--Go to <a href="<c:url value='/list' />">Users List</a>--%>
    </div>
</div>
</body>
</html>

<%@ taglib prefix="c" uri ="http://java.sun.com/jstl/core_rt" %>

    <html>

    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="<c:url value=" /resources/css/stylesheet.css " />" rel="stylesheet">
        <!--
    <link rel="stylesheet" type="text/css" href="./css/stylesheet.css">
    <link rel="stylesheet" href="./css/bootstrap.min.css">
-->
        <title>Tu Teoria | Results</title>
    </head>

    <body class="staticSearchResults">

        <div class="navbar">
            <a class="logo-box" href="<c:url value=" / "/>">
                <img alt="Tu Teoria" class="logo" src="<c:url value=" /resources/images/logo_invert.jpg " />" />
            </a>
            <!--
            <a class="navbar-button" href="./staticHome.html">Home</a>
            <a class="navbar-button" href="javascript:void(0)">Courses</a>
            <a class="navbar-button" href="javascript:void(0)">Tutors</a>
            <a class="navbar-button" href="https://google.com">Profile</a>
            -->
            <div class="search-bar">
                <input class="search-input" />
                <div class="dropdown">
                    <select>
                        <option value="" selected disabled>Select your search category</option>
                        <option>Professor</option>
                        <option>Course</option>
                        <option>Subject</option>
                    </select>
                </div>
                <button class="search-button">
                    <img class="search-img" src="https://static.thenounproject.com/png/337699-200.png" />
                </button>
            </div>
        </div>

        <div class="content">
            <div class="filter-panel">
                <!--<div class="area-filter">
                    <a>Only tutors from your area?</a>
                    <input type="checkbox" />
                </div>
                <div class="area-filter">
                    <a>Include creepy guys?</a>
                    <input type="checkbox" />
                </div>-->
            </div>

            <div class="search-results">
                <h3 class="search-data">Buscaste: 
                    <c:out value="'${search}'" escapeXml="true" />
                </h3>
                <c:forEach var="result" items="${results}">
                    <div class="search-result">
                        <a class="search-result-img"><img src="https://static.thenounproject.com/png/337699-200.png" /></a>
                        <a class="search-result-title">
                            <c:out value="${result.name}" escapeXml="true" /></a>
                        <a class="search-result-description">
                            <c:out value="${result.description}" escapeXml="true" /></a>
                    </div>
                </c:forEach>
            </div>
        </div>
    </body>

    </html>
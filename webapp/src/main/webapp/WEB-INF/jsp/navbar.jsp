<div class="navbar">
    <a href="<c:url value="/" />" class="logo-box">
        <img alt="Tu Teoria" class="logo" src="<c:url value="/resources/images/logo_invert.jpg" />" />
    </a>

    <div class="search-bar">
        <form role="search" action="<c:url value="/searchResults" />" class="search-bar">
            <input class="search-input" type="search" name="search" placeholder="<spring:message code="search"/>"/>
            <div class="dropdown">
                <select class="select-search-type" name="type">
                    <option value="" selected disabled><spring:message code="search.category" /> </option>
                    <option value="professor"><spring:message code="professor" /> </option>
                    <option value="course"><spring:message code="course.title" /> </option>
                    <option value="area"><spring:message code="area" /></option>
                </select>
            </div>
            <button type="submit" class="search-button">
                <img class="search-img" src="<c:url value="https://static.thenounproject.com/png/337699-200.png" />" />
            </button>
        </form>
    </div>

    <div class="navbar-buttons">
        <c:choose>
            <c:when test="${currentUser != null}">
                <a href="<c:url value="/Conversations" />" class="navbar-button"><spring:message code="conversations.title"/></a>
                <c:choose>
                    <c:when test="${currentUserIsProfessor == true}">
                        <a href="<c:url value="/Profile" />" class="navbar-button"><spring:message code="profile.title"/></a>
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value="/registerAsProfessor" />" class="navbar-button"><spring:message code="register.professor"/></a>
                    </c:otherwise>
                </c:choose>
                <a href="<c:url value="/logout" />" class="navbar-button"><spring:message code="user.logout"/></a>
            </c:when>
            <c:otherwise>
                <a href="<c:url value="/register" />" class="navbar-button"><spring:message code="register"/></a>
                <a href="<c:url value="/login" />" class="navbar-button"><spring:message code="login"/></a>
            </c:otherwise>
        </c:choose>
    </div>
</div>

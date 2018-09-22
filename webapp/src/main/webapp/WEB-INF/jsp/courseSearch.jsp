<c:forEach var="result" items="${results}">
        <div class="search-course-result">
            <a class="search-result-img"><img src="<c:url value="https://static.thenounproject.com/png/337699-200.png" />"/></a>
            <a class="search-result-title" href="<c:url value="/Course/?professor=${result.professor.id}&subject=${result.subject.id}" />">
                <c:out value="${result.subject.area.name} - ${result.subject.name}" escapeXml="true" /></a>
            <a class="search-result-professor" href="<c:url value="/Professor/${result.professor.id}"/>" >
                <c:out value="${result.professor.name}" escapeXml="true" /></a>
            <a class="search-result-specs"><spring:message code="course.specs" arguments="${result.price}" htmlEscape="true" /></a>
            <a class="search-result-description">
                <c:out value="${result.description}" escapeXml="true" /></a>
        </div>
</c:forEach>

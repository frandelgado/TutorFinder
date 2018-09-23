<c:forEach var="result" items="${results}">
    <div class="search-area-result">
        <a class="search-result-img"><img src="<c:url value="https://static.thenounproject.com/png/337699-200.png" />"/></a>
        <a class="search-result-title" href="<c:url value="/Area/${result.id}"/>" >
            <c:out value="${result.name}" escapeXml="true" /></a>
        <a class="search-result-description">
            <c:out value="${result.description}" escapeXml="true" /></a>
    </div>
</c:forEach>

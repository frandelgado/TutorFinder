<c:forEach var="result" items="${pagedResults.results}">
    <div class="search-area-result">
        <a class="search-result-img"><img src="<c:url value="https://static.thenounproject.com/png/337699-200.png" />"/></a>
        <a class="search-result-title" href="<c:url value="/Professor/${result.id}"/>" >
            <c:out value="${result.name} ${result.lastname}" escapeXml="true" /></a>
        <a class="search-result-description">
            <c:out value="${result.description}" escapeXml="true" /></a>
    </div>
</c:forEach>

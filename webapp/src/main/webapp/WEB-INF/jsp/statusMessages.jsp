<c:if test="${SUCCESS_MESSAGE.length() > 0}">
    <div id="display-success"><c:out value="${SUCCESS_MESSAGE}"/></div>
</c:if>
<c:if test="${ERROR_MESSAGE.length() > 0}">
    <div id="display-error"><c:out value="${ERROR_MESSAGE}"/></div>
</c:if>

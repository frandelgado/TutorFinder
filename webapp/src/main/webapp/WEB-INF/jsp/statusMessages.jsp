<c:if test="${SUCCESS_MESSAGE.length() > 0}">
    <div id="display-success"><spring:message code="${SUCCESS_MESSAGE}"/></div>
</c:if>
<c:if test="${ERROR_MESSAGE.length() > 0}">
    <div id="display-error"><spring:message code="${ERROR_MESSAGE}"/></div>
</c:if>

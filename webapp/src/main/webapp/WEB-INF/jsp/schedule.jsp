<div class="schedule">
    <table>
        <tr>
            <th>
            </th>
            <th>
                <a><spring:message code="day.monday"/></a>
            </th>
            <th>
                <a><spring:message code="day.tuesday"/></a>
            </th>
            <th>
                <a><spring:message code="day.wednesday"/></a>
            </th>
            <th>
                <a><spring:message code="day.thursday"/></a>
            </th>
            <th>
                <a><spring:message code="day.friday"/></a>
            </th>
            <th>
                <a><spring:message code="day.saturday"/></a>
            </th>
            <th>
                <a><spring:message code="day.sunday"/></a>
            </th>
        </tr>
        <c:forEach var="i" begin="1" end="24">
            <tr>
                <td>${i}:00</td>
                <c:choose>
                    <c:when test="${schedule.monday.contains(i)}">
                        <td class="selected-time" />
                    </c:when>
                    <c:otherwise>
                        <td />
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test="${schedule.tuesday.contains(i)}">
                        <td class="selected-time" />
                    </c:when>
                    <c:otherwise>
                        <td />
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test="${schedule.wednesday.contains(i)}">
                        <td class="selected-time" />
                    </c:when>
                    <c:otherwise>
                        <td />
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test="${schedule.thursday.contains(i)}">
                        <td class="selected-time" />
                    </c:when>
                    <c:otherwise>
                        <td />
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test="${schedule.friday.contains(i)}">
                        <td class="selected-time" />
                    </c:when>
                    <c:otherwise>
                        <td />
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test="${schedule.saturday.contains(i)}">
                        <td class="selected-time" />
                    </c:when>
                    <c:otherwise>
                        <td />
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test="${schedule.sunday.contains(i)}">
                        <td class="selected-time" />
                    </c:when>
                    <c:otherwise>
                        <td />
                    </c:otherwise>
                </c:choose>
            </tr>
        </c:forEach>
    </table>
</div>
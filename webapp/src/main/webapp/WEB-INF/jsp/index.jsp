<%@ taglib prefix="c" uri ="http://java.sun.com/jstl/core_rt" %>

<html>
<body>
<h2>Hello <c:out value="${user.username}" escapeXml="True"/>!</h2>
</body>
</html>

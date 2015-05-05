<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<html>
<body>
<h2>Users List</h2>
    <ul>
    <c:if test="${not empty users}">
        <c:forEach var="user" items="${users}">
        <li><a href="/api/users/${user.getUsername()}">${user.getUsername()}</a></li>
        </c:forEach>
    </c:if>
    </ul>
</body>
</html>

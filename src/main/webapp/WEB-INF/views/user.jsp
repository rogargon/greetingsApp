<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<body>

<p><a href="/api/users">Users</a></p>

<c:if test="${not empty user}">
    <h2>User ${user.getUsername()}</h2>
    <c:if test="${not empty user.getEmail()}">
        <p>E-mail: ${user.getEmail()}</p>
    </c:if>
    <c:if test="${not empty user.getImageUrl()}">
        <img src="${user.getImageUrl()}"/>
    </c:if>
    <c:if test="${not empty user.getGreetings()}">
        <h3>User Greetings</h3>
        <c:forEach var="greeting" items="${user.getGreetings()}">
            <li><a href="/api/greetings/${greeting.getId()}">${greeting.getId()}</a>: ${fn:escapeXml(greeting.getContent())}</li>
        </c:forEach>
    </c:if>
</c:if>

</body>
</html>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<html>
<body>
<h2>Greetings List</h2>
    <ul>
    <c:if test="${not empty greetings}">
        <c:forEach var="greeting" items="${greetings}">
            <li><a href="/api/greetings/${greeting.getId()}">${greeting.getId()}</a>: ${fn:escapeXml(greeting.getContent())}</li>
        </c:forEach>
    </c:if>
    </ul>
    <p><a href="/api/greetings/form">Add</a></p>
</body>
</html>
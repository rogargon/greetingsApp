<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>

<html>
<body>
<h2>Greetings List</h2>
    <ul>
    <c:if test="${not empty greetings}">
        <c:forEach var="greeting" items="${greetings}">
        <li><a href="/greetings/${greeting.getId()}">${greeting.getId()}</a>: ${greeting.getContent()}</li>
        </c:forEach>
    </c:if>
        <li><a href="greetings/form">Add</a></li>
    </ul>

</body>
</html>
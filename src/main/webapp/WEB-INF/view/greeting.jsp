<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<body>

<p><a href="/greetings">Greetings</a></p>

<c:if test="${not empty greeting}">
    <h2>Greeting number ${greeting.getId()}</h2>
    <p>Message: ${greeting.getContent()} (<a href="/greetings/${greeting.getId()}/form">edit</a>)</p>
</c:if>

<form:form method="DELETE" action="/greetings/${greeting.getId()}">
    <p><input type="submit" value="Delete"/></p>
</form:form>

</body>
</html>
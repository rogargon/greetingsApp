<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<body>

<p><a href="/api/greetings">Greetings</a></p>

<c:if test="${not empty greeting}">
    <h2>Greeting number ${greeting.getId()}</h2>
    <p>Message: ${fn:escapeXml(greeting.getContent())}
        (<a href="/api/greetings/${greeting.getId()}/form">edit</a>)</p>
    <p>By ${greeting.getEmail()} on ${greeting.getDate()}</p>

    <form:form method="DELETE" action="/api/greetings/${greeting.getId()}">
        <p><input type="submit" value="Delete"/></p>
    </form:form>
</c:if>

</body>
</html>

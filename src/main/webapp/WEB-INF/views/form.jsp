<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
    <title>Greeting Form</title>
</head>
<body>

<c:choose>
    <c:when test="${greeting.getId()>0}">
        <h3>Update Greeting</h3>
        <c:set var="method" value="PUT"/>
        <c:set var="action" value="/greetings/${greeting.getId()}"/>
    </c:when>
    <c:otherwise>
        <h3>Create Greeting</h3>
        <c:set var="method" value="POST"/>
        <c:set var="action" value="/greetings"/>
    </c:otherwise>
</c:choose>

<form:form method="${method}" action="${action}" modelAttribute="greeting">
    <table>
        <tr>
            <td><form:label path="content">Content</form:label></td>
            <td><form:input path="content"/></td>
        </tr>
        <tr>
            <td><input type="submit" value="Submit" /></td>
        </tr>
    </table>
</form:form>

</body>
</html>
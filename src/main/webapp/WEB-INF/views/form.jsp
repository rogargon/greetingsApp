<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
        <c:set var="action" value="/api/greetings/${greeting.getId()}"/>
    </c:when>
    <c:otherwise>
        <h3>Create Greeting</h3>
        <c:set var="method" value="POST"/>
        <c:set var="action" value="/api/greetings"/>
    </c:otherwise>
</c:choose>

<form:form method="${method}" action="${action}" modelAttribute="greeting">
    <table>
        <tr>
            <td><form:label path="content">Content</form:label></td>
            <td><form:input path="content"/> <i><form:errors path="content"></form:errors></i></td>
        </tr>
        <tr>
            <td><form:label path="email">E-Mail</form:label></td>
            <td><form:input path="email"/> <i><form:errors path="email"></form:errors></i></td>
        </tr>
        <tr>
            <td><form:hidden path="date"/> <i><form:errors path="date"></form:errors></i></td>
        </tr>
        <tr>
            <td><input type="submit" value="Submit" /></td>
        </tr>
    </table>
</form:form>

</body>
</html>
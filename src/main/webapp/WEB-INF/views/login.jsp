<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <title>GreetingsApp Login</title>
</head>
<body>

    <h1>GreetingsApp Login</h1>

    <sec:authorize access="isAnonymous()">
    <form action="<c:url value="/api/connect/twitter"/>" method="POST">
        <p><input type="submit" value="Connect with Twitter"/></p>
    </form>
    </sec:authorize>

    <sec:authorize access="isAuthenticated()">
    <p>User already authenticated</p>
    </sec:authorize>

</body>
</html>

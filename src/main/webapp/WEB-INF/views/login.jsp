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
<div class="page-header">
    <h1>GreetingsApp Login</h1>
</div>

<sec:authorize access="isAnonymous()">
    <!-- Social Sign In Buttons -->
    <p>
        <a href="${pageContext.request.contextPath}/auth/twitter">
            <img src="https://g.twimg.com/dev/sites/default/files/images_documentation/sign-in-with-twitter-gray.png"/>
        </a>
    </p>
</sec:authorize>

<sec:authorize access="isAuthenticated()">
    <p>User already authenticated</p>
</sec:authorize>

</body>
</html>

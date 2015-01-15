<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <title></title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/social-buttons-3.css"/>
</head>
<body>
<div class="page-header">
    <h1>GreetingsApp Login</h1>
</div>

<sec:authorize access="isAnonymous()">
    <!-- Social Sign In Buttons -->
    <div class="panel panel-default">
        <div class="panel-body">
            <h2>Twitter Sign In</h2>
            <div class="row social-button-row">
                <div class="col-lg-4">
                    <!-- Add Twitter sign in Button -->
                    <a href="${pageContext.request.contextPath}/auth/twitter"><button class="btn btn-twitter"><i class="icon-twitter"></i> | Sign in with Twitter</button></a>
                </div>
            </div>
        </div>
    </div>
</sec:authorize>

<sec:authorize access="isAuthenticated()">
    <p>User already authenticated</p>
</sec:authorize>
</body>
</html>

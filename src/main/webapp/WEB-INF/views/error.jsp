<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Error</title>
</head>
<body>
<h1>Error</h1>
<c:if test="${not empty error}">
    <p>
        <b>Error Page:</b> ${error.getUrl()}
    </p>
    <p>
        <b>Error Message:</b> ${error.getMessage()}
    </p>
</c:if>
</body>
</html>

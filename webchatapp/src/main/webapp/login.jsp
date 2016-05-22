<%--
  Created by IntelliJ IDEA.
  User: Dasha
  Date: 08.05.2016
  Time: 22:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
    <meta charset="UTF-8">
    <title>Login in BugChat</title>
    <link rel="stylesheet" type="text/css" href="../style/style_login.css" />
    <link rel="icon" href="../pic/ico.ico" type="image/x-icon">
</head>
<body>
<span>
    <h1 id="title">Welcome in BugChat</h1>

<h3>
    <c:choose>
    <c:when test="${requestScope.errorMsg!=null}">
        <c class="error">
            <c:out value="${requestScope.errorMsg}"></c:out>
            <br>
        </c>
    </c:when>
    <c:otherwise></c:otherwise>
</c:choose>
    </h3>
</span>

<form id="enter_name" action="/login" method="post">
    <p id="enter_name_text">Enter login</p>
    <input type="text" name="username" id="enter_name_box" placeholder=""/>
    <p id="enter_password_text">Enter password</p>
    <input type="password" name="pass" id="enter_password_box" placeholder=""/>
    <div><button type="submit" class="button" id="enter_name_button">Enter</button></div>
</form>
</body>
</html>

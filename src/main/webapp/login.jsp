<%--
  Created by IntelliJ IDEA.
  User: melon
  Date: 2020-11-05
  Time: 3:34 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>

<h1>Login</h1>

<%
    String userName = null;
    Cookie[] cookies = request.getCookies();
    if(cookies !=null){
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("user")) userName = cookie.getValue();
        }
    }
//    userName = (String) session.getAttribute("username");
    if(userName != null) response.sendRedirect("login");
%>

<form action="login" method="post">
    <input type="text" name="username">
    <input type="password" name="password">
    <input type="submit" value="login">
</form>

</body>
</html>

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

<%
    String userName = null;
    Cookie[] cookies = request.getCookies();
    if(cookies !=null){
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("user")) userName = cookie.getValue();
        }
    }
    if(userName != null) response.sendRedirect("home.jsp");
%>

<form method="post" action="Login">
    <input type="text" name="username">
    <input type="password" name="password">
    <input type="submit">
</form>

</body>
</html>

<%--
  Created by IntelliJ IDEA.
  User: melon
  Date: 2020-11-05
  Time: 3:38 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Seddit</title>
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
    if(userName == null) response.sendRedirect("index.jsp");
%>

<h1>Hi <%=userName%>, Welcome to Seddit</h1>

<form action="Logout" method="post">
    <input type="submit" value="Logout" >
</form>

<!--testing post message -->
<form action="Posting" method ="post">
    <label for="title">Post Title:</label>
    <input name = "title" type="text" id ="title">
    <textarea name = "content" id = "content">Enter your content...</textarea>
    <input type="submit" value="Post">
</form>

</body>
</html>

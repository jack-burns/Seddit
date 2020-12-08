<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="java.util.ArrayList" %>      <%--Importing all the dependent classes--%>
<%@page import="dao.UserPost" %>

<%
    UserPost userPost = (UserPost) request.getSession().getAttribute("UserPost");
%>
<html>
<head>
    <title> Template view</title>
</head>
<body>
<%=userPost.toString() %>
</body>
</html>

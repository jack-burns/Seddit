<%--
  Created by IntelliJ IDEA.
  User: melon
  Date: 2020-11-05
  Time: 10:47 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="java.util.ArrayList"%>      <%--Importing all the dependent classes--%>
<%@page import="dao.UserPost"%>
<%@page import="java.util.Iterator"%>

<body>
<h1>POSTS JSP PAGE</h1>

<%
    ArrayList userPosts = (ArrayList) request.getAttribute("UserPosts");
    if(userPosts != null){

        for (Object post : userPosts) {
            UserPost userPost = (UserPost) post;
%>
            <p><%=userPost.getTitle()%><p><br/>
            <p><%=userPost.getContent()%><p><br/>
            <p><%=userPost.getUsername()%><p><br/>
            <p><%=userPost.getCreate_timestamp()%><p><br/>
            <p><%=userPost.getModified_timestamp()%><p><br/>
        <%
        }
    }
%>
</body>



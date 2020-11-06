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

<%
    ArrayList userPosts = (ArrayList) request.getAttribute("UserPosts");
    if(userPosts != null){

        for (Object post : userPosts) {
            UserPost userPost = (UserPost) post;
%>
<div style="border:1px solid orangered">

            <h4>Title: <%=userPost.getTitle()%></h4>
            <p>Message: <%=userPost.getContent()%><p>
            <p>User ID: <%=userPost.getUsername()%><p>
            <p>Date created: <%=userPost.getCreate_timestamp()%><p>
            <p>Date modified: <%=userPost.getModified_timestamp()%><p>
</div>
        <%
        }
    }
%>
</body>



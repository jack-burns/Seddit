<%--
  Created by IntelliJ IDEA.
  User: ZiLi
  Date: 2020-11-12
  Time: 9:49 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="java.util.ArrayList"%>      <%--Importing all the dependent classes--%>
<%@page import="dao.UserPost"%>
<html>
<head>
    <title>Search a Post</title>
</head>
<body>
    <h1>Find a post:</h1><br/>
    <form action="search" method="post">
        <label for="username">By Username:</label>
        <input type="text" name="username" id="username"><br/>
        <label for="hashtag">By Hashtag(s):</label>
        <input type="text" name="hashtag" id="hashtag"><br/>
        <label for="fromDate">By Date:</label>
        <input type="date" name="from" id="fromDate">
        <input type="date" name="to" id="toDate"><br/>
        <input type="submit" value = "Search">
    </form>
    <%
        String userName = null;
        Cookie[] cookies = request.getCookies();
        if(cookies !=null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("user")) userName = cookie.getValue();
            }
        }
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
            <%
        if(userPost.getFileAttachment().getId()!=0){
    %>
        <form action="download" method="get">
        <p>File Attachment: <%=userPost.getFileAttachment().getName()%></p>
        <button type="submit" name="fileID" value="<%=userPost.getFileAttachment().getId()%>">Download</button>
        </form>
        <%
            }
            if(userName.equals(userPost.getUsername())){
        %>
        <form action="modify" method="GET">
            <%--there must be a better way to do this, maybe handling it in the frontend? also a JSP declaration with content reference to userPost doesn't work here for some reason--%>
            <input type = "hidden" name = "postID" value = '<%= userPost.getPostID()%>'>
            <input type="submit" value="Modify">
        </form>
        <%
            }
        %>
    </div>
    <%
            }
        }
    %>
</body>
</html>

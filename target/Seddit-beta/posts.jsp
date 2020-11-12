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

<form action="home" method="get">
    <input type="submit" name="viewposts" value="10">
    <input type="submit" name="viewposts" value="25">
    <input type="submit" name="viewposts" value="50">
    <input type="submit" name="viewposts" value="all">
</form>

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
    <form action="download" method="get">
    <p>File Attachment: <%=userPost.getFileAttachment().getName()%></p>
    <button type="submit" name="download" value="<%=userPost.getFileAttachment().getId()%>">Download</button>
    </form>
            <%
                if(userName.equals(userPost.getUsername())){
            %>
                <form action="modify" method="GET">
                    <%--there must be a better way to do this, maybe handling it in the frontend? also a JSP declaration with content reference to userPost doesn't work here for some reason--%>
                    <input type = "hidden" name = "postID" value = '<%= userPost.getPostID()%>'>
                    <input type="hidden" name = "oldTitle" value="<%=userPost.getTitle()%>">
                    <input type="hidden" name= "oldContent" value="<%= userPost.getContent()%>">
                    <input type = "submit" name = "modifyPost" value = "Modify">
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



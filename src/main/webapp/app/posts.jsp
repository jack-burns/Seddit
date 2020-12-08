<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="java.util.ArrayList" %>      <%--Importing all the dependent classes--%>
<%@page import="dao.UserPost" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css"
          integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="../style/style.css">
    <title>Post</title>
    <style>
        .post-class {
            border: 1px solid orangered;
            border-radius: 10px;
            margin: 25px;
            padding: 10px;
            background-color: white;
            -webkit-box-shadow: 0 10px 6px -6px #777;
            -moz-box-shadow: 0 10px 6px -6px #777;
            box-shadow: 0 10px 6px -6px #777;
        }

        .page-buttons {
            text-align: right;
        }

        .modify-class {
            text-align: right;
        }
    </style>
</head>
<body>

<form action="home" method="get">
    <div class="page-buttons">
        <input type="submit" class="btn btn-primary" name="viewposts" value="10">
        <input type="submit" class="btn btn-primary" name="viewposts" value="25">
        <input type="submit" class="btn btn-primary" name="viewposts" value="50">
        <input type="submit" class="btn btn-primary" name="viewposts" value="all">
    </div>
</form>

<%
    ArrayList userPosts = (ArrayList) request.getAttribute("UserPosts");
    if (userPosts != null) {

        for (Object post : userPosts) {
            UserPost userPost = (UserPost) post;
%>
<div class="post-class container">
    <div class="row">
        <div class="col col-10">
            <h1><%=userPost.getTitle()%>
            </h1>
            <p>Message:<%=userPost.getContent()%><p>
            <p>User ID:<%=userPost.getUsername()%><p>
            <p>Date created:<%=userPost.getCreate_timestamp()%><p>
            <p>Date modified:<%=userPost.getModified_timestamp()%><p>
            <p>Group:<%=userPost.getGroup()%></p>
            <p>
            <form action="templateView" method="GET">
                <%--there must be a better way to do this, maybe handling it in the frontend? also a JSP declaration with content reference to userPost doesn't work here for some reason--%>
                <input type="hidden" name="postID" value='<%= userPost.getPostID()%>'>
                <input type="submit" class="btn btn-secondary" value="View Post">
            </form>



<%
    try{
        if(!userPost.getFileAttachment().getName().equals("")){
%>
                <form action="download" method="get">
            <p>File Attachment: <%=userPost.getFileAttachment().getName()%>
            </p>
            <button class="btn btn-secondary" type="submit" name="fileID"
                    value="<%=userPost.getFileAttachment().getId()%>">Download
            </button>
            </form>
<%
        }
    } catch (NullPointerException e) {}

    if (userName.equals(userPost.getUsername()) || visibility.equals("admin")) {
%>
        </div>
        <div class="col modify-class">
            <form action="modify" method="GET">
                <%--there must be a better way to do this, maybe handling it in the frontend? also a JSP declaration with content reference to userPost doesn't work here for some reason--%>
                <input type="hidden" name="postID" value='<%= userPost.getPostID()%>'>
                <input type="submit" class="btn btn-secondary" value="Modify">
            </form>
<%
    }
    if (visibility.equals("admin")) {
%>
        </div>
        <div class="col modify-class">
            <form action="delete" method="GET">
                <input type="hidden" name="postID" value="<%= userPost.getPostID()%>">
                <input type="submit" class="btn btn-secondary" value="Delete">
            </form>
<%
    }
%>
        </div>
    </div>
</div>
<%
        }
    }
%>
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"
        integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx"
        crossorigin="anonymous"></script>
</body>
</body>
</html>


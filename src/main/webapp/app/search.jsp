<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="java.util.ArrayList" %>      <%--Importing all the dependent classes--%>
<%@page import="dao.UserPost" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css"
          integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="../style/style.css">
    <title> Search a Post</title>
    <style>
        body {
            background-color: #f5f5f5;
        }

        .global-container {
            background-color: #f5f5f5;
            height: 100%;
        }

        .submit-form {
            margin-top: 100px;
            margin-bottom: 50px;
            padding: 20px;
            border-radius: 10px;
            background-color: white;
            -webkit-box-shadow: 0 10px 6px -6px #777;
            -moz-box-shadow: 0 10px 6px -6px #777;
            box-shadow: 0 10px 6px -6px #777;
        }

        .welcome-msg {
            color: white;
            margin-right: 20px;
        }

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

    </style>
</head>
<body>
<div class="global-container">

    <%
        String userName = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("user")) userName = cookie.getValue();
            }
        }
    %>
    <nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark bg-light">
        <img src="../style/seddit.png" width="40" height="40" alt="" loading="img">
        <span class="navbar-brand">Seddit</span>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarCollapse"
                aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarCollapse">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item">
                    <a class="nav-link" href="/app/home">Home </a>
                </li>
                <li class="nav-item active">
                    <a class="nav-link active" href="#">Search <span class="sr-only">(current)</span></a>
                </li>
            </ul>
            <span class="welcome-msg">Logged in as <%=userName%> </span>
            <form class="form-inline mt-2 mt-md-0" action="logout" method="post">
                <input class="btn btn-outline-primary" type="submit" value="logout">
            </form>
        </div>


    </nav>
    <div class="container">
        <div class="submit-form">
            <h1>Find a post:</h1><br/>
            <form action="search" method="post">
                <label for="username"><h5>By Username:</h5></label>
                <input type="text" class="form-control" name="username" id="username"><br/>
                <label for="hashtag"><h5>By Hashtag(s):</h5></label>
                <input type="text" class="form-control" name="hashtag" id="hashtag"><br/>
                <label for="fromDate"><h5>By Date:</h5></label>
                <input type="date" class="form-control" name="from" id="fromDate">
                <input type="date" class="form-control" name="to" id="toDate"><br/>
                <input type="submit" class="btn btn-primary" value="Search">
            </form>
        </div>
        <%
            ArrayList userPosts = (ArrayList) request.getAttribute("UserPosts");
            if (userPosts != null) {

                for (Object post : userPosts) {
                    UserPost userPost = (UserPost) post;
        %>
        <div class="post-class">

            <h1><%=userPost.getTitle()%>
            </h1>
            <p>Message:<%=userPost.getContent()%><p>
            <p>User ID:<%=userPost.getUsername()%><p>
            <p>Date created:<%=userPost.getCreate_timestamp()%><p>
            <p>Date modified:<%=userPost.getModified_timestamp()%>
            <p>
                    <%
        if(userPost.getFileAttachment().getId()!=0){
    %>
                <form action="download" method="get">
            <p>File Attachment: <%=userPost.getFileAttachment().getName()%>
            </p>
            <button type="submit" name="fileID" value="<%=userPost.getFileAttachment().getId()%>">Download</button>
            </form>
            <%
                }
                if (userName.equals(userPost.getUsername())) {
            %>
            <form action="modify" method="GET">
                <%--there must be a better way to do this, maybe handling it in the frontend? also a JSP declaration with content reference to userPost doesn't work here for some reason--%>
                <input type="hidden" name="postID" value='<%= userPost.getPostID()%>'>
                <input type="submit" class="btn btn-secondary" value="Modify">
            </form>
            <%
                }
            %>
        </div>
        <%
                }
            }
        %>
    </div>
</div>
</div>
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"
        integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx"
        crossorigin="anonymous"></script>

</body>
</html>

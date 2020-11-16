<%@ page import="dao.FileAttachment" %>
<%@ page import="dao.UserPost" %>
<%@ page import="java.util.Objects" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    UserPost userPost = (UserPost) request.getSession().getAttribute("UserPost");
    FileAttachment fileAttachment = userPost.getFileAttachment();
%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="style/style.css">
    <title>Modify content</title>
    <style>
        .global-container{
            height: 100%;
            background-color: #f5f5f5;
        }
        body{
            background-color: #f5f5f5;
        }

        .welcome-msg{
            color: white;
            margin-right:20px;
        }

        .submit-form{
            margin-top:100px;
            padding: 20px;
            border-radius: 10px;
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
        if(cookies !=null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("user")) userName = cookie.getValue();
            }
        }
//    userName = (String) session.getAttribute("username");
        if(userName == null) response.sendRedirect("login");
    %>
    <nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark bg-light">
        <img src="style/seddit.png" width="40" height="40" alt="" loading="img">
        <span class="navbar-brand">Seddit</span>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarCollapse">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item ">
                    <a class="nav-link " href="/home.jsp">Home <span class="sr-only">(current)</span></a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">Search</a>
                </li>
            </ul>
            <span class="welcome-msg">Logged in as <%=userName%> </span>
            <!--
             <form class="form-inline mt-2 mt-md-0">
                 <input class="form-control mr-sm-2" type="text" placeholder="Search" aria-label="Search">
                 <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
             </form>
             -->
            <form class="form-inline mt-2 mt-md-0" action="logout" method="post">
                <input class="btn btn-outline-primary" type="submit" value="logout" >
            </form>
        </div>


    </nav>
    <div class="container">
        <div class="submit-form">
    <h1>Modify your post</h1>
    <form action="modifyPost" method="POST">
        <label for="title">Title:</label>
        <input type="text" id="title" name = "title" value="<%=userPost.getTitle()%>">
        <br/>
        <label for="content">Title:</label>
        <textarea id="content" name = "content" ><%=userPost.getContent()%></textarea>
        <br/>
        <input type="submit" class="btn btn-primary" name="modifyPost" value="Post Comment">
        <input type = "submit" class="btn btn-primary" name = "deletePost" value = "Delete">
    </form>

    <form action="modifyFile" method="POST" enctype="multipart/form-data">
        File Attachment: <%=Objects.toString(fileAttachment.getName(), "No Attachment")%>
        <input type="file" name="file" size="50"/>
        <input type = "submit" class="btn btn-secondary" name = "uploadFile" value = "Replace file">
        <input type = "submit" class="btwn btn-secondary" name = "deleteFile" value = "Delete">
    </form>
        </div>
        <%--
        -see if request is cumulative, if it is there is no need for hidden input here (okay, it would seem like the request is overwritten)
        -send necessary info to update the correct post in DB, username, created time, title and content
        --%>
    </div>
</div>
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx" crossorigin="anonymous"></script>

</body>
</html>

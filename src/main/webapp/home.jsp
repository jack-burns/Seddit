<%@ page import="java.util.ArrayList" %>
<%@ page import="dao.UserPost" %><%--
  Created by IntelliJ IDEA.
  User: melon
  Date: 2020-11-05
  Time: 3:38 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="style/style.css">
    <title>Seddit</title>
    <style>
        .global-container{
            background-color: #f5f5f5;
        }

        .submit-form{
            margin-top:70px;
            padding-top: 20px;
        }

        .welcome-msg{
            color: white;
            margin-right:20px;
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
        if(userName == null){ 
            response.sendRedirect("login");
        } else {}
    %>
    <nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark bg-light">
        <img src="style/seddit.png" width="40" height="40" alt="" loading="img">
        <span class="navbar-brand">Seddit</span>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarCollapse">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item active">
                    <a class="nav-link active" href="#">Home <span class="sr-only">(current)</span></a>
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
    <form action="home" method="post"class="submit-form" enctype="multipart/form-data">
        <label for="title">Title:</label>
        <input name="title" type="text" id="title">
        <label for="content">Enter your content:</label>
        <textarea name="content" id="content"></textarea>
        <input type="file" class="btn btn-secondary" name="file" size="50"/>
       <input type="submit" class="btn btn-secondary" name="postmessage" value="Post"/>
    </form>
    </div>
<form action="search" method="get">
    <input name = "search" type="submit" value = "Search Posts">
</form>


 
<div class="container">
<%@ include file="posts.jsp" %>

</div>
<%
    }
%>
</div>
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx" crossorigin="anonymous"></script>
</body>
</html>

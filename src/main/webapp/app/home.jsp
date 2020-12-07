<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css"
          integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="../style/style.css">
    <title>Seddit</title>
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

    </style>
</head>
<body>
<div class="global-container">
    <%
        String userName = (String) session.getAttribute("username");
        String visibility = (String) session.getAttribute("visibility");
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
                <li class="nav-item active">
                    <a class="nav-link active" href="#">Home <span class="sr-only">(current)</span></a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/app/search">Search</a>
                </li>
            </ul>
            <span class="welcome-msg">Logged in as <%=userName%> </span>
            <form class="form-inline mt-2 mt-md-0" action="/logout" method="post">
                <input class="btn btn-outline-primary" type="submit" value="logout">
            </form>
        </div>


    </nav>

    <div class="container">
        <form action="home" method="post" class="submit-form" enctype="multipart/form-data">
            <h1>Create a new post</h1>
            <label for="title"><h5>Title:</h5></label><br>
            <input name="title" class="form-control" type="text" id="title"><br>
            <label for="content"><h5>Enter your content:</h5></label><br>
            <textarea name="content" id="content" class="form-control" rows="3"></textarea><br>
            <label for="group"><h5>Visibility:</h5></label>
            <select name="group" id="group">
                <option value="Public">Public</option>
                <c:forEach items="${allVisibilities}" var="vis">
                    <option value="${vis}">${vis}</option>
                </c:forEach>
            </select><br><br>
            <input type="file" class="btn btn-secondary" name="file" size="50"/>

            <input type="submit" class="btn btn-primary" name="postmessage" value="Post"/>
        </form>
    </div>

    <div class="container">
        <%@ include file="posts.jsp" %>
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

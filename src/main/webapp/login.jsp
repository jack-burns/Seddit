<%--
  Created by IntelliJ IDEA.
  User: melon
  Date: 2020-11-05
  Time: 3:34 p.m.
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
    <title>Login</title>
    <style>
        .global-container{
            height: 100%;
            background-color: #f5f5f5;
        }

        .form-style{
            background: #fff;
            padding: 30px;
            border-radius: 10px;
            border-color:grey;
            margin-top:30px;

        }
    </style>
</head>
<body>



<%
    String userName = null;
    Cookie[] cookies = request.getCookies();
    if(cookies !=null){
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("user")) userName = cookie.getValue();
        }
    }
//    userName = (String) session.getAttribute("username");
    if(userName != null) response.sendRedirect("login");
%>
<div class="global-container">
<section class="container-fluid">
    <section class="row justify-content-center">
        <section class="col-12 col-sm-6 col-md-3">
            <form  class="form-style" action="login" method="post">
                <h1>Log in to Seddit</h1>
                    <div class="form-group">
                        <label for="usernameLabel">Username</label>
                        <input type="text" class="form-control" id="usernameLabel" name="username">
                    </div>
                    <div class="form-group">
                        <label for="passwordLabel">Password</label>
                        <input type="password" class="form-control" id="passwordLabel" name="password">
                    </div>
                        <input class="btn btn-primary btn-block" type="submit" value="Sign in">

            </form>
        </section>
    </section>
</section>
</div>
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx" crossorigin="anonymous"></script>
</body>
</html>

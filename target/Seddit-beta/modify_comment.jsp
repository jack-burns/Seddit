<%--
  Created by IntelliJ IDEA.
  User: ZiLi
  Date: 2020-11-07
  Time: 3:24 a.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Modify content</title>
</head>
<body>
    <h1>Modify your post</h1>
    <form action="modify" method="post">
        <input type= "hidden" name = "postID" value = '<%= request.getParameter("postID")%>'>
        <label for="newTitle">Title:</label>
        <input type="text" id="newTitle" name = "newTitle" value="<%=request.getParameter("oldTitle")%>">
        <br/>
        <textarea name = "newContent" id="newContent"><%=request.getParameter("oldContent")%></textarea>
        <br/>
        <input type="submit" value="Post Comment">
    </form>
    <form action="delete" method="GET">
        <input type = "hidden" name = "postID" value = '<%= request.getParameter("postID")%>'>
        <input type = "submit" name = "deletePost" value = "Delete">
    </form>
        <%--
        -see if request is cumulative, if it is there is no need for hidden input here (okay, it would seem like the request is overwritten)
        -send necessary info to update the correct post in DB, username, created time, title and content
        --%>
</body>
</html>

<%@ page import="dao.FileAttachment" %>
<%@ page import="dao.UserPost" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    UserPost userPost = (UserPost) request.getSession().getAttribute("UserPost");
    FileAttachment fileAttachment = userPost.getFileAttachment();
%>
<html>
<head>
    <title>Modify content</title>
</head>
<body>
    <h1>Modify your post</h1>
    <form action="modifyPost" method="POST">
        <label for="title">Title:</label>
        <input type="text" id="title" name = "title" value="<%=userPost.getTitle()%>">
        <br/>
        <label for="content">Title:</label>
        <textarea id="content" name = "content" ><%=userPost.getContent()%></textarea>
        <br/>
        <input type="submit" name="modifyPost" value="Post Comment">
        <input type = "submit" name = "deletePost" value = "Delete">
    </form>

    <form action="modifyFile" method="POST" enctype="multipart/form-data">
        File Attachment: <%=fileAttachment.getName()%>
        <input type="file" name="file" size="50"/>
        <input type = "submit" name = "uploadFile" value = "Replace file">
        <input type = "submit" name = "deleteFile" value = "Delete">
    </form>

        <%--
        -see if request is cumulative, if it is there is no need for hidden input here (okay, it would seem like the request is overwritten)
        -send necessary info to update the correct post in DB, username, created time, title and content
        --%>
</body>
</html>
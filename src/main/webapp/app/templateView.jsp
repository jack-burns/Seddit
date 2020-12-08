<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="java.util.ArrayList" %>      <%--Importing all the dependent classes--%>
<%@page import="dao.UserPost" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<jsp:useBean id="aPost" type="dao.UserPost"
             scope="request"/>
<html>
<head>
    <title> Template view</title>
</head>
<body>
<div class="post">
    Post id: <c:out value="${sessionScope.UserPost.postID}"/><br>
    Post title: <c:out value="${sessionScope.UserPost.title}" /><br>
    Post author: <c:out value="${sessionScope.UserPost.username}"/><br>
    Created on: <c:out value="${sessionScope.UserPost.create_timestamp}" /><br>
    <c:if test="${sessionScope.UserPost.create_timestamp ne sessionScope.UserPost.modified_timestamp}">
        Modified on: <c:out value="${sessionScope.UserPost.modified_timestamp}"/> <br>
    </c:if>
    Content: <c:out value="${sessionScope.UserPost.content}"/><br>
    Group <c:out value="${sessionScope.UserPost.group}"/> <br>
    <c:if test="${sessionScope.UserPost.fileAttachment.name ne null && sessionScope.UserPost.fileAttachment.name ne ''}">
        Attachment name: <c:out value="${sessionScope.UserPost.fileAttachment.name}"/> <br>
        <form action="download" method="get">
            <button  type="submit" name="fileID"
                    value="${sessionScope.UserPost.fileAttachment.id}">Download
            </button>
        </form>
    </c:if>

</div>
</body>
</html>

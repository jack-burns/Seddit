<%--
  Created by IntelliJ IDEA.
  User: Tim
  Date: 12/14/2020
  Time: 6:09 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType = "text/xml" %>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@page import="java.util.ArrayList" %>      <%--Importing all the dependent classes--%>
<%@page import="dao.UserPost" %>
<jsp:useBean id="aPost" type="dao.UserPost"
             scope="request"/>


<post>
    <id> <c:out value="${sessionScope.UserPost.postID}"/></id>
    <author> <c:out value="${sessionScope.UserPost.username}"/> </author>
    <created> <c:out value="${sessionScope.UserPost.create_timestamp}"/></created>
    <updated> <c:out value = "${sessionScope.UserPost.modified_timestamp}"/></updated>
    <group> <c:out value = "${sessionScope.UserPost.group}"/></group>
    <body> <c:out value = "${sessionScope.UserPost.content}"/> </body>

</post>

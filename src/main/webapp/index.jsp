<%@page language="java" contentType="text.html" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ page import="com.courses.db.dto.Student"  %>
<%@ page import="java.util.List"  %>
<html>
<head>
</head>
<body>
    <%            for (Student student : (List<Student>)request.getAttribute("students")) {
                       out.println("<table><tr><a href=\"myServlet?getParametr=getStudentById&id=" + student.getId() + "\">" + student + "</a>");

                       out.println("</tr></table>");
                   }
    %>

    <input type="button" value="Create" onClick='location.href="myServlet?getParametr=pageForInserting"'><br>
    <h3>${studentWasDeleted}</h3><h3>${studentWasAdded}</h3>
</body>
</html>

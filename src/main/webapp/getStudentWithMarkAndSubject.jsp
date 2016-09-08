<%@page language="java" contentType="text.html" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ page import="com.courses.db.dto.Student" import="com.courses.db.dto.Subject"  %>
<%@ page import="java.util.List"  %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <% if (((List<Student>)request.getAttribute("students")).size() == 0) {%>
        <h1>У студента нет оценок</h1>
    <% } else {%>
        <form action="myServlet"><input name="getParametr" type="hidden" value="deleteMarks">
            <input name="id" type="hidden" value="${id}">
            <% for (Student student : (List<Student>)request.getAttribute("students")) {
            out.println("<table><tr><td>" + student + "</td>" +
                "<td><input align=\"right\" type=\"checkbox\" name=\"markId\" value=" + student.getMarkId() + "></td>" +
                "</tr>");
                }
                    out.println("<tr><td align=\"right\" colspan=\"2\"><input type=\"submit\" value=\"Delete selected marks\"></td>" +
                        "</tr>" +
                    "</table>");
            %>
        </form>
        <%}%>
        <form action="myServlet"><input name="getParametr" type="hidden" value="insertMark">
            <input name="id" type="hidden" value="${id}">
            <table border="1" cellspacing="0"><coption> Insert mark</coption>
                <tr>
                    <td>Subject</td>
                    <td> <select name="subject">
                <% for (Subject subject : (List<Subject>)request.getAttribute("subjects")) {
                    out.println("<option value=\"" + subject.getId() + "\">" + subject.getName() + "</option>");
                    }
                    %>
                        </select>
                    </td>
                </tr>
                <tr>
                     <td>Mark</td>
                     <td>
                        <select name="mark">
                            <option value="1">1</option>
                            <option value="2">2</option>
                            <option value="3">3</option>
                            <option value="4">4</option>
                            <option value="5">5</option>
                            <option value="6">6</option>
                            <option value="7">7</option>
                            <option value="8">8</option>
                            <option value="9">9</option>
                            <option value="10">10</option>
                        </select>
                     </td>
                </tr>
                <tr>
                    <td align="right" colspan="2"><input type="submit" value="Insert"></td>
                </tr>
            </table>
            </form>



</body>
</html>
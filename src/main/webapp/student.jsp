<%@page language="java" contentType="text/html" pageEncoding="UTF-8" isELIgnored="false"%>
<html>
<head>
</head>
<body>
    <table>
        <tr>
            <td> ${student} </td>
        </tr>
    </table>
    <table>
            <td>
                <input type="button" value="Info student" onClick='location.href="myServlet?getParametr=getStudentWithMarkAndSubject&id=${id}"'>

            </td>
            <td>
                <input type="button" value="Update" onClick='location.href="myServlet?getParametr=pageForUpdating&id=${id}"'>
            </td>
            <td>
                <input type="button" value="Delete" onClick='location.href="myServlet?getParametr=deleteStudent&id=${id}"'>
            </td><td><input type="button" onclick="history.back()" value="Back"></td>
    </table>


</body>
</html>
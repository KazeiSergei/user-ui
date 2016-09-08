<%@page language="java" contentType="text.html" pageEncoding="UTF-8" isELIgnored="false"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>Page for inserting</title>
</head>
<body>
    <h2>Create student</h2>
    <form action="myServlet">
        <table>
            <tr>
                <td><input name="getParametr" type="hidden" value="insertStudentToDB">Имя</td>
                <td><input type="text" name="firstName" required value="${name}"></td>
                <td><font color="red">${invalidName}</font></td>
            </tr>
            <tr>
                <td>Фамилия</td>
                <td><input type="text" name="SecondName" required value="${surName}"></td>
                <td><font color="red">${invalidSurName}</font></td>
            </tr>
            <tr>
                <td align="right" colspan="2"><input type="submit" value="Send"></td>
            </tr>
        </table>

    </form>
</body>
</html>
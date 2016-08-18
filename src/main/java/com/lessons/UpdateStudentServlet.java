package com.lessons;


import com.courses.db.dto.Student;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class UpdateStudentServlet extends BaseServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset = utf-8");

        String name = request.getParameter("firstName");
        String surName = request.getParameter("SecondName");
        Student student = new Student(name, surName);
        studentDao.updateStudent(student, Integer.parseInt(request.getParameter("id")));

        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title> Hellow Servlet </title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1> Студент изменен </h1>");
        out.println("</body>");
        out.println("</html>");
        studentDao.close();
    }
}

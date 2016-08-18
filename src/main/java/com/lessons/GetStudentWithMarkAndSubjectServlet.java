package com.lessons;


import com.courses.db.dto.Student;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class GetStudentWithMarkAndSubjectServlet extends BaseServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset = utf-8");



        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title> Hellow Servlet </title>");
        out.println("</head>");
        out.println("<body>");
        List<Student> students = studentDao.getStudentWithMark(Integer.parseInt(request.getParameter("id")));
        for (Student student : students) {
            out.println("<table><tr>" + student + "</tr></table>");
        }

        //studentDao.getStudentWithMark(5).forEach(student -> out.println("<h3>" + student + "</h3>"));
        out.println("</body>");
        out.println("</html>");

    }


}

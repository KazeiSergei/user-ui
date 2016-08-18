package com.lessons;




import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class DelateStudentServlet extends BaseServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset = utf-8");


        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title> Hellow Servlet </title>");
        out.println("</head>");
        out.println("<body>");
        studentDao.delateStudent(Integer.parseInt(request.getParameter("id")));
        out.println("<h1> Студент удален </h1>");
        out.println("</body>");
        out.println("</html>");
    }
}

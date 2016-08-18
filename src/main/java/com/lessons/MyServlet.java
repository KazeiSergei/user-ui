package com.lessons;


import com.courses.db.dto.Mark;
import com.courses.db.dto.Student;
import com.courses.db.dto.Subject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyServlet extends BaseServlet {
    private static final String GET_ALL_STUDENT = "getAllStudents";
    private static final String GET_STUDENT_WITH_MARK_AND_SUBJECT = "getStudentWithMarkAndSubject";
    private static final String INSERT_STUDENT = "insertStudent";
    private static final String DELATE_STUDENT = "delateStudent";
    private static final String UPDATE_STUDENT = "updateStudent";
    private static final String GET_STUDENT_BY_ID = "getStudentById";
    private static final String UPDATE_STUDENT_PAGE = "updateStudentPage";
    private static final String INSERT_MARK = "insertMark";
    private static final String INSERT_STUDENT_PAGE = "insertStudentPage";


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("getParametr") == null) {
            getAllStudent(request, response);
        } else {
            switch (request.getParameter("getParametr")) {
                case GET_ALL_STUDENT:
                    getAllStudent(request, response);
                    break;
                case GET_STUDENT_WITH_MARK_AND_SUBJECT:
                    getStudentWithMarkAndSubject(request, response);
                    break;
                case INSERT_STUDENT:
                    insertStudent(request, response);
                    break;
                case DELATE_STUDENT:
                    delateStudent(request, response);
                    break;
                case UPDATE_STUDENT:
                    updateStudent(request, response);
                    break;
                case GET_STUDENT_BY_ID:
                    getStudentById(request, response);
                    break;
                case UPDATE_STUDENT_PAGE:
                    updateStudentPage(request, response);
                    break;
                case INSERT_MARK:
                    insertMark(request, response);
                    break;
                case INSERT_STUDENT_PAGE:
                    insertStudentPage(request, response);
                    break;
                case "":
                    getAllStudent(request, response);
                    break;

            }
        }


    }

    private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset = utf-8");
        String id = request.getParameter("id");
        String name = request.getParameter("firstName");
        String surName = request.getParameter("SecondName");
        if (checkWithRegExp(name) & checkWithRegExp(surName)) {
            Student student = new Student(name, surName);
            studentDao.updateStudent(student, Integer.parseInt(id));
            getStudentById(request, response);
        } else {
            updateStudentPage(request, response);

        }

    }

    private void delateStudent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset = utf-8");
        String id = request.getParameter("id");
        studentDao.delateStudent(Integer.parseInt(id));
        response.sendRedirect(request.getContextPath() + "/" + request.getServletPath() + "?deleted=true");


    }

    protected void insertStudent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset = utf-8");

        String name = request.getParameter("firstName");
        String surName = request.getParameter("SecondName");
        boolean nameValid = checkWithRegExp(name);
        boolean surNameValid = checkWithRegExp(surName);
        if (nameValid & surNameValid) {
            Student student = new Student(name, surName);
            studentDao.insertStudent(student);
            response.sendRedirect(request.getContextPath() + "/myServlet?inserted=true");
        } else {
            insertStudentPage(request, response, nameValid, surNameValid);
        }

    }

    protected void getAllStudent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title> Hellow Servlet </title>");
        out.println("</head>");
        out.println("<body>");

        List<Student> students = studentDao.getAll();
        for (Student student : students) {
            out.println("<table><tr><td><a href=\"myServlet?getParametr=getStudentById&id=" + student.getId() + "\"<br>" + student + "</a>");

            out.println("</tr></table>");
        }
        out.println("<input type=\"button\" value=\"Create\" onClick='location.href=\"myServlet?getParametr=insertStudentPage\"'>");
        out.println("<input type=\"button\" onclick=\"history.back()\" value=\"Back\">");
        if (request.getParameter("deleted") != null) {
            out.println("<h1> Student was deleted</h1>");
        }
        if (request.getParameter("inserted") != null) {
            out.println("<h1> Student was added</h1>");
        }
        out.println("</body>");
        out.println("</html>");
    }

    protected void getStudentWithMarkAndSubject(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset = utf-8");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title> Hellow Servlet </title>");
        out.println("</head>");
        out.println("<body>");
        int id = Integer.parseInt(request.getParameter("id"));
        List<Student> students = studentDao.getStudentWithMark(id);
        if (students.size() == 0) {
            out.println("<h1>У студента нет оценок</h1>");
        } else {
            for (Student student : students) {
                out.println("<table><tr>" + student + "</tr></table>");
            }
        }

        out.println("<form action=\"myServlet\"><input name=\"getParametr\" type=\"hidden\" value=\"insertMark\">" +
                "<input name=\"id\" type=\"hidden\" value=\"" + id + "\">" +
                "<table border=\"1\" cellspacing=\"0\"><coption> Insert mark</coption>" +
                "<tr>" +
                "<td>Subject</td>" +
                "<td> <select name=\"subject\">");
        List<Subject> subjects = subjectDao.getAllSudject();
        for (Subject subject : subjects) {
            out.println("<option value=\"" + subject.getId() + "\">" + subject.getName() + "</option>");
        }
        out.println("</select></td>" +
                "<tr>" +
                "<td>Mark</td>" +
                "<td><select name=\"mark\">" +
                "<option value=\"1\">1</option>\"" +
                "<option value=\"2\">2</option>\"" +
                "<option value=\"3\">3</option>\"" +
                "<option value=\"4\">4</option>\"" +
                "<option value=\"5\">5</option>\"" +
                "<option value=\"6\">6</option>\"" +
                "<option value=\"7\">7</option>\"" +
                "<option value=\"8\">8</option>\"" +
                "<option value=\"9\">9</option>\"" +
                "<option value=\"10\">10</option>\"" +
                "</select></td>" +
                "<tr><td align=\"right\" colspan=\"2\"><input type=\"submit\" value=\"Insert\"></td></tr>" +
                "</table></form>");
        //out.println("<input type=\"button\" onclick=\"history.back()\" value=\"Back\">");
        out.println("</body>");
        out.println("</html>");
    }

    protected void getStudentById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset = utf-8");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title> Hellow Servlet </title>");
        out.println("</head>");
        out.println("<body>");
        int id = Integer.parseInt(request.getParameter("id"));
        Student student = studentDao.getStudentById(id);
        out.println("<table><tr><td>" + student + "</td>");
        if (request.getParameter("getParametr").equals("updateStudent")) {
            out.println("<td><font color=\"red\"> Студент изменен</font></td>");
        }
        out.println("</tr></table>");

        out.println("<table><td>\n" +
                //   "        <form>\n" +
                "            <input type=\"button\" value=\"Info student\" onClick='location.href=\"myServlet?getParametr=getStudentWithMarkAndSubject&id=" + id + "\"'<br>" +
                //  "        </form>\n" +
                "    </td>\n" +
                "<td>\n" +
                //   "        <form>\n" +
                "            <input type=\"button\" value=\"Update\" onClick='location.href=\"myServlet?getParametr=updateStudentPage&id=" + id + "\"'<br>" +
                //  "        </form>\n" +
                "    </td>\n " +
                "<td>\n" +
                //  "        <form>\n" +
                "            <input type=\"button\" value=\"Delate\" onClick='location.href=\"myServlet?getParametr=delateStudent&id=" + id + "\"'<br>" +
                //  "        </form>\n" +
                "    </td><td><input type=\"button\" onclick=\"history.back()\" value=\"Back\"></td> </table>");
        out.println("</body>");
        out.println("</html>");
    }

    protected void updateStudentPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset = utf-8");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title> Hellow Servlet </title>");
        out.println("</head>");
        out.println("<body>");
        String id = request.getParameter("id");
        out.println("<form action=\"myServlet\">\n" +
                "    <table>\n" +
                "        <tr>\n" +
                "            <td><input name=\"getParametr\" type=\"hidden\" value=\"updateStudent\"></td>\n" +
                "            <td><input type=\"hidden\" name=\"id\" value=\"" + id + "\"></td>\n" +
                "<td></td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td>Имя</td>\n" +
                "            <td><input type=\"text\" name=\"firstName\" required></td>\n" +
                "            <td>");

        if (request.getParameter("getParametr").equals(UPDATE_STUDENT)) {
            if (!checkWithRegExp(request.getParameter("firstName"))) {
                out.println("<font color=\"red\">Неправильно введено имя</font>");
            }
        }

        out.println("       </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td>Фамилия</td>\n" +
                "            <td><input type=\"text\" name=\"SecondName\" required></td>\n" +
                "            <td>");
        if (request.getParameter("getParametr").equals(UPDATE_STUDENT)) {
            if (!checkWithRegExp(request.getParameter("SecondName"))) {
                out.println("<font color=\"red\">Неправильно введена фамилия</font>");
            }
        }

        out.println("        </td></tr>\n" +
                "        <tr>\n" +
                "            <td align=\"right\" colspan=\"2\"><input type=\"submit\" value=\"Send\"></td>\n" +
                "        </tr>\n" +
                "\n" +
                "    </table>\n" +
                "</form>");
        out.println("</body>");
        out.println("</html>");

    }

    protected void insertMark(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset = utf-8");
        int studentId = Integer.parseInt(request.getParameter("id"));
        int subjectId = Integer.parseInt(request.getParameter("subject"));
        int markId = Integer.parseInt(request.getParameter("mark"));
        Mark mark = new Mark(studentId, subjectId, markId);
        markDao.insertMark(mark);

        
        response.sendRedirect(request.getContextPath() + "/" +
        request.getServletPath() + "?getParametr=getStudentWithMarkAndSubject&id=" + studentId);
    }

    protected void getTheStartPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        getAllStudent(request, response);
    }

    protected void insertStudentPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        insertStudentPage(request, response, true, true);
    }

    protected void insertStudentPage(HttpServletRequest request, HttpServletResponse response, boolean nameValid, boolean surnameValid) throws ServletException, IOException {
        response.setContentType("text/html;charset = utf-8");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title> Hellow Servlet </title>");
        out.println("</head>");
        out.println("<body>");
        String name = request.getParameter("firstName");
        String surName = request.getParameter("SecondName");
        if(name == null){
            name = "";
        }
        if(surName == null){
            surName = "";
        }
        out.println("<h2>Create students</h2>\n" +
                "<form action=\"myServlet\">\n" +
                "    <table>\n" +
                "        <tr>\n" +
                "            <td><input name=\"getParametr\" type=\"hidden\" value=\"insertStudent\">Имя</td>\n" +
                "            <td><input type=\"text\" name=\"firstName\" required value=\"" + name +
                "\"></td>\n" +
                "            <td>");

        if (!nameValid) {
            out.println("<font color=\"red\">Неправильно введено имя</font>");
        }

        out.println("       </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td>Фамилия</td>\n" +
                "            <td><input type=\"text\" name=\"SecondName\" required value=\"" + surName +
                "\"></td>\n" +
                "            <td>");
        if (!surnameValid) {
            out.println("<font color=\"red\">Неправильно введена фамилия</font>");
        }

        out.println("        </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td align=\"right\" colspan=\"2\"><input type=\"submit\" value=\"Send\"></td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td><input type=\"button\" onclick=\"history.back()\" value=\"Back\"></tr></td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "\n" +
                "</form>");
        out.println("</body>");
        out.println("</html>");
    }

    protected boolean checkWithRegExp(String string) {
        Pattern p = Pattern.compile("^[A-Z][a-z]{1,255}$");
        Matcher m = p.matcher(string);
        return m.matches();
    }

}

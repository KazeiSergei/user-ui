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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyServlet extends BaseServlet {
    private static final String GET_ALL_STUDENT = "getAllStudents";
    private static final String GET_STUDENT_WITH_MARK_AND_SUBJECT = "getStudentWithMarkAndSubject";
    private static final String INSERT_STUDENT_TO_DB = "insertStudentToDB";
    private static final String DELETE_STUDENT = "deleteStudent";
    private static final String UPDATE_STUDENT_IN_DB = "updateStudentInDB";
    private static final String GET_STUDENT_BY_ID = "getStudentById";
    private static final String PAGE_FOR_UPDATING = "pageForUpdating";
    private static final String INSERT_MARK = "insertMark";
    private static final String PAGE_FOR_INSERTING = "pageForInserting";
    private static final String DELETE_MARKS = "deleteMarks";


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
                case INSERT_STUDENT_TO_DB:
                    insertStudentToDB(request, response);
                    break;
                case DELETE_STUDENT:
                    deleteStudent(request, response);
                    break;
                case UPDATE_STUDENT_IN_DB:
                    updateStudentInDB(request, response);
                    break;
                case GET_STUDENT_BY_ID:
                    getStudentById(request, response);
                    break;
                case PAGE_FOR_UPDATING:
                    pageForUpdating(request, response);
                    break;
                case INSERT_MARK:
                    insertMark(request, response);
                    break;
                case PAGE_FOR_INSERTING:
                    pageForInserting(request, response);
                    break;
                case "":
                    getAllStudent(request, response);
                    break;
                case DELETE_MARKS:
                    deleteMarks(request, response);
                    break;

            }
        }


    }

    private void updateStudentInDB(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset = utf-8");
        String id = request.getParameter("id");
        String name = request.getParameter("firstName");
        String surName = request.getParameter("SecondName");
        boolean nameValid = checkWithRegExp(name);
        boolean surNameValid = checkWithRegExp(surName);
        if (nameValid && surNameValid) {
            Student student = new Student(name, surName);
            studentDao.updateStudent(student, Integer.parseInt(id));
            response.sendRedirect(request.getContextPath() + "/" +
                    request.getServletPath() + "?getParametr=getStudentById&id=" + id);
        } else {
            pageForUpdating(request, response, nameValid, surNameValid);

        }

    }

    private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset = utf-8");
        String id = request.getParameter("id");
        studentDao.delateStudent(Integer.parseInt(id));
        response.sendRedirect(request.getContextPath() + "/" + request.getServletPath() + "?deleted=true");
    }

    private void deleteMarks(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset = utf-8");
        int id = Integer.parseInt(request.getParameter("id"));
        String[] marksId = request.getParameterValues("markId");
        for (String markId : marksId) {
            markDao.delateMark(Integer.parseInt(markId));
        }
        response.sendRedirect(request.getContextPath() + "/" + request.getServletPath() + "?getParametr=getStudentWithMarkAndSubject&id=" + id);
    }

    protected void insertStudentToDB(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset = utf-8");

        String name = request.getParameter("firstName");
        String surName = request.getParameter("SecondName");
        boolean nameValid = checkWithRegExp(name);
        boolean surNameValid = checkWithRegExp(surName);
        if (nameValid && surNameValid) {
            Student student = new Student(name, surName);
            studentDao.insertStudent(student);
            response.sendRedirect(request.getContextPath() + "/myServlet?inserted=true");
        } else {
            pageForInserting(request, response, nameValid, surNameValid);
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
        out.println("<input type=\"button\" value=\"Create\" onClick='location.href=\"myServlet?getParametr=pageForInserting\"'>");
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
            out.println("<form action=\"myServlet\"><input name=\"getParametr\" type=\"hidden\" value=\"deleteMarks\">" +
                    "<input name=\"id\" type=\"hidden\" value=\"" + id + "\">");
            for (Student student : students) {
                out.println("<table><tr><td>" + student + "</td>" +
                        "<td><input align=\"right\" type=\"checkbox\" name=\"markId\" value=" + student.getMarkId() + "></td>" +
                        "</tr>");
            }

            out.println("<tr><td align=\"right\" colspan=\"2\"><input type=\"submit\" value=\"Delete selected marks\"></td>" +
                    "</tr>" +
                    "</table></form>");

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
        if (UPDATE_STUDENT_IN_DB.equals(request.getParameter("getParametr"))) {
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
                "            <input type=\"button\" value=\"Update\" onClick='location.href=\"myServlet?getParametr=pageForUpdating&id=" + id + "\"'<br>" +
                //  "        </form>\n" +
                "    </td>\n " +
                "<td>\n" +
                //  "        <form>\n" +
                "            <input type=\"button\" value=\"Delete\" onClick='location.href=\"myServlet?getParametr=deleteStudent&id=" + id + "\"'<br>" +
                //  "        </form>\n" +
                "    </td><td><input type=\"button\" onclick=\"history.back()\" value=\"Back\"></td> </table>");
        out.println("<input type=\"button\" onClick='location.href=\"myServlet\" value=\"Back\">");
        out.println("</body>");
        out.println("</html>");
    }

    protected void pageForUpdating(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        pageForUpdating(request, response, true, true);
    }

    protected void pageForUpdating(HttpServletRequest request, HttpServletResponse response, boolean nameValid, boolean surNameValid) throws ServletException, IOException {
        response.setContentType("text/html;charset = utf-8");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title> Hellow Servlet </title>");
        out.println("</head>");
        out.println("<body>");
        String id = request.getParameter("id");
        String name = request.getParameter("firstName");
        String surName = request.getParameter("SecondName");
        if (name == null) {
            name = "";
        }
        if (surName == null) {
            surName = "";
        }
        out.println("<form action=\"myServlet\">\n" +
                "    <table>\n" +
                "        <tr>\n" +
                "            <td><input name=\"getParametr\" type=\"hidden\" value=\"updateStudentInDB\"></td>\n" +
                "            <td><input type=\"hidden\" name=\"id\" value=\"" + id + "\"></td>\n" +
                "<td></td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td>Имя</td>\n" +
                "            <td><input type=\"text\" name=\"firstName\" required value=\"" + name + "\"></td>\n" +
                "            <td>");

        if (!nameValid) {
            out.println("<font color=\"red\">Неправильно введено имя</font>");
        }


        out.println("       </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td>Фамилия</td>\n" +
                "            <td><input type=\"text\" name=\"SecondName\" required value=\"" + surName + "\"></td>\n" +
                "            <td>");
        if (!surNameValid) {
            out.println("<font color=\"red\">Неправильно введена фамилия</font>");
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
        response.sendRedirect(request.getContextPath() + "/" + request.getServletPath() + "?getParametr=getStudentWithMarkAndSubject&id=" + studentId);
    }


    protected void pageForInserting(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        pageForInserting(request, response, true, true);
    }

    protected void pageForInserting(HttpServletRequest request, HttpServletResponse response, boolean nameValid, boolean surnameValid) throws ServletException, IOException {
        response.setContentType("text/html;charset = utf-8");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title> Hellow Servlet </title>");
        out.println("</head>");
        out.println("<body>");
        String name = request.getParameter("firstName");
        String surName = request.getParameter("SecondName");
        if (name == null) {
            name = "";
        }
        if (surName == null) {
            surName = "";
        }
        out.println("<h2>Create students</h2>\n" +
                "<form action=\"myServlet\">\n" +
                "    <table>\n" +
                "        <tr>\n" +
                "            <td><input name=\"getParametr\" type=\"hidden\" value=\"insertStudentToDB\">Имя</td>\n" +
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

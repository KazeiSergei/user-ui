package com.lessons;


import com.courses.db.dao.MarkDao;
import com.courses.db.dao.StudentDao;
import com.courses.db.dao.SubjectDao;
import com.courses.db.dto.Mark;
import com.courses.db.dto.Student;
import com.courses.db.dto.Subject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainServlet extends HttpServlet {
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
    protected StudentDao studentDao;
    protected SubjectDao subjectDao;
    protected MarkDao markDao;


    @Override
    public void init() throws ServletException {
        studentDao = new StudentDao();
        subjectDao = new SubjectDao();
        markDao = new MarkDao();

        super.init();
    }

    public void destroy() {
        studentDao.close();
        subjectDao.close();
        markDao.close();
        super.destroy();

    }


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

        List<Student> students = studentDao.getAll();
        request.setAttribute("students", students);

        if (request.getParameter("deleted") != null) {
            String studentWasDeleted = "Student was deleted";
            request.setAttribute("studentWasDeleted", studentWasDeleted);
        } else {
            String studentWasDeleted = "";
            request.setAttribute("studentWasDeleted", studentWasDeleted);
        }
        if (request.getParameter("inserted") != null) {
            String studentWasAdded = "Student was added";
            request.setAttribute("studentWasAdded", studentWasAdded);
        } else {
            String studentWasAdded = "";
            request.setAttribute("studentWasAdded", studentWasAdded);
        }
        RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/index.jsp");
        requestDispatcher.forward(request, response);

    }

    protected void getStudentWithMarkAndSubject(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset = utf-8");

        int id = Integer.parseInt(request.getParameter("id"));
        request.setAttribute("id", id);
        List<Student> students = studentDao.getStudentWithMark(id);
        request.setAttribute("students", students);



        List<Subject> subjects = subjectDao.getAllSudject();
        request.setAttribute("subjects", subjects);



        request.getRequestDispatcher("/getStudentWithMarkAndSubject.jsp").forward(request, response);
    }

    protected void getStudentById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset = utf-8");
        int id = Integer.parseInt(request.getParameter("id"));
        Student student = studentDao.getStudentById(id);
        request.setAttribute("student", student);
        request.setAttribute("id", id);
        request.getRequestDispatcher("/student.jsp").forward(request, response);

    }

    protected void pageForUpdating(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        pageForUpdating(request, response, true, true);
    }

    protected void pageForUpdating(HttpServletRequest request, HttpServletResponse response, boolean nameValid, boolean surNameValid) throws ServletException, IOException {
        response.setContentType("text/html;charset = utf-8");
        String id = request.getParameter("id");
        request.setAttribute("id", id);
        String name = request.getParameter("firstName");
        String surName = request.getParameter("SecondName");
        if (name == null) {
            name = "";
        }
        request.setAttribute("name", name);
        if (surName == null) {
            surName = "";
        }
        request.setAttribute("surName", surName);

        if (!nameValid) {
            String invalidName = "Неправильно введено имя";
            request.setAttribute("invalidName", invalidName);
        } else {
            String invalidName = "";
            request.setAttribute("invalidName", invalidName);
        }

        if (!surNameValid) {
            String invalidSurName = "Неправильно введена фамилия";
            request.setAttribute("invalidSurName", invalidSurName);
        } else {
            String invalidSurName = "";
            request.setAttribute("invalidSurName", invalidSurName);
        }
        request.getRequestDispatcher("/pageForUpdating.jsp").forward(request, response);

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

    protected void pageForInserting(HttpServletRequest request, HttpServletResponse response, boolean nameValid, boolean surNameValid) throws ServletException, IOException {
        response.setContentType("text/html;charset = utf-8");
        String name = request.getParameter("firstName");
        String surName = request.getParameter("SecondName");
        if (name == null) {
            name = "";
        }
        request.setAttribute("name", name);
        if (surName == null) {
            surName = "";
        }
        request.setAttribute("surName", surName);

        if (!nameValid) {
            String invalidName = "Неправильно введено имя";
            request.setAttribute("invalidName", invalidName);
        } else {
            String invalidName = "";
            request.setAttribute("invalidName", invalidName);
        }

        if (!surNameValid) {
            String invalidSurName = "Неправильно введена фамилия";
            request.setAttribute("invalidSurName", invalidSurName);
        } else {
            String invalidSurName = "";
            request.setAttribute("invalidSurName", invalidSurName);
        }
        request.getRequestDispatcher("/pageForInserting.jsp").forward(request, response);

    }

    protected boolean checkWithRegExp(String string) {
        Pattern p = Pattern.compile("^[A-Z][a-z]{1,255}$");
        Matcher m = p.matcher(string);
        return m.matches();
    }

}

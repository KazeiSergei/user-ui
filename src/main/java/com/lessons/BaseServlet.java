package com.lessons;

import com.courses.db.dao.MarkDao;
import com.courses.db.dao.StudentDao;
import com.courses.db.dao.SubjectDao;
import com.courses.db.dto.Subject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public abstract class BaseServlet extends HttpServlet {
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
}

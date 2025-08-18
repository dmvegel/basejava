package com.javaops.webapp.web;

import com.javaops.webapp.Config;
import com.javaops.webapp.model.Resume;
import com.javaops.webapp.storage.SqlStorage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class ResumeServlet extends HttpServlet {
    private SqlStorage storage;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        String uuid = request.getParameter("uuid");
        List<Resume> resumes = (uuid == null) ? storage.getAllSorted() : List.of(storage.get(uuid));
        request.setAttribute("resumes", resumes);
        getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    public void init() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        Config cfg = Config.getInstance();
        storage = new SqlStorage(cfg.getDbUrl(), cfg.getDbUser(), cfg.getDbPassword());
    }
}
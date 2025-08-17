package com.javaops.webapp.web;

import com.javaops.webapp.Config;
import com.javaops.webapp.storage.SqlStorage;
import com.javaops.webapp.util.HtmlHelper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ResumeServlet extends HttpServlet {
    private SqlStorage storage;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        String uuid = request.getParameter("uuid");
        response.getWriter().write(HtmlHelper.buildPage(storage, uuid));
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
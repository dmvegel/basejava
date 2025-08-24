package com.javaops.webapp.web;

import com.javaops.webapp.Config;
import com.javaops.webapp.model.ContactType;
import com.javaops.webapp.model.Resume;
import com.javaops.webapp.model.SectionType;
import com.javaops.webapp.storage.SqlStorage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class ResumeServlet extends HttpServlet {
    private SqlStorage storage;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        processResume(request);
        response.sendRedirect("resume");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String uuid = request.getParameter("uuid");
        Action action = Action.parseAction(request.getParameter("action"));
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume r = null;
        switch (action) {
            case Action.DELETE:
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case Action.VIEW:
            case Action.EDIT:
                r = storage.get(uuid);
                break;
            case Action.CREATE:
                r = new Resume(UUID.randomUUID().toString(), "");
                request.getSession().setAttribute("resume", r);
                break;
        }
        request.setAttribute("resume", r);
        request.setAttribute("sectionTypeValues", SectionType.values());
        request.setAttribute("contactTypeValues", ContactType.values());
        request.getRequestDispatcher(
                (Action.VIEW.equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
        ).forward(request, response);
    }

    @Override
    public void init() {
        Config cfg = Config.getInstance();
        storage = new SqlStorage(cfg.getDbUrl(), cfg.getDbUser(), cfg.getDbPassword());
    }

    private void processResume(HttpServletRequest request) {
        boolean isCreation = Objects.equals(request.getParameter("action"), Action.CREATE.name());
        String fullName = request.getParameter("fullName").trim();
        Resume r;

        if (isCreation) {
            HttpSession session = request.getSession();
            r = (Resume) session.getAttribute("resume");
            session.removeAttribute("resume");
        } else {
            String uuid = request.getParameter("uuid");
            r = storage.get(uuid);
        }

        if (fullName.isEmpty()) {
            if (isCreation) {
                return;
            }
        } else {
            r.setFullName(fullName);
        }

        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name()).trim();
            if (!value.isEmpty()) {
                r.getContacts().put(type, value);
            } else {
                r.getContacts().remove(type);
            }
        }

        for (SectionType type : SectionType.values()) {
            String value = request.getParameter(type.name()).trim();
            if (!value.isBlank() && type != SectionType.EDUCATION && type != SectionType.EXPERIENCE) {
                r.getSections().put(type, type.convert(value));
            } else {
                r.getSections().remove(type);
            }
        }

        if (isCreation) {
            storage.save(r);
        } else {
            storage.update(r);
        }
    }
}
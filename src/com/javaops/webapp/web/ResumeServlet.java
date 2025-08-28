package com.javaops.webapp.web;

import com.javaops.webapp.Config;
import com.javaops.webapp.model.*;
import com.javaops.webapp.storage.SqlStorage;
import com.javaops.webapp.util.ResumeUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;

public class ResumeServlet extends HttpServlet {
    private SqlStorage storage;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        ResumeUtils.processResume(request, storage);
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
        Resume resume;
        switch (action) {
            case Action.DELETE:
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case Action.VIEW:
            case Action.EDIT:
                resume = storage.get(uuid);
                break;
            case Action.CREATE:
                resume = new Resume(UUID.randomUUID().toString(), "");
                request.getSession().setAttribute("resume", resume);
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is not supported");
        }
        setRequestAttributes(request, resume, action);
        request.getRequestDispatcher(
                (Action.VIEW.equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
        ).forward(request, response);
    }

    @Override
    public void init() {
        Config cfg = Config.getInstance();
        storage = new SqlStorage(cfg.getDbUrl(), cfg.getDbUser(), cfg.getDbPassword());
    }

    private void setRequestAttributes(HttpServletRequest request, Resume resume, Action action) {
        request.setAttribute("resume", resume);
        request.setAttribute("sectionTypeValues", SectionType.values());
        request.setAttribute("contactTypeValues", ContactType.values());
        switch (action) {
            case CREATE, EDIT -> {
                Map<SectionType, List<CompanyBlock>> companyBlocks = new LinkedHashMap<>();

                for (SectionType type : new SectionType[]{SectionType.EXPERIENCE, SectionType.EDUCATION}) {
                    CompanySection section = (CompanySection) resume.getSections().get(type);
                    if (section == null) {
                        section = new CompanySection();
                        section.setBlocks(new ArrayList<>());
                        resume.getSections().put(type, section);
                    }
                    CompanyBlock emptyBlock = new CompanyBlock();
                    emptyBlock.setPeriods(List.of(new Period()));
                    List<CompanyBlock> allBlocks = section.getBlocks();
                    for (CompanyBlock block : allBlocks) {
                        block.getPeriods().add(new Period());
                    }
                    section.getBlocks().add(emptyBlock);

                    companyBlocks.put(type, section.getBlocks());
                }
                request.setAttribute("companyBlocks", companyBlocks);
            }
        }
    }
}
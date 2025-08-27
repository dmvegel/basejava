package com.javaops.webapp.web;

import com.javaops.webapp.Config;
import com.javaops.webapp.model.*;
import com.javaops.webapp.storage.SqlStorage;
import com.javaops.webapp.util.ResumeFormBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.*;

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

    private void processResume(HttpServletRequest request) {
        boolean isCreation = Objects.equals(request.getParameter("action"), Action.CREATE.name());
        String fullName = request.getParameter("fullName").trim();
        Resume resume;

        if (isCreation) {
            HttpSession session = request.getSession();
            resume = (Resume) session.getAttribute("resume");
            session.removeAttribute("resume");
        } else {
            String uuid = request.getParameter("uuid");
            resume = storage.get(uuid);
        }

        resume.setFullName(fullName);

        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name()).trim();
            if (!value.isEmpty()) {
                resume.getContacts().put(type, value);
            } else {
                resume.getContacts().remove(type);
            }
        }

        for (SectionType type : SectionType.values()) {
            String[] values = request.getParameterValues(type.name());
            if (values != null && values.length > 0 && !values[0].isBlank()) {
                switch (type) {
                    case PERSONAL, OBJECTIVE:
                        resume.getSections().put(type, new TextSection(values[0]));
                        break;
                    case ACHIEVEMENT, QUALIFICATIONS:
                        resume.getSections().put(type, new ListSection(Arrays.stream(values[0].split("\n")).filter(s -> !s.isBlank()).toList()));
                        break;
                    case EXPERIENCE, EDUCATION:
                        CompanySection section = ResumeFormBuilder.buildCompanySection(type, values, request);
                        if (!section.getBlocks().isEmpty()) {
                            resume.getSections().put(type, section);
                        } else {
                            resume.getSections().remove(type);
                        }
                        break;
                    default:
                        throw new IllegalStateException("Unsupported section type: " + type);
                }
            } else {
                resume.getSections().remove(type);
            }
        }

        if (isCreation) {
            storage.save(resume);
        } else {
            storage.update(resume);
        }
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
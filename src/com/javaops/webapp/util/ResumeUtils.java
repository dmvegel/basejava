package com.javaops.webapp.util;

import com.javaops.webapp.model.*;
import com.javaops.webapp.storage.Storage;
import com.javaops.webapp.web.Action;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ResumeUtils {
    public static void processResume(HttpServletRequest request, Storage storage) {
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
                        CompanySection section = ResumeUtils.buildCompanySection(type, values, request);
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

    public static CompanySection buildCompanySection(SectionType type, String[] values, HttpServletRequest request) {
        String[] urls = request.getParameterValues(type.name() + "url");
        CompanySection section = new CompanySection();
        section.setBlocks(new ArrayList<>());
        for (int i = 0; i < values.length; i++) {
            List<Period> periods = buildPeriods(type, request, i);
            String title = values[i].trim();
            if (!title.isBlank()) {
                String url = urls[i].trim();
                if (!periods.isEmpty()) {
                    section.getBlocks().add(new CompanyBlock(title, url, periods));
                }
            }
        }
        return section;
    }

    private static List<Period> buildPeriods(SectionType type, HttpServletRequest request, int blockIndex) {
        String[] startDates = request.getParameterValues(type.name() + "start" + blockIndex);
        String[] endDates = request.getParameterValues(type.name() + "end" + blockIndex);
        String[] periodTitles = request.getParameterValues(type.name() + "periodTitle" + blockIndex);
        String[] periodTexts = request.getParameterValues(type.name() + "periodText" + blockIndex);

        List<Period> periods = new ArrayList<>();
        for (int j = 0; j < startDates.length; j++) {
            if (!startDates[j].isBlank() && !periodTitles[j].isBlank()) {
                LocalDate endDate = endDates[j].isBlank() ? Period.FOR_NOW : HtmlHelper.parseMonth(endDates[j]);
                periods.add(new Period(HtmlHelper.parseMonth(startDates[j]),
                        endDate,
                        periodTitles[j].trim(),
                        periodTexts[j].trim()));
            }
        }
        return periods;
    }
}

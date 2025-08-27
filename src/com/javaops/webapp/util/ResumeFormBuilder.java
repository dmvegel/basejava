package com.javaops.webapp.util;

import com.javaops.webapp.model.CompanyBlock;
import com.javaops.webapp.model.CompanySection;
import com.javaops.webapp.model.Period;
import com.javaops.webapp.model.SectionType;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ResumeFormBuilder {
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

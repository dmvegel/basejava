package com.javaops.webapp.util;

import com.javaops.webapp.model.ListSection;
import com.javaops.webapp.model.Period;
import com.javaops.webapp.model.Section;
import com.javaops.webapp.model.TextSection;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class HtmlHelper {
    public static String toLink(String name, String ref) {
        return "<a href='" + ref + "'>" + name + "</a>";
    }

    public static String convertDate(LocalDate date) {
        return date == null ? "" : date.equals(Period.FOR_NOW) ? "Сейчас" : date.format(DateTimeFormatter.ofPattern("MM/yyyy"));
    }

    public static String convertDateToInputFormat(LocalDate date) {
        return date == null || date.equals(Period.FOR_NOW) ? "" : date.format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }

    public static String convertSection(Section section) {
        if (section instanceof TextSection) {
            return ((TextSection) section).getText();
        } else if (section instanceof ListSection) {
            return String.join("\n", ((ListSection) section).getTexts());
        }
        throw new IllegalStateException("Unsupported section: " + section.getClass());
    }

    public static LocalDate parseMonth(String value) {
        return (value == null || value.isBlank()) ? Period.FOR_NOW : YearMonth.parse(value).atDay(1);
    }
}

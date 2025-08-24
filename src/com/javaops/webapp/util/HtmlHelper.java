package com.javaops.webapp.util;

import com.javaops.webapp.model.*;

public class HtmlHelper {
    public static String sectionToHtml(SectionType sectionType, Section section) {
        return "<h3>" + sectionType.getTitle() + "</h3><br/>" + "<p>" + convertSection(section);
    }

    private static String convertSection(Section section) {
        if (section instanceof TextSection) {
            return ((TextSection) section).getText();
        } else if (section instanceof ListSection) {
            return "<ul>" + ((ListSection) section).getTexts().stream().reduce("", (a, b) -> a + "<li>" + b + "</li>") + "</ul>";
        } else {
            return "";
        }
    }

    public static String toLink(String name, String ref) {
        return "<a href='" + ref + "'>" + name + "</a>";
    }
}

package com.javaops.webapp.util;

import com.javaops.webapp.model.Resume;
import com.javaops.webapp.storage.Storage;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class HtmlHelper {
    public static String fillResumesTable(Supplier<List<Resume>> resumesToHtml) {
        return createHtmlTable(resumesToHtml.get());
    }

    public static String fillResumesTable(Function<String, Resume> resumeToHtml, String uuid) {
        return createHtmlTable(List.of(resumeToHtml.apply(uuid)));
    }

    private static String createHtmlTable(List<Resume> resumes) {
        StringBuilder result = new StringBuilder();
        result.append("  <table>\n")
                .append("    <tr>\n")
                .append("      <th>UUID</th>\n")
                .append("      <th>fullName</th>\n")
                .append("    </tr>\n");

        for (Resume r : resumes) {
            result.append("   <tr>\n")
                    .append("   <td>").append(r.getUuid()).append("</td>\n")
                    .append("   <td>").append(r.getFullName()).append("</td>\n")
                    .append(" </tr>\n");
        }

        result.append("    </table>\n");
        return result.toString();
    }

    public static String buildPage(Storage storage, String uuid) {
        try {
            return "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<body>\n" +
                    ((uuid == null) ? fillResumesTable(storage::getAllSorted) : HtmlHelper.fillResumesTable(storage::get, uuid)) +
                    "</body>\n" +
                    "</html>";
        } catch (Exception e) {
            return e.toString();
        }
    }
}

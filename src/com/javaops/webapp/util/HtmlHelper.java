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
        String result =
                " <table>\n" +
                        "<tr>\n" +
                        "  <th>UUID</th>\n" +
                        "  <th>fullName</th>\n" +
                        "</tr>\n";
        for (Resume r : resumes) {
            result += "  <tr>\n" +
                    "      <td>" + r.getUuid() + "</td>\n" +
                    "      <td>" + r.getFullName() + "</td>\n" +
                    "    </tr>\n";
        }
        return result + "</table>";
    }

    public static String buildPage(Storage storage, String uuid) {
        String result;
        try {
            result = (uuid == null) ? fillResumesTable(storage::getAllSorted) : HtmlHelper.fillResumesTable(storage::get, uuid);
        } catch (Exception e) {
            result = e.toString();
        }
        return result;
    }
}

package com.javaops.webapp;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class PrintDirectory {
    private static final List<String> EXCLUDE = Arrays.asList(
            ".idea", "lib", "out", "lesson", ".git", ".gitignore"
    );
    private static final String LEVEL_SYMBOL = "\\";
    private static final String USER_DIR = "user.dir";

    public static void main(String[] args) {
        printHierarchy(new File(System.getProperty(USER_DIR)), "");
    }

    private static void printHierarchy(File file, String indent) {
        if (EXCLUDE.contains(file.getName())) {
            return;
        }

        System.out.println(indent + file.getName());

        if (file.isDirectory()) {
            File[] nestedFiles = file.listFiles();
            if (nestedFiles == null) {
                return;
            }
            for (File nestedFile : nestedFiles) {
                printHierarchy(nestedFile, indent + LEVEL_SYMBOL);
            }
        }
    }
}

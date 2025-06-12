package com.javaops.webapp.exception;

public class NoFilesStorageException extends StorageException {
    public static final String NO_FILES_MESSAGE = "there are no files in root directory";

    public NoFilesStorageException(String root) {
        super(NO_FILES_MESSAGE, root);
    }
}

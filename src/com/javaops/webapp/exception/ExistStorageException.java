package com.javaops.webapp.exception;

public class ExistStorageException extends StorageException {
    public static final String RESUME_PRESENT_MESSAGE = "Резюме с uuid = %s уже существует в хранилище";

    public ExistStorageException(String uuid) {
        super(RESUME_PRESENT_MESSAGE, uuid);
    }
}

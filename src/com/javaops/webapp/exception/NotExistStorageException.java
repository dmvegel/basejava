package com.javaops.webapp.exception;

public class NotExistStorageException extends StorageException {
    public static final String RESUME_ABSENT_MESSAGE = "Резюме с uuid = %s отсутствует в хранилище";

    public NotExistStorageException(String uuid) {
        super(RESUME_ABSENT_MESSAGE, uuid);
    }
}

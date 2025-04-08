package com.javaops.webapp.exception;

import com.javaops.webapp.storage.AbstractArrayStorage;

public class NonExistStorageException extends StorageException {
    public static final String RESUME_ABSENT_MESSAGE = "Резюме с uuid = %s отсутствует в хранилище";

    public NonExistStorageException(String uuid) {
        super(RESUME_ABSENT_MESSAGE, uuid);
    }
}

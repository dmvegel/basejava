package com.javaops.webapp.storage;

import com.javaops.webapp.model.Resume;

public abstract class AbstractArrayStorage implements Storage {
    public static final String RESUME_PRESENT_MESSAGE = "Резюме с uuid = %s уже существует в хранилище";
    public static final String RESUME_ABSENT_MESSAGE = "Резюме с uuid = %s отсутствует в хранилище";
    public static final String STORAGE_EXCESS_MESSAGE = "Превышен размер хранилища";
    public static final int STORAGE_LIMIT = 10000;

    protected final Resume[] storage = new Resume[STORAGE_LIMIT];

    protected int size;

    public Resume get(String uuid) {
        int position = getPosition(uuid);
        if (position >= 0) {
            return storage[position];
        }
        System.out.printf((RESUME_ABSENT_MESSAGE) + "%n", uuid);
        return null;
    }

    protected abstract int getPosition(String uuid);
}

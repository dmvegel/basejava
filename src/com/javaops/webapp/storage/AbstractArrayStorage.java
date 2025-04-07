package com.javaops.webapp.storage;

import com.javaops.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage {
    public static final String RESUME_PRESENT_MESSAGE = "Резюме с uuid = %s уже существует в хранилище";
    public static final String RESUME_ABSENT_MESSAGE = "Резюме с uuid = %s отсутствует в хранилище";
    public static final String STORAGE_EXCESS_MESSAGE = "Превышен размер хранилища";
    public static final int STORAGE_LIMIT = 10000;

    protected final Resume[] storage = new Resume[STORAGE_LIMIT];

    protected int size;

    public final void save(Resume r) {
        if (size == storage.length) {
            System.out.println(STORAGE_EXCESS_MESSAGE);
            return;
        }

        int insertPosition = -getPosition(r.getUuid()) - 1;
        if (insertPosition < 0) {
            System.out.printf((RESUME_PRESENT_MESSAGE) + "%n", r.getUuid());
            return;
        }

        insertResume(r, insertPosition);
        size++;
    }

    public final void delete(String uuid) {
        int resumePosition = getPosition(uuid);
        if (resumePosition >= 0) {
            size--;
            deleteResume(resumePosition);
            storage[size] = null;
            return;
        }
        System.out.printf((RESUME_ABSENT_MESSAGE) + "%n", uuid);
    }

    public final void update(Resume r) {
        int resumePosition = getPosition(r.getUuid());
        if (resumePosition < 0) {
            System.out.printf((RESUME_ABSENT_MESSAGE) + "%n", r.getUuid());
            return;
        }
        storage[resumePosition] = r;
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int size() {
        return size;
    }

    public final Resume get(String uuid) {
        int position = getPosition(uuid);
        if (position >= 0) {
            return storage[position];
        }
        System.out.printf((RESUME_ABSENT_MESSAGE) + "%n", uuid);
        return null;
    }

    protected abstract int getPosition(String uuid);

    protected abstract void insertResume(Resume r, int insertPosition);

    protected abstract void deleteResume(int resumePosition);
}

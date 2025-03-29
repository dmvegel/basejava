package com.javaops.webapp.storage;

import com.javaops.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private final Resume[] storage = new Resume[10000];

    public static final String RESUME_PRESENT_MESSAGE = "Резюме с uuid = %s уже существует в хранилище";
    public static final String RESUME_ABSENT_MESSAGE = "Резюме с uuid = %s отсутствует в хранилище";
    public static final String STORAGE_EXCESS_MESSAGE = "Превышен размер хранилища";

    private int size;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void update(Resume r) {
        int resumePosition = getPosition(r.getUuid());
        if (resumePosition < 0) {
            System.out.printf((RESUME_ABSENT_MESSAGE) + "%n", r.getUuid());
            return;
        }
        storage[resumePosition] = r;
    }

    public int getPosition(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].toString().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    public void save(Resume r) {
        if (size == storage.length) {
            System.out.println(STORAGE_EXCESS_MESSAGE);
            return;
        }
        if (getPosition(r.getUuid()) >= 0) {
            System.out.printf((RESUME_PRESENT_MESSAGE) + "%n", r.getUuid());
            return;
        }
        storage[size++] = r;
    }

    public Resume get(String uuid) {
        int resumePosition = getPosition(uuid);
        if (resumePosition >= 0) {
            return storage[resumePosition];
        }
        System.out.printf((RESUME_ABSENT_MESSAGE) + "%n", uuid);
        return null;
    }

    public void delete(String uuid) {
        int resumePosition = getPosition(uuid);
        if (resumePosition >= 0) {
            storage[resumePosition] = storage[--size];
            storage[size] = null;
            return;
        }
        System.out.printf((RESUME_ABSENT_MESSAGE) + "%n", uuid);
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int size() {
        return size;
    }
}

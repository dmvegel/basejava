package com.javaops.webapp.storage;

import com.javaops.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {
    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public void update(Resume r) {
        int resumePosition = getPosition(r.getUuid());
        if (resumePosition < 0) {
            System.out.printf((RESUME_ABSENT_MESSAGE) + "%n", r.getUuid());
            return;
        }
        storage[resumePosition] = r;
    }

    @Override
    public int getPosition(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].toString().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
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

    @Override
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
    @Override
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    @Override
    public int size() {
        return size;
    }
}

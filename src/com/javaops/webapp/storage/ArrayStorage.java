package com.javaops.webapp.storage;

import com.javaops.webapp.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {
    @Override
    protected int getPosition(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -size - 1;
    }

    @Override
    protected void insertResume(Resume r, int insertPosition) {
        storage[insertPosition] = r;
        size++;
    }

    @Override
    protected void deleteResume(int resumePosition) {
        storage[resumePosition] = storage[--size];
        storage[size] = null;
    }
}

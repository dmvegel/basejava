package com.javaops.webapp.storage;

import com.javaops.webapp.model.Resume;

public class ArrayStorage extends AbstractArrayStorage {
    @Override
    protected void insertResume(Resume r, int insertPosition) {
        storage[-insertPosition - 1] = r;
    }

    @Override
    protected void deleteResume(int resumePosition) {
        storage[resumePosition] = storage[size - 1];
    }

    @Override
    protected Integer getSearchKey(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -size - 1;
    }
}

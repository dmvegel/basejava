package com.javaops.webapp.storage;

import com.javaops.webapp.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {
    @Override
    public void save(Resume r) {
        if (size == storage.length) {
            System.out.println(STORAGE_EXCESS_MESSAGE);
            return;
        }

        int position = getPosition(r.getUuid());
        if (position >= 0) {
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

    @Override
    public int getPosition(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -size - 1;
    }
}

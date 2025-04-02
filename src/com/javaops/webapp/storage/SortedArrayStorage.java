package com.javaops.webapp.storage;

import com.javaops.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {
    @Override
    public void save(Resume r) {
        if (size == storage.length) {
            System.out.println(STORAGE_EXCESS_MESSAGE);
            return;
        }

        int position = getPosition(r.getUuid());
        int insertPosition = -position - 1;
        if (position >= 0) {
            System.out.printf((RESUME_PRESENT_MESSAGE) + "%n", r.getUuid());
            return;
        }

        System.arraycopy(storage, insertPosition, storage, insertPosition + 1, size - insertPosition);
        storage[insertPosition] = r;
        size++;
    }

    @Override
    public void delete(String uuid) {
        int resumePosition = getPosition(uuid);
        if (resumePosition >= 0) {
            size--;
            System.arraycopy(storage, resumePosition + 1, storage, resumePosition, size - resumePosition);
            storage[size] = null;
            return;
        }
        System.out.printf((RESUME_ABSENT_MESSAGE) + "%n", uuid);
    }

    @Override
    public int getPosition(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }
}

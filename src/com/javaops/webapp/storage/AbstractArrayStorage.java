package com.javaops.webapp.storage;

import com.javaops.webapp.exception.StorageException;
import com.javaops.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage<T extends Integer> extends AbstractStorage<T> {
    public static final String STORAGE_EXCESS_MESSAGE = "Превышен размер хранилища";
    public static final int STORAGE_LIMIT = 10000;

    protected final Resume[] storage = new Resume[STORAGE_LIMIT];

    @Override
    protected Resume doGet(T searchKey) {
        return storage[searchKey.intValue()];
    }

    @Override
    protected void doSave(Resume resume, T searchKey) {
        if (size == storage.length) {
            throw new StorageException(resume.getUuid(), STORAGE_EXCESS_MESSAGE);
        }
        insertResume(resume, searchKey);
    }

    @Override
    protected void doUpdate(Resume resume, T searchKey) {
        storage[searchKey.intValue()] = resume;
    }

    @Override
    protected void doDelete(T searchKey) {
        deleteResume(searchKey);
        storage[size] = null;
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    protected boolean isExist(Integer searchKey) {
        return searchKey >= 0;
    }

    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    protected abstract void insertResume(Resume r, int insertPosition);

    protected abstract void deleteResume(int resumePosition);
}

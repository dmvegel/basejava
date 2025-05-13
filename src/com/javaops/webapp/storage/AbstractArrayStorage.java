package com.javaops.webapp.storage;

import com.javaops.webapp.exception.StorageException;
import com.javaops.webapp.model.Resume;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage<Integer> {
    public static final String STORAGE_EXCESS_MESSAGE = "Превышен размер хранилища";
    public static final int STORAGE_LIMIT = 10000;

    protected int size;

    protected final Resume[] storage = new Resume[STORAGE_LIMIT];

    @Override
    protected Resume doGet(Integer searchKey) {
        return storage[searchKey];
    }

    @Override
    protected void doSave(Resume resume, Integer searchKey) {
        if (size == storage.length) {
            throw new StorageException(resume.getUuid(), STORAGE_EXCESS_MESSAGE);
        }
        insertResume(resume, searchKey);
        size++;
    }

    @Override
    protected void doUpdate(Resume resume, Integer searchKey) {
        storage[searchKey] = resume;
    }

    @Override
    protected void doDelete(Integer searchKey) {
        deleteResume(searchKey);
        storage[size] = null;
        size--;
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    protected boolean isExist(Integer searchKey) {
        return searchKey >= 0;
    }

    public int size() {
        return size;
    }

    @Override
    protected List<Resume> doCopyAll() {
        return Arrays.stream(Arrays.copyOf(storage, size)).toList();
    }

    protected abstract void insertResume(Resume r, int insertPosition);

    protected abstract void deleteResume(int resumePosition);
}

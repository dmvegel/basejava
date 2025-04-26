package com.javaops.webapp.storage;

import com.javaops.webapp.exception.ExistStorageException;
import com.javaops.webapp.exception.NotExistStorageException;
import com.javaops.webapp.model.Resume;

public abstract class AbstractStorage<T> implements Storage {
    protected int size;

    @Override
    public Resume get(String uuid) {
        T searchKey = getExistingSearchKey(uuid);
        return doGet(searchKey);
    }

    @Override
    public void save(Resume r) {
        T searchKey = getNotExistingSearchKey(r.getUuid());
        doSave(r, searchKey);
        size++;
    }

    @Override
    public void update(Resume r) {
        T searchKey = getExistingSearchKey(r.getUuid());
        doUpdate(r, searchKey);
    }

    @Override
    public void delete(String uuid) {
        T searchKey = getExistingSearchKey(uuid);
        doDelete(searchKey);
        size--;
    }

    public int size() {
        return size;
    }

    protected T getExistingSearchKey(String uuid) {
        T searchKey = getSearchKey(uuid);
        if (isExist(searchKey))
            return searchKey;
        throw new NotExistStorageException(uuid);
    }

    protected T getNotExistingSearchKey(String uuid) {
        T searchKey = getSearchKey(uuid);
        if (!isExist(searchKey))
            return searchKey;
        throw new ExistStorageException(uuid);
    }

    protected abstract Resume doGet(T searchKey);

    protected abstract void doSave(Resume resume, T searchKey);

    protected abstract void doUpdate(Resume resume, T searchKey);

    protected abstract void doDelete(T searchKey);

    protected abstract T getSearchKey(String uuid);

    protected abstract boolean isExist(T searchKey);
}

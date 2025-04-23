package com.javaops.webapp.storage;

import com.javaops.webapp.model.Resume;

public abstract class AbstractStorage<T> implements Storage {
    T searchKey;

    @Override
    public Resume get(String uuid) {
        return doGet(searchKey);
    }

    @Override
    public void save(Resume r) {
        doSave(r, searchKey);
        size++;
    }

    @Override
    public void update(Resume r) {
        doUpdate(r, searchKey);
    }

    @Override
    public void delete(String uuid) {
        doDelete(searchKey);
        size--;
    }

    protected abstract Resume doGet(T searchKey);

    protected abstract void doSave(Resume resume, T searchKey);

    protected abstract void doUpdate(Resume resume, T searchKey);

    protected abstract void doDelete(T searchKey);

    protected int size;

    public int size() {
        return size;
    }
}

package com.javaops.webapp.storage;

import com.javaops.webapp.model.Resume;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ListStorage extends AbstractStorage<Integer> {
    private final List<Resume> storage;

    public ListStorage() {
        this.storage = new ArrayList<>();
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    protected Resume doGet(Integer searchKey) {
        return storage.get(searchKey);
    }

    @Override
    protected void doSave(Resume resume, Integer searchKey) {
        storage.add(resume);
    }

    @Override
    protected void doUpdate(Resume resume, Integer searchKey) {
        storage.set(searchKey, resume);
    }

    @Override
    protected void doDelete(Integer searchKey) {
        storage.remove(searchKey.intValue());
    }

    @Override
    protected Integer getSearchKey(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected boolean isExist(Integer searchKey) {
        return searchKey >= 0;
    }

    @Override
    public List<Resume> getAllSorted() {
        return storage.stream().sorted(Comparator.comparing(Resume::getFullName)).toList();
    }

    @Override
    public int size() {
        return storage.size();
    }
}

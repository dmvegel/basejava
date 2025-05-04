package com.javaops.webapp.storage;

import com.javaops.webapp.model.Resume;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapUuidStorage extends AbstractStorage<String> {
    private final Map<String, Resume> map;

    public MapUuidStorage() {
        this.map = new HashMap<>();
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    protected Resume doGet(String searchKey) {
        return map.get(searchKey);
    }

    @Override
    protected void doSave(Resume resume, String searchKey) {
        map.put(searchKey, resume);
    }

    @Override
    protected void doUpdate(Resume resume, String searchKey) {
        map.put(searchKey, resume);
    }

    @Override
    protected void doDelete(String searchKey) {
        map.remove(searchKey);
    }

    @Override
    protected String getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected boolean isExist(String searchKey) {
        return map.containsKey(searchKey);
    }

    @Override
    public List<Resume> getAllSorted() {
        return map.values().stream().sorted(Comparator.comparing(Resume::getFullName)).toList();
    }

    @Override
    public int size() {
        return map.size();
    }
}

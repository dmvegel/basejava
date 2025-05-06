package com.javaops.webapp.storage;

import com.javaops.webapp.model.Resume;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapResumeStorage extends AbstractStorage<Resume> {
    private final Map<String, Resume> map;

    public MapResumeStorage() {
        this.map = new HashMap<>();
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    protected Resume doGet(Resume searchKey) {
        return searchKey;
    }

    @Override
    protected void doSave(Resume resume, Resume searchKey) {
        map.put(resume.getUuid(), resume);
    }

    @Override
    protected void doUpdate(Resume resume, Resume searchKey) {
        map.put(resume.getUuid(), resume);
    }

    @Override
    protected void doDelete(Resume searchKey) {
        map.remove(searchKey.getUuid());
    }

    @Override
    protected Resume getSearchKey(String uuid) {
        return map.get(uuid);
    }

    @Override
    protected boolean isExist(Resume searchKey) {
        if (searchKey == null) return false;
        return map.containsKey(searchKey.getUuid());
    }

    @Override
    public List<Resume> getAllSorted() {
        return map.values().stream().sorted(SORT_COMPARATOR).toList();
    }

    @Override
    public int size() {
        return map.size();
    }
}

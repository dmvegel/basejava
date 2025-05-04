package com.javaops.webapp.storage;

import com.javaops.webapp.model.Resume;

import java.util.List;

public interface Storage {
    void clear();

    Resume get(String uuid);

    void save(Resume r);

    void update(Resume r);

    void delete(String uuid);

    List<Resume> getAllSorted();

    int size();
}

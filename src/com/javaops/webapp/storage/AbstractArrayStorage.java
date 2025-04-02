package com.javaops.webapp.storage;

import com.javaops.webapp.model.Resume;

public abstract class AbstractArrayStorage implements Storage {
    public static final int STORAGE_LIMIT = 10000;

    protected final Resume[] storage = new Resume[STORAGE_LIMIT];

    protected int size;
}

package com.javaops.webapp.storage;

public abstract class AbstractStorage implements Storage {

    protected int size;

    public int size() {
        return size;
    }
}

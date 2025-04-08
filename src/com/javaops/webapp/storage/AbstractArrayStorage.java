package com.javaops.webapp.storage;

import com.javaops.webapp.exception.ExistStorageException;
import com.javaops.webapp.exception.NonExistStorageException;
import com.javaops.webapp.exception.StorageException;
import com.javaops.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage {
    public static final String STORAGE_EXCESS_MESSAGE = "Превышен размер хранилища";
    public static final int STORAGE_LIMIT = 10000;

    protected final Resume[] storage = new Resume[STORAGE_LIMIT];

    protected int size;

    public final void save(Resume r) {
        if (size == storage.length) {
            throw new StorageException(r.getUuid(), STORAGE_EXCESS_MESSAGE);
        }

        int insertPosition = -getPosition(r.getUuid()) - 1;
        if (insertPosition < 0) {
            throw new ExistStorageException(r.getUuid());
        }

        insertResume(r, insertPosition);
        size++;
    }

    public final void delete(String uuid) {
        int resumePosition = getPosition(uuid);
        if (resumePosition >= 0) {
            size--;
            deleteResume(resumePosition);
            storage[size] = null;
            return;
        }
        throw new NonExistStorageException(uuid);
    }

    public final void update(Resume r) {
        int resumePosition = getPosition(r.getUuid());
        if (resumePosition < 0) {
            throw new NonExistStorageException(r.getUuid());
        }
        storage[resumePosition] = r;
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int size() {
        return size;
    }

    public final Resume get(String uuid) {
        int position = getPosition(uuid);
        if (position >= 0) {
            return storage[position];
        }
        throw new NonExistStorageException(uuid);
    }

    protected abstract int getPosition(String uuid);

    protected abstract void insertResume(Resume r, int insertPosition);

    protected abstract void deleteResume(int resumePosition);
}

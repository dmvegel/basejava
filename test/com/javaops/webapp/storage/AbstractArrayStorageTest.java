package com.javaops.webapp.storage;

import com.javaops.webapp.exception.NotExistStorageException;
import com.javaops.webapp.exception.StorageException;
import com.javaops.webapp.model.Resume;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AbstractArrayStorageTest {
    private final Storage storage = new ArrayStorage();
    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";

    @BeforeEach
    public void setUp() throws Exception {
        storage.clear();
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_3));
    }

    @Test
    void save() throws Exception {
    }

    @Test
    void delete() throws Exception {
    }

    @Test
    void update() throws Exception {
    }

    @Test
    void clear() {
    }

    @Test
    void getAll() {
    }

    @Test
    void size() {
        Assertions.assertEquals(3, storage.size());
    }

    @Test
    void get() throws Exception {
    }

    @Test
    public void getNotExist() throws Exception {
        Assertions.assertThrows(NotExistStorageException.class, () -> storage.get("dummy"));
    }

    @Test
    public void overflow() {
        try {
            int currSize = storage.size();
            for (int i = 0; i < AbstractArrayStorage.STORAGE_LIMIT - currSize; i++) {
                storage.save(new Resume());
            }
        } catch (StorageException e) {
            Assertions.fail("Переполнение произошло раньше времени");
        }
        Assertions.assertThrows(StorageException.class, () -> storage.save(new Resume()));
    }
}
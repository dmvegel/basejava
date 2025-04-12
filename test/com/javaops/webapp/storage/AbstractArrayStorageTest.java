package com.javaops.webapp.storage;

import com.javaops.webapp.exception.ExistStorageException;
import com.javaops.webapp.exception.NotExistStorageException;
import com.javaops.webapp.exception.StorageException;
import com.javaops.webapp.model.Resume;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public abstract class AbstractArrayStorageTest {
    private final Storage storage;
    public static final String EARLY_OVERFLOW_MESSAGE = "Переполнение произошло раньше времени";

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_4 = "uuid4";

    private int initialLength;

    AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @BeforeEach
    public void setUp() {
        storage.clear();
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_3));

        initialLength = storage.size();
    }

    @Test
    public void save() {
        Resume newResume = new Resume(UUID_4);
        this.storage.save(newResume);
        Assertions.assertEquals(initialLength + 1, storage.size());
        Assertions.assertEquals(newResume, storage.get(UUID_4));
    }

    @Test
    public void delete() {
        Assertions.assertEquals(new Resume(UUID_1), storage.get(UUID_1));
        storage.delete(UUID_1);
        Assertions.assertEquals(initialLength - 1, storage.size());
        Assertions.assertThrows(NotExistStorageException.class, () -> storage.get(UUID_1));
    }

    @Test
    public void update() {
        Resume resume = new Resume(UUID_1);
        storage.update(resume);
        Assertions.assertEquals(resume, storage.get(UUID_1));
        Assertions.assertEquals(initialLength, storage.size());
    }

    @Test
    public void clear() {
        Assertions.assertNotEquals(0, storage.size());
        storage.clear();
        Assertions.assertEquals(0, storage.size());
    }

    @Test
    public void getAll() {
        Resume[] allResumes = storage.getAll();
        Assertions.assertEquals(3, allResumes.length);

        Assertions.assertEquals(new Resume(UUID_1), allResumes[0]);
        Assertions.assertEquals(new Resume(UUID_2), allResumes[1]);
        Assertions.assertEquals(new Resume(UUID_3), allResumes[2]);
    }

    @Test
    public void size() {
        Assertions.assertEquals(3, storage.size());
    }

    @Test
    public void get() {
        Assertions.assertEquals(new Resume(UUID_1), storage.get(UUID_1));
        Assertions.assertEquals(new Resume(UUID_2), storage.get(UUID_2));
        Assertions.assertEquals(new Resume(UUID_3), storage.get(UUID_3));
    }

    @Test
    public void getNotExist() {
        Assertions.assertThrows(NotExistStorageException.class, () -> storage.get("dummy"));
    }

    @Test
    public void updateNotExist() {
        Assertions.assertThrows(NotExistStorageException.class, () -> storage.update(new Resume("dummy")));
    }

    @Test
    public void deleteNotExist() {
        Assertions.assertThrows(NotExistStorageException.class, () -> storage.delete("dummy"));
    }

    @Test
    public void saveExist() {
        Assertions.assertThrows(ExistStorageException.class, () -> storage.save(new Resume(UUID_1)));
    }

    @Test
    public void overflow() {
        try {
            int currSize = storage.size();
            for (int i = 0; i < AbstractArrayStorage.STORAGE_LIMIT - currSize; i++) {
                storage.save(new Resume());
            }
        } catch (StorageException e) {
            Assertions.fail(EARLY_OVERFLOW_MESSAGE);
        }
        Assertions.assertThrows(StorageException.class, () -> storage.save(new Resume()));
    }
}
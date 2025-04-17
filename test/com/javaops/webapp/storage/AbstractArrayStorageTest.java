package com.javaops.webapp.storage;

import com.javaops.webapp.exception.ExistStorageException;
import com.javaops.webapp.exception.NotExistStorageException;
import com.javaops.webapp.exception.StorageException;
import com.javaops.webapp.model.Resume;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {
    public static final String EARLY_OVERFLOW_MESSAGE = "Переполнение произошло раньше времени";

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_4 = "uuid4";
    private static final String UUID_NOT_EXIST = "dummy";
    private static final int INITIAL_SIZE = 3;

    private static final Resume RESUME_1 = new Resume(UUID_1);
    private static final Resume RESUME_2 = new Resume(UUID_2);
    private static final Resume RESUME_3 = new Resume(UUID_3);
    private static final Resume RESUME_4 = new Resume(UUID_4);
    private static final Resume DUMMY_RESUME = new Resume(UUID_NOT_EXIST);

    private final Storage storage;

    AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @BeforeEach
    public void setUp() {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    @Test
    public void save() {
        this.storage.save(RESUME_4);
        assertGet(RESUME_4);
        assertSize(INITIAL_SIZE + 1);
    }

    @Test
    public void delete() {
        Assertions.assertEquals(RESUME_1, storage.get(UUID_1));
        storage.delete(UUID_1);
        assertSize(INITIAL_SIZE - 1);
        Assertions.assertThrows(NotExistStorageException.class, () -> assertGet(RESUME_1));
    }

    @Test
    public void update() {
        storage.update(RESUME_1);
        Assertions.assertSame(RESUME_1, storage.get(RESUME_1.getUuid()));
    }

    @Test
    public void clear() {
        Assertions.assertNotEquals(0, storage.size());
        storage.clear();
        Assertions.assertArrayEquals(new Resume[]{}, storage.getAll());
        assertSize(0);
    }

    @Test
    public void getAll() {
        Assertions.assertArrayEquals(new Resume[]{RESUME_1, RESUME_2, RESUME_3}, storage.getAll());
    }

    @Test
    public void size() {
        assertSize(3);
    }

    public void assertSize(int size) {
        Assertions.assertEquals(storage.size(), size);
    }

    @Test
    public void get() {
        assertGet(RESUME_1);
        assertGet(RESUME_2);
        assertGet(RESUME_3);
    }

    public void assertGet(Resume resume) {
        Assertions.assertEquals(resume, storage.get(resume.getUuid()));
    }

    @Test
    public void getNotExist() {
        Assertions.assertThrows(NotExistStorageException.class, () -> storage.get(UUID_NOT_EXIST));
    }

    @Test
    public void updateNotExist() {
        Assertions.assertThrows(NotExistStorageException.class, () -> storage.update(DUMMY_RESUME));
    }

    @Test
    public void deleteNotExist() {
        Assertions.assertThrows(NotExistStorageException.class, () -> storage.delete(UUID_NOT_EXIST));
    }

    @Test
    public void saveExist() {
        Assertions.assertThrows(ExistStorageException.class, () -> storage.save(RESUME_1));
    }

    @Test
    public void saveOverflow() {
        storage.clear();
        for (int i = 0; i < AbstractArrayStorage.STORAGE_LIMIT; i++) {
            storage.save(new Resume());
        }
        Assertions.assertThrows(StorageException.class, () -> storage.save(new Resume()));
    }
}
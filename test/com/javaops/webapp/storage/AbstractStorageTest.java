package com.javaops.webapp.storage;

import com.javaops.webapp.Config;
import com.javaops.webapp.ResumeTestData;
import com.javaops.webapp.exception.ExistStorageException;
import com.javaops.webapp.exception.NotExistStorageException;
import com.javaops.webapp.model.Resume;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public abstract class AbstractStorageTest {
    protected static final String UUID_1 = UUID.randomUUID().toString();
    protected static final String UUID_2 = UUID.randomUUID().toString();
    protected static final String UUID_3 = UUID.randomUUID().toString();
    protected static final String UUID_4 = UUID.randomUUID().toString();
    protected static final String UUID_NOT_EXIST = "dummy";

    protected static final String FULL_NAME_1 = "full_name1";
    protected static final String FULL_NAME_2 = "full_name2";
    protected static final String FULL_NAME_3 = "full_name3";
    protected static final String FULL_NAME_4 = "full_name4";

    protected static final int INITIAL_SIZE = 3;

    protected static final Resume RESUME_1 = ResumeTestData.createResume(UUID_1, FULL_NAME_1);
    protected static final Resume RESUME_2 = ResumeTestData.createResume(UUID_2, FULL_NAME_2);
    protected static final Resume RESUME_3 = ResumeTestData.createResume(UUID_3, FULL_NAME_3);
    protected static final Resume RESUME_4 = ResumeTestData.createResume(UUID_4, FULL_NAME_4);
    protected static final Resume DUMMY_RESUME = ResumeTestData.createResume(UUID_NOT_EXIST, "");

    protected static final File STORAGE_DIR = Config.getInstance().getStorageDir();

    protected final Storage storage;

    AbstractStorageTest(Storage storage) {
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
    public void size() {
        assertSize(3);
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
        Resume newResume = ResumeTestData.createResume(UUID_1, FULL_NAME_2);
        storage.update(newResume);
        Assertions.assertEquals(newResume, storage.get(newResume.getUuid()));
    }

    @Test
    public void clear() {
        Assertions.assertNotEquals(0, storage.size());
        storage.clear();
        Assertions.assertEquals(new ArrayList<Resume>(), storage.getAllSorted());
        assertSize(0);
    }

    @Test
    public void getAllSorted() {
        storage.clear();
        storage.save(RESUME_3);
        storage.save(RESUME_2);
        storage.save(RESUME_1);
        Assertions.assertEquals(Arrays.asList(RESUME_1, RESUME_2, RESUME_3), storage.getAllSorted());
    }

    @Test
    public void get() {
        assertGet(RESUME_1);
        assertGet(RESUME_2);
        assertGet(RESUME_3);
    }

    @Test
    public void getNotExist() {
        getNotExist(UUID_NOT_EXIST);
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
    public void consistentDelete() {
        storage.delete(UUID_1);
        storage.delete(UUID_2);
        storage.delete(UUID_3);

        assertSize(0);
        getNotExist(UUID_1);
        getNotExist(UUID_2);
        getNotExist(UUID_3);
    }

    public void assertSize(int size) {
        Assertions.assertEquals(storage.size(), size);
    }

    public void assertGet(Resume resume) {
        Assertions.assertEquals(resume, storage.get(resume.getUuid()));
    }

    public void getNotExist(String uuid) {
        Assertions.assertThrows(NotExistStorageException.class, () -> storage.get(uuid));
    }

}

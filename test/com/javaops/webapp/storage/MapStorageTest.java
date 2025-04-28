package com.javaops.webapp.storage;

import com.javaops.webapp.model.Resume;
import org.junit.jupiter.api.Assertions;

import java.util.Arrays;
import java.util.Comparator;

public class MapStorageTest extends AbstractStorageTest {
    MapStorageTest() {
        super(new MapStorage());
    }

    @Override
    public void getAll() {
        Resume[] resumes = storage.getAll();
        Arrays.sort(storage.getAll(), Comparator.comparing(Resume::getUuid));
        Assertions.assertArrayEquals(new Resume[]{RESUME_1, RESUME_2, RESUME_3}, resumes);
    }
}

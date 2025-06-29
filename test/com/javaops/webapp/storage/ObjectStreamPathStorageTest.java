package com.javaops.webapp.storage;

public class ObjectStreamPathStorageTest extends AbstractStorageTest {
    ObjectStreamPathStorageTest() {
        super(new ObjectStreamPathStorage(STORAGE_DIR.toPath(), new StandardSerialization()));
    }
}

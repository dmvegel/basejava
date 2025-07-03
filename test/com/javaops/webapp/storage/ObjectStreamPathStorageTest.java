package com.javaops.webapp.storage;

import com.javaops.webapp.storage.serialization.StandardSerialization;

public class ObjectStreamPathStorageTest extends AbstractStorageTest {
    ObjectStreamPathStorageTest() {
        super(new ObjectStreamPathStorage(STORAGE_DIR.getAbsolutePath(), new StandardSerialization()));
    }
}

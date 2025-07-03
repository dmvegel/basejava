package com.javaops.webapp.storage;

import com.javaops.webapp.storage.serialization.StandardSerialization;

public class ObjectStreamStorageTest extends AbstractStorageTest {
    ObjectStreamStorageTest() {
        super(new ObjectStreamStorage(STORAGE_DIR, new StandardSerialization()));
    }
}

package com.javaops.webapp.storage;

import com.javaops.webapp.storage.serialization.ObjectStreamSerialization;

public class FileStorageTest extends AbstractStorageTest {
    FileStorageTest() {
        super(new FileStorage(STORAGE_DIR, new ObjectStreamSerialization()));
    }
}

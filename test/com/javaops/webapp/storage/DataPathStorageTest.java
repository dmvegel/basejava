package com.javaops.webapp.storage;

import com.javaops.webapp.storage.serialization.DataStreamSerialization;

public class DataPathStorageTest extends AbstractStorageTest {
    DataPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new DataStreamSerialization()));
    }
}

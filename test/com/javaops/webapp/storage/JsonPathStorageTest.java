package com.javaops.webapp.storage;

import com.javaops.webapp.storage.serialization.JsonStreamSerialization;

public class JsonPathStorageTest extends AbstractStorageTest {
    JsonPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new JsonStreamSerialization()));
    }
}

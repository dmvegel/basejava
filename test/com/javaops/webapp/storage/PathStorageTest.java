package com.javaops.webapp.storage;

import com.javaops.webapp.storage.serialization.ObjectStreamSerialization;

public class PathStorageTest extends AbstractStorageTest {
    PathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new ObjectStreamSerialization()));
    }
}

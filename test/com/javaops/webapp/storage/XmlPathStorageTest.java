package com.javaops.webapp.storage;

import com.javaops.webapp.storage.serialization.XmlStreamSerialization;

public class XmlPathStorageTest extends AbstractStorageTest {
    XmlPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new XmlStreamSerialization()));
    }
}

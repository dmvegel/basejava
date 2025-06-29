package com.javaops.webapp.storage;

import java.io.File;

public class ObjectStreamStorage extends AbstractFileStorage {
    protected ObjectStreamStorage(File directory, SerializationStrategy serializationStrategy) {
        super(directory, serializationStrategy);
    }
}

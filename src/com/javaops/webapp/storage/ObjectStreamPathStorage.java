package com.javaops.webapp.storage;

import java.nio.file.Path;

public class ObjectStreamPathStorage extends AbstractPathStorage {
    protected ObjectStreamPathStorage(Path directory, SerializationStrategy serializationStrategy) {
        super(directory.toString(), serializationStrategy);
    }
}

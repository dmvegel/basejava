package com.javaops.webapp.storage;

public abstract class AbstractSerializableStorage<T> extends AbstractStorage<T> {
    protected SerializationStrategy serializationStrategy;

    public AbstractSerializableStorage(SerializationStrategy serializationStrategy) {
        this.serializationStrategy = serializationStrategy;
    }
}

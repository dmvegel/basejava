package com.javaops.webapp.storage;

import com.javaops.webapp.model.Resume;

import java.util.List;
import java.util.stream.Stream;

public abstract class AbstractSerializableStorage<T> extends AbstractStorage<T> {
    public static final String IO_ERROR = "I/O error";
    public static final String FILE_IS_NULL_ERROR = "file must not be null";
    public static final String FILE_CREATE_ERROR = "cannot create file";
    public static final String FILE_READ_ERROR = "file read error";
    public static final String NO_DIRECTORY_ERROR = "directory must not be null";
    public static final String FILE_DELETE_ERROR = "cannot delete file";
    public static final String NOT_A_DIR_OR_ACCESS_ERROR = " is not directory or is not writable/readable";

    protected SerializationStrategy serializationStrategy;

    public void setSerializationStrategy(SerializationStrategy serializationStrategy) {
        this.serializationStrategy = serializationStrategy;
    }

    public AbstractSerializableStorage(SerializationStrategy serializationStrategy) {
        this.serializationStrategy = serializationStrategy;
    }

    @Override
    protected List<Resume> doCopyAll() {
        return getStreamResumeFiles().map(this::doGet).toList();
    }

    @Override
    public void clear() {
        getStreamResumeFiles().forEach(this::doDelete);
    }

    @Override
    public int size() {
        return (int) getStreamResumeFiles().count();
    }

    abstract protected Stream<T> getStreamResumeFiles();
}

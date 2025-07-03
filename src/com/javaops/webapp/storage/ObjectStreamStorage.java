package com.javaops.webapp.storage;

import com.javaops.webapp.exception.StorageException;
import com.javaops.webapp.model.Resume;
import com.javaops.webapp.storage.serialization.SerializationStrategy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public class ObjectStreamStorage extends AbstractSerializableStorage<File> {
    private final File directory;

    protected ObjectStreamStorage(File directory, SerializationStrategy serializationStrategy) {
        super(serializationStrategy);
        Objects.requireNonNull(directory, NO_DIRECTORY_ERROR);
        if (!directory.canRead() || !directory.canWrite() || !directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + NOT_A_DIR_OR_ACCESS_ERROR);
        }
        this.directory = directory;
    }

    @Override
    protected Resume doGet(File file) {
        try {
            return serializationStrategy.doRead(new FileInputStream(file));
        } catch (IOException e) {
            throw new StorageException(file.getName(), FILE_READ_ERROR, e);
        }
    }

    @Override
    protected void doSave(Resume resume, File file) {
        try {
            if (!file.createNewFile()) {
                throw new StorageException(file.getName(), FILE_CREATE_ERROR);
            }
        } catch (IOException e) {
            throw new StorageException(file.getName(), IO_ERROR, e);
        }
        doUpdate(resume, file);
    }

    @Override
    protected void doUpdate(Resume resume, File file) {
        try {
            serializationStrategy.doWrite(resume, new FileOutputStream(file));
        } catch (IOException e) {
            throw new StorageException(file.getName(), IO_ERROR, e);
        }
    }

    @Override
    protected void doDelete(File file) {
        Objects.requireNonNull(file, FILE_IS_NULL_ERROR);
        if (!file.delete()) {
            throw new StorageException(file.getName(), FILE_DELETE_ERROR);
        }
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    protected Stream<File> getStreamResumeFiles() {
        return Arrays.stream(Objects.requireNonNull(directory.listFiles(), IO_ERROR));
    }
}

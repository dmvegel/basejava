package com.javaops.webapp.storage;

import com.javaops.webapp.exception.StorageException;
import com.javaops.webapp.model.Resume;
import com.javaops.webapp.storage.serialization.SerializationStrategy;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {
    private final Path directory;

    protected SerializationStrategy serializationStrategy;

    protected PathStorage(String dir, SerializationStrategy serializationStrategy) {
        this.serializationStrategy = serializationStrategy;
        Objects.requireNonNull(dir, NO_DIRECTORY_ERROR);
        directory = Paths.get(dir);
        if (!Files.isDirectory(directory) || !Files.isWritable(directory) || !Files.isReadable(directory)) {
            throw new IllegalArgumentException(directory + NOT_A_DIR_OR_ACCESS_ERROR);
        }
    }

    @Override
    protected Resume doGet(Path path) {
        try {
            return serializationStrategy.doRead(new FileInputStream(path.toString()));
        } catch (IOException e) {
            throw new StorageException(path.toString(), FILE_READ_ERROR, e);
        }
    }

    @Override
    protected void doSave(Resume resume, Path path) {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            throw new StorageException(path.toString(), FILE_CREATE_ERROR, e);
        }
        doUpdate(resume, path);
    }

    @Override
    protected void doUpdate(Resume resume, Path path) {
        try {
            serializationStrategy.doWrite(resume, new FileOutputStream(path.toString()));
        } catch (IOException e) {
            throw new StorageException(path.toString(), IO_ERROR, e);
        }
    }

    @Override
    protected void doDelete(Path path) {
        Objects.requireNonNull(path, FILE_IS_NULL_ERROR);
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException(path.toString(), FILE_DELETE_ERROR);
        }
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return Paths.get(directory.toString(), uuid);
    }

    @Override
    protected boolean isExist(Path path) {
        return Files.exists(path);
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

    private Stream<Path> getStreamResumeFiles() {
        try {
            return Files.list(directory);
        } catch (IOException e) {
            throw new StorageException(null, IO_ERROR);
        }
    }
}

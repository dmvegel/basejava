package com.javaops.webapp.storage;

import com.javaops.webapp.exception.StorageException;
import com.javaops.webapp.model.Resume;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class AbstractPathStorage extends AbstractSerializableStorage<Path> {
    private final Path directory;

    protected AbstractPathStorage(String dir, SerializationStrategy serializationStrategy) {
        super(serializationStrategy);
        Objects.requireNonNull(dir, "directory must not be null");
        directory = Paths.get(dir);
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(directory + " is not directory or is not writable");
        }
    }

    @Override
    protected Resume doGet(Path path) {
        Objects.requireNonNull(path, "directory must not be null");
        if (!Files.isReadable(directory)) {
            throw new IllegalArgumentException(directory + " is not readable");
        }
        try {
            return serializationStrategy.doRead(new FileInputStream(path.toString()));
        } catch (IOException e) {
            throw new StorageException("directory delete error ", path.toString(), e);
        }
    }

    @Override
    protected void doSave(Resume resume, Path path) {
        try {
            Files.createFile(path);
            serializationStrategy.doWrite(resume, new FileOutputStream(path.toString()));
        } catch (IOException e) {
            throw new StorageException(path.toString(), "IO error", e);
        }
    }

    @Override
    protected void doUpdate(Resume resume, Path path) {
        try {
            serializationStrategy.doWrite(resume, new FileOutputStream(path.toString()));
        } catch (IOException e) {
            throw new StorageException(path.toString(), "IO error", e);
        }
    }

    @Override
    protected void doDelete(Path path) {
        Objects.requireNonNull(path, "path must not be null");
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException(path.toString(), "cannot delete path ");
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
        List<Resume> result = new ArrayList<>();
        List<Path> paths;
        try (Stream<Path> pathStream = Files.list(directory)) {
            paths = pathStream.toList();
        } catch (IOException e) {
            throw new StorageException("Path delete error", null);
        }
        for (Path path : paths) {
            if (!Files.isDirectory(path)) {
                result.add(doGet(path));
            }
        }
        return result;
    }

    @Override
    public void clear() {
        try (Stream<Path> pathStream = Files.list(directory)) {
            pathStream.forEach(this::doDelete);
        } catch (IOException e) {
            throw new StorageException("Path delete error", null);
        }
    }

    @Override
    public int size() {
        try (Stream<Path> pathStream = Files.list(directory)) {
            return (int) pathStream.count();
        } catch (IOException e) {
            throw new StorageException("Path count error", null);
        }
    }
}

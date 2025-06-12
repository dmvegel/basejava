package com.javaops.webapp.storage;

import com.javaops.webapp.exception.NoFilesStorageException;
import com.javaops.webapp.exception.StorageException;
import com.javaops.webapp.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {
    private final File directory;

    protected AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
        this.directory = directory;
    }

    @Override
    protected Resume doGet(File file) {
        Objects.requireNonNull(file, "file must not be null");
        if (!directory.canRead()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
        return doRead(file);
    }

    @Override
    protected void doSave(Resume resume, File file) {
        try {
            if (!file.createNewFile()) {
                throw new StorageException(file.getName(), "cannot create file");
            }
            doWrite(resume, file);
        } catch (IOException e) {
            throw new StorageException(file.getName(), "IO error", e);
        }
    }

    @Override
    protected void doUpdate(Resume resume, File file) {
        try {
            doWrite(resume, file);
        } catch (IOException e) {
            throw new StorageException(file.getName(), "IO error", e);
        }
    }

    @Override
    protected void doDelete(File file) {
        Objects.requireNonNull(file, "file must not be null");
        if (!file.delete()) {
            throw new StorageException(file.getName(), "cannot delete file ");
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
    protected List<Resume> doCopyAll() {
        List<Resume> result = new ArrayList<>();
        File[] files = directory.listFiles();
        if (files == null) throw new NoFilesStorageException(directory.getName());
        for (File file : files) {
            if (!file.isDirectory()) {
                result.add(doRead(file));
            }
        }
        return result;
    }

    @Override
    public void clear() {
        File[] files = directory.listFiles();
        if (files == null) throw new NoFilesStorageException(directory.getName());
        for (File file : files) {
            doDelete(file);
        }
    }

    @Override
    public int size() {
        File[] files = directory.listFiles();
        if (files == null) throw new NoFilesStorageException(directory.getName());
        return files.length;
    }

    protected abstract Resume doRead(File file);

    protected abstract void doWrite(Resume r, File file) throws IOException;
}

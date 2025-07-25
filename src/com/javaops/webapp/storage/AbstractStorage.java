package com.javaops.webapp.storage;

import com.javaops.webapp.exception.ExistStorageException;
import com.javaops.webapp.exception.NotExistStorageException;
import com.javaops.webapp.model.Resume;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractStorage<T> implements Storage {
    public static final String IO_ERROR = "I/O error";
    public static final String FILE_IS_NULL_ERROR = "file must not be null";
    public static final String FILE_CREATE_ERROR = "cannot create file";
    public static final String FILE_READ_ERROR = "file read error";
    public static final String NO_DIRECTORY_ERROR = "directory must not be null";
    public static final String FILE_DELETE_ERROR = "cannot delete file";
    public static final String NOT_A_DIR_OR_ACCESS_ERROR = " is not directory or is not writable/readable";

    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());

    protected static final Comparator<Resume> SORT_COMPARATOR =
            Comparator.comparing(Resume::getFullName)
                    .thenComparing(Resume::getUuid);

    @Override
    public Resume get(String uuid) {
        LOG.info("Get " + uuid);
        T searchKey = getExistingSearchKey(uuid);
        return doGet(searchKey);
    }

    @Override
    public void save(Resume r) {
        LOG.info("Save " + r);
        T searchKey = getNotExistingSearchKey(r.getUuid());
        doSave(r, searchKey);
    }

    @Override
    public void update(Resume r) {
        LOG.info("Update " + r);
        T searchKey = getExistingSearchKey(r.getUuid());
        doUpdate(r, searchKey);
    }

    @Override
    public void delete(String uuid) {
        LOG.info("Delete " + uuid);
        T searchKey = getExistingSearchKey(uuid);
        doDelete(searchKey);
    }

    @Override
    public List<Resume> getAllSorted() {
        LOG.info("getAllSorted");
        return doCopyAll().stream().sorted(SORT_COMPARATOR).toList();
    }

    protected T getExistingSearchKey(String uuid) {
        T searchKey = getSearchKey(uuid);
        if (isExist(searchKey))
            return searchKey;
        LOG.warning("Resume " + uuid + " not exist");
        throw new NotExistStorageException(uuid);
    }

    protected T getNotExistingSearchKey(String uuid) {
        T searchKey = getSearchKey(uuid);
        if (!isExist(searchKey))
            return searchKey;
        LOG.warning("Resume " + uuid + " already exist");
        throw new ExistStorageException(uuid);
    }

    protected abstract Resume doGet(T searchKey);

    protected abstract void doSave(Resume resume, T searchKey);

    protected abstract void doUpdate(Resume resume, T searchKey);

    protected abstract void doDelete(T searchKey);

    protected abstract T getSearchKey(String uuid);

    protected abstract boolean isExist(T searchKey);

    protected abstract List<Resume> doCopyAll();
}

package com.javaops.webapp.storage;

import com.javaops.webapp.exception.ExistStorageException;
import com.javaops.webapp.exception.NotExistStorageException;
import com.javaops.webapp.model.Resume;
import com.javaops.webapp.util.SqlHelper;
import org.postgresql.util.PSQLException;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        this.sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.execute("delete from resume", PreparedStatement::executeUpdate);
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.execute("select * from resume where uuid = ?", ps -> {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Resume(uuid, rs.getString("full_name").trim());
                }
                throw new NotExistStorageException(uuid);
            }
        }, uuid);
    }

    @Override
    public void save(Resume r) {
        sqlHelper.execute("insert into resume (uuid, full_name) values (?, ?)", ps -> {
            try {
                ps.execute();
                return null;
            } catch (PSQLException e) {
                throw new ExistStorageException(r.getUuid());
            }
        }, r.getUuid(), r.getFullName());
    }

    @Override
    public void update(Resume r) {
        sqlHelper.executeUpdate("update resume set full_name = ? where uuid = ?", r.getUuid(), r.getFullName(), r.getUuid());
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.executeUpdate("delete from resume where uuid = ?", uuid, uuid);
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.execute("select * from resume order by full_name", ps -> {
            List<Resume> resumes = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resumes.add(new Resume(rs.getString("uuid").trim(), rs.getString("full_name").trim()));
                }
            }
            return resumes;
        });
    }

    @Override
    public int size() {
        return sqlHelper.execute("select count(*) from resume", ps -> {
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        });
    }
}

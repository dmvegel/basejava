package com.javaops.webapp.storage;

import com.javaops.webapp.model.Resume;
import com.javaops.webapp.util.SqlHelper;

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
        return sqlHelper.selectExist("select * from resume where uuid = ?", ps -> {
            Resume result = null;
            ps.setObject(1, uuid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = new Resume(uuid, rs.getString("full_name"));
                }
            }
            return result;
        }, uuid);
    }

    @Override
    public void save(Resume r) {
        sqlHelper.insertUnique("insert into resume (uuid, full_name) values (?, ?)", ps -> {
            ps.setObject(1, r.getUuid());
            ps.setObject(2, r.getFullName());
            return ps.execute();
        }, r.getUuid());
    }

    @Override
    public void update(Resume r) {
        sqlHelper.executeUpdate("update resume set full_name = ? where uuid = ?", ps -> {
            ps.setObject(1, r.getFullName());
            ps.setObject(2, r.getUuid());
            return ps.executeUpdate();
        }, r.getUuid());
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.executeUpdate("delete from resume where uuid = ?", ps -> {
            ps.setObject(1, uuid);
            return ps.executeUpdate();
        }, uuid);
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.execute("select * from resume order by full_name", ps -> {
            List<Resume> resumes = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resumes.add(new Resume(rs.getString("uuid"), rs.getString("full_name")));
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

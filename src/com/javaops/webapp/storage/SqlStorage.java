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
        sqlHelper.executeQuery("delete from resume", PreparedStatement::executeUpdate);
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.executeQuery("select * from resume where uuid = ?", ps -> {
            Resume resume = null;
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                resume = new Resume(uuid, rs.getString("full_name"));
            }
            return sqlHelper.checkExistAndReturn(resume, uuid);
        });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.executeQuery("insert into resume (uuid, full_name) values (?, ?)", ps -> {
            ps.setString(1, r.getUuid());
            ps.setString(2, r.getFullName());
            return ps.executeUpdate();
        });
    }

    @Override
    public void update(Resume r) {
        sqlHelper.executeQuery("update resume set full_name = ? where uuid = ?", ps -> {
            ps.setString(1, r.getFullName());
            ps.setString(2, r.getUuid());
            return sqlHelper.checkExistAndReturn(ps.executeUpdate(), r.getUuid());
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.executeQuery("delete from resume where uuid = ?", ps -> {
            ps.setString(1, uuid);
            return sqlHelper.checkExistAndReturn(ps.executeUpdate(), uuid);
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.executeQuery("select * from resume order by full_name", ps -> {
            List<Resume> resumes = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resumes.add(new Resume(rs.getString("uuid"), rs.getString("full_name")));
            }
            return resumes;
        });
    }

    @Override
    public int size() {
        return sqlHelper.executeQuery("select count(*) from resume", ps -> {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        });
    }
}
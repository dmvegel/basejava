package com.javaops.webapp.storage;

import com.javaops.webapp.model.ContactType;
import com.javaops.webapp.model.Resume;
import com.javaops.webapp.util.SqlHelper;

import java.sql.*;
import java.util.*;

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
        return sqlHelper.transactionalExecute(conn -> {
                    Resume resume = null;
                    try (PreparedStatement ps = conn.prepareStatement("select * from resume where uuid = ?")) {
                        ps.setString(1, uuid);
                        ResultSet rs = ps.executeQuery();
                        if (rs.next()) {
                            resume = resultSetToResume(rs);
                        }
                        sqlHelper.checkExist(resume, uuid);
                    }
                    try (PreparedStatement ps = conn.prepareStatement("select * from contact where resume_uuid = ?")) {
                        ps.setString(1, uuid);
                        ResultSet rs = ps.executeQuery();
                        resultSetToContacts(rs, Map.of(uuid, resume));
                    }
                    return resume;
                }
        );
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
                    try (PreparedStatement ps = conn.prepareStatement("insert into resume (uuid, full_name) values (?,?)")) {
                        ps.setString(1, r.getUuid());
                        ps.setString(2, r.getFullName());
                        ps.executeUpdate();
                    }
                    insertContacts(conn, r);
                    return null;
                }
        );
    }

    @Override
    public void update(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("update resume set full_name = ? where uuid = ?")) {
                ps.setString(1, r.getFullName());
                ps.setString(2, r.getUuid());
                sqlHelper.checkExist(ps.executeUpdate(), r.getUuid());
            }
            deleteContacts(conn, r.getUuid());
            insertContacts(conn, r);
            return null;
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.executeQuery("delete from resume where uuid = ?", ps -> {
            ps.setString(1, uuid);
            sqlHelper.checkExist(ps.executeUpdate(), uuid);
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.transactionalExecute(conn -> {
                    Map<String, Resume> resumes = new LinkedHashMap<>();
                    try (PreparedStatement ps = conn.prepareStatement("select * from resume order by full_name, uuid")) {
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            resultSetToResumeMap(rs, resumes);
                        }
                    }
                    try (PreparedStatement ps = conn.prepareStatement("select * from contact")) {
                        ResultSet rs = ps.executeQuery();
                        resultSetToContacts(rs, resumes);
                    }
                    return new ArrayList<>(resumes.values());
                }
        );
    }

    @Override
    public int size() {
        return sqlHelper.executeQuery("select count(*) from resume", ps -> {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        });
    }

    private void insertContacts(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "insert into contact (resume_uuid, type, value) values (?,?,?)")) {
            for (Map.Entry<ContactType, String> e : resume.getContacts().entrySet()) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void deleteContacts(Connection conn, String uuid) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "delete from contact where resume_uuid = ?")) {
            ps.setString(1, uuid);
            ps.executeUpdate();
        }
    }

    private Resume resultSetToResume(ResultSet rs) throws SQLException {
        return new Resume(rs.getString("uuid"), rs.getString("full_name"));
    }

    private void resultSetToResumeMap(ResultSet rs, Map<String, Resume> resumes) throws SQLException {
        resumes.put(rs.getString("uuid"), resultSetToResume(rs));
    }

    private void resultSetToContacts(ResultSet rs, Map<String, Resume> resumes) throws SQLException {
        while (rs.next()) {
            resumes.get(rs.getString("resume_uuid")).getContacts().put(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
        }
    }
}
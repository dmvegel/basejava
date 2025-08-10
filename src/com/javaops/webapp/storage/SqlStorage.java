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
        return sqlHelper.executeQuery(
                "select * from resume " +
                        "left join contact " +
                        "on resume.uuid = contact.resume_uuid " +
                        "where resume.uuid = ?", ps -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    Resume resume = rs.next() ? mapResume(rs) : null;
                    return sqlHelper.checkExistAndReturn(resume, uuid);
                });
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
                }
        );
    }

    @Override
    public void update(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("update resume set full_name = ? where uuid = ?")) {
                ps.setString(1, r.getFullName());
                ps.setString(2, r.getUuid());
                sqlHelper.checkExistAndReturn(ps.executeUpdate(), r.getUuid());
            }
            deleteContacts(conn, r.getUuid());
            insertContacts(conn, r);
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
        return sqlHelper.executeQuery(
                "select * from resume " +
                        "left join contact on resume.uuid = contact.resume_uuid " +
                        "order by full_name, uuid", ps -> {
                    ResultSet rs = ps.executeQuery();
                    List<Resume> resumes = new ArrayList<>();
                    if (!rs.next()) {
                        return resumes;
                    }
                    do {
                        resumes.add(mapResume(rs));
                    } while (!rs.isAfterLast());
                    return resumes;
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

    private Resume mapResume(ResultSet rs) throws SQLException {
        Resume resume = resultSetToResume(rs);
        Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
        do {
            String type = rs.getString("type");
            if (type != null) {
                addContact(rs, contacts);
            }
        } while (rs.next() && rs.getString("uuid").equals(resume.getUuid()));
        resume.setContacts(contacts);
        return resume;
    }

    private Resume resultSetToResume(ResultSet rs) throws SQLException {
        return new Resume(rs.getString("uuid"), rs.getString("full_name"));
    }

    private void addContact(ResultSet rs, Map<ContactType, String> contacts) throws SQLException {
        contacts.put(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
    }
}
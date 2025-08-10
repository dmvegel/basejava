package com.javaops.webapp.storage;

import com.javaops.webapp.model.ContactType;
import com.javaops.webapp.model.Resume;
import com.javaops.webapp.util.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        this.sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("delete from resume")) {
                ps.execute();
            }
            try (PreparedStatement ps = conn.prepareStatement("delete from contact")) {
                ps.execute();
            }
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.executeQuery(
                "SELECT * FROM resume r " +
                        "LEFT JOIN contact c " +
                        "ON r.uuid = c.resume_uuid " +
                        "WHERE r.uuid = ?", ps -> {
                    Resume resume = null;
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        resume = new Resume(uuid, rs.getString("full_name"));
                        Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
                        do {
                            String value = rs.getString("value");
                            ContactType type = ContactType.valueOf(rs.getString("type"));
                            contacts.put(type, value);
                        } while (rs.next());
                        resume.setContacts(contacts);
                    }
                    return sqlHelper.checkExistAndReturn(resume, uuid);
                });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
                    try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                        ps.setString(1, r.getUuid());
                        ps.setString(2, r.getFullName());
                        ps.executeUpdate();
                    }
                    try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
                        for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                            ps.setString(1, r.getUuid());
                            ps.setString(2, e.getKey().name());
                            ps.setString(3, e.getValue());
                            ps.addBatch();
                        }
                        return ps.executeBatch();
                    }
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

            try (PreparedStatement ps = conn.prepareStatement("delete from contact where contact.resume_uuid = ?")) {
                ps.setString(1, r.getUuid());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement("insert into contact (resume_uuid, type, value) values (?,?,?)")) {
                for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                    ps.setString(1, r.getUuid());
                    ps.setString(2, e.getKey().name());
                    ps.setString(3, e.getValue());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            return null;
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
        return sqlHelper.transactionalExecute(conn -> {
            Map<String, Resume> resumes = new LinkedHashMap<>();
            try (PreparedStatement ps = conn.prepareStatement("select * from resume order by full_name")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    resumes.put(rs.getString("uuid"), new Resume(rs.getString("uuid"), rs.getString("full_name")));
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("select * from contact")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    resumes.get(rs.getString("resume_uuid")).getContacts().put(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
                }
            }
            return new ArrayList<Resume>(resumes.values());
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
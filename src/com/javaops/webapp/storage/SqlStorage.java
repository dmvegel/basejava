package com.javaops.webapp.storage;

import com.javaops.webapp.model.*;
import com.javaops.webapp.util.JsonParser;
import com.javaops.webapp.util.SqlHelper;

import java.sql.*;
import java.util.*;

public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
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
                    Map<String, Resume> singleResume = Map.of(uuid, resume);
                    try (PreparedStatement ps = conn.prepareStatement("select * from contact where resume_uuid = ?")) {
                        ps.setString(1, uuid);
                        ResultSet rs = ps.executeQuery();
                        resultSetToContacts(rs, singleResume);
                    }
                    try (PreparedStatement ps = conn.prepareStatement("select * from section where resume_uuid = ?")) {
                        ps.setString(1, uuid);
                        ResultSet rs = ps.executeQuery();
                        resultSetToSections(rs, singleResume);
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
                    insertSections(conn, r);
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
            replaceContacts(conn, r);
            replaceSections(conn, r);
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
                    try (PreparedStatement ps = conn.prepareStatement("select * from section")) {
                        ResultSet rs = ps.executeQuery();
                        resultSetToSections(rs, resumes);
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

    private void insertSections(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "insert into section (resume_uuid, type, value) values (?,?,?)")) {
            for (Map.Entry<SectionType, Section> e : resume.getSections().entrySet()) {
                SectionType sectionType = e.getKey();
                ps.setString(1, resume.getUuid());
                ps.setString(2, sectionType.name());
                ps.setString(3, JsonParser.write(e.getValue(), Section.class));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void replaceContacts(Connection conn, Resume resume) throws SQLException {
        deleteByUuid(conn, "delete from contact where resume_uuid = ?", resume.getUuid());
        insertContacts(conn, resume);
    }


    private void replaceSections(Connection conn, Resume resume) throws SQLException {
        deleteByUuid(conn, "delete from section where resume_uuid = ?", resume.getUuid());
        insertSections(conn, resume);
    }

    private void deleteByUuid(Connection conn, String sql, String uuid) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
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

    private void resultSetToSections(ResultSet rs, Map<String, Resume> resumes) throws SQLException {
        while (rs.next()) {
            resumes.get(rs.getString("resume_uuid")).getSections().put(SectionType.valueOf(rs.getString("type")),
                    JsonParser.read(rs.getString("value"), Section.class));
        }
    }
}
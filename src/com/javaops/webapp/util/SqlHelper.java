package com.javaops.webapp.util;

import com.javaops.webapp.exception.NotExistStorageException;
import com.javaops.webapp.exception.StorageException;
import com.javaops.webapp.sql.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    private final ConnectionFactory connectionFactory;

    public SqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @FunctionalInterface
    public interface Executor<T> {
        T execute(PreparedStatement ps) throws SQLException;
    }

    public <T> T execute(String sql, Executor<T> executor, Object... args) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            return executor.execute(ps);
        } catch (SQLException e) {
            throw new StorageException(e.getMessage(), e);
        }
    }

    public void executeUpdate(String sql, String uuid, Object... args) {
        int result = execute(sql, PreparedStatement::executeUpdate, args);
        if (result == 0) {
            throw new NotExistStorageException(uuid);
        }
    }
}

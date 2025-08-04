package com.javaops.webapp.util;

import com.javaops.webapp.exception.ExistStorageException;
import com.javaops.webapp.exception.NotExistStorageException;
import com.javaops.webapp.exception.StorageException;
import com.javaops.webapp.sql.ConnectionFactory;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;

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

    private <T> T executeQuery(String sql, Executor<T> executor, String uuid) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            return executor.execute(ps);
        } catch (PSQLException e) {
            if (e.getSQLState().equals(PSQLState.UNIQUE_VIOLATION.getState())) {
                throw new ExistStorageException(uuid);
            }
            throw new StorageException(e.getMessage(), e);
        } catch (SQLException e) {
            throw new StorageException(e.getMessage(), e);
        }
    }

    public void executeUpdate(String sql, Executor<Integer> executor, String uuid) {
        int result = executeQuery(sql, executor, uuid);
        if (result == 0) {
            throw new NotExistStorageException(uuid);
        }
    }

    public <T> void insertUnique(String sql, Executor<T> executor, String uuid) {
        executeQuery(sql, executor, uuid);
    }

    public <T> T selectExist(String sql, Executor<T> executor, String uuid) {
        T result = executeQuery(sql, executor, uuid);
        if (result == null) {
            throw new NotExistStorageException(uuid);
        }
        return result;
    }

    public <T> T execute(String sql, Executor<T> executor) {
        return executeQuery(sql, executor, null);
    }
}

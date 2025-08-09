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

    public <T> T executeQuery(String sql, Executor<T> executor, String uuid) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            return executor.execute(ps);
        } catch (PSQLException e) {
            if (e.getSQLState().equals(PSQLState.UNIQUE_VIOLATION.getState())) {
                throw new ExistStorageException(e.getMessage());
            }
            throw new StorageException(e.getMessage(), e);
        } catch (SQLException e) {
            throw new StorageException(e.getMessage(), e);
        }
    }

    public <T> T checkExistAndReturn(T result, String uuid) {
        if (result == null ||
                result instanceof Integer && (Integer) result == 0) {
            throw new NotExistStorageException(uuid);
        }
        return result;
    }
}

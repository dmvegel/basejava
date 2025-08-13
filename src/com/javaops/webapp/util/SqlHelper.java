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

    @FunctionalInterface
    public interface SqlTransaction<T> {
        T execute(Connection conn) throws SQLException;
    }

    public <T> T executeQuery(String sql, Executor<T> executor) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            return executor.execute(ps);
        } catch (SQLException e) {
            throw handleException(e);
        }
    }

    public <T> void checkExist(T result, String uuid) {
        if (result == null ||
                result instanceof Integer && (Integer) result == 0) {
            throw new NotExistStorageException(uuid);
        }
    }

    public <T> T transactionalExecute(SqlTransaction<T> executor) {
        try (Connection conn = connectionFactory.getConnection()) {
            try {
                conn.setAutoCommit(false);
                T res = executor.execute(conn);
                conn.commit();
                return res;
            } catch (SQLException e) {
                conn.rollback();
                throw handleException(e);
            }
        } catch (SQLException e) {
            throw handleException(e);
        }
    }

    private RuntimeException handleException(SQLException e) {
        if (e instanceof PSQLException &&
                e.getSQLState().equals(PSQLState.UNIQUE_VIOLATION.getState())) {
            return new ExistStorageException(e.getMessage());
        }
        return new StorageException(e.getMessage(), e);
    }
}
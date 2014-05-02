package com.jd.framework.orm.transaction;

import java.sql.*;

import org.apache.commons.lang3.RandomStringUtils;

import com.jd.framework.orm.exception.DbError;
import com.jd.framework.orm.exception.SystemException;

/**
 * Jdbc 子事务
 */
public class JdbcNestedTransaction implements Transaction {
    private Connection conn;
    private Savepoint savepoint;

    public JdbcNestedTransaction(Connection conn) {
        this.conn = conn;

        try {
            savepoint = conn.setSavepoint(RandomStringUtils.randomAlphabetic(4));
        } catch (SQLException e) {
            throw SystemException.unchecked(e, DbError.TRANSACTION_ERROR);
        }
    }

    public Connection getConnection() {
        return conn;
    }

    /**
     * 提交一个事务
     */
    public void commit() {
        try {
            if (conn.isClosed()) {
                throw new SystemException("the connection is closed in transaction.", DbError.TRANSACTION_ERROR);
            }
            // 子事务不需要 commit
            // conn.commit(savepoint);
        } catch (SQLException e) {
            throw SystemException.unchecked(e, DbError.TRANSACTION_ERROR);
        }
    }

    /**
     * 回滚一个事务
     */
    public void rollback() {
        try {
            if (conn.isClosed()) {
                throw new SystemException("the connection is closed in transaction.", DbError.TRANSACTION_ERROR);
            }
            conn.rollback(savepoint);
        	} catch (SQLException e) {
            throw SystemException.unchecked(e, DbError.TRANSACTION_ERROR);
        }
    }

    /**
     * 结束一个事务
     */
    public void close() {
        // 子事务不需要 close
    }

}

package com.jd.framework.orm.util;

import java.sql.*;
import javax.sql.DataSource;

import com.jd.framework.orm.dialect.Dialect;
import com.jd.framework.orm.exception.SystemException;

public final class JdbcUtils {
    public static void closeQuietly(Connection conn) {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
        }
    }

    public static void closeQuietly(Statement stmt) {
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
        }
    }

    public static void closeQuietly(ResultSet rs) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
        }
    }

    public static boolean doGetTableExist(Connection conn, String tabelName) {
        ResultSet rs = null;
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            rs = metaData.getTables(null, null, tabelName.toUpperCase(), new String[] { "TABLE" });
            return rs.next();
        } catch (SQLException e) {
            throw SystemException.unchecked(e);
        } finally {
            JdbcUtils.closeQuietly(rs);
        }
    }

    public static Dialect doGetDialect(DataSource dataSource) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            return JdbcUtils.doGetDialect(conn);
        } catch (SQLException e) {
            throw SystemException.unchecked(e);
        } finally {
            JdbcUtils.closeQuietly(conn);
        }
    }

    public static Dialect doGetDialect(Connection conn) {
        try {
            String name = conn.getMetaData().getDatabaseProductName();
            return Dialect.getDialect(name);
        } catch (SQLException e) {
            throw SystemException.unchecked(e);
        } finally {
            JdbcUtils.closeQuietly(conn);
        }
    }
}

package com.jd.framework.orm.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.commons.lang3.ClassUtils;

public class PreparedStatementSetter {

    public static void setValue(PreparedStatement ps, int index, Object value) throws SQLException {
        if (value == null) {
            ps.setObject(index, null);
            return;
        }
        Class<?> requiredType = value.getClass();
        requiredType = ClassUtils.primitiveToWrapper(requiredType);

        if (String.class.equals(requiredType)) {
            ps.setString(index, (String) value);
        } else if (Integer.class.equals(requiredType)) {
            ps.setInt(index, ((Number) value).intValue());
        } else if (Float.class.equals(requiredType)) {
            ps.setFloat(index, ((Number) value).floatValue());
        } else if (Double.class.equals(requiredType)) {
            ps.setDouble(index, ((Number) value).doubleValue());
        } else if (Boolean.class.equals(requiredType)) {
            ps.setBoolean(index, ((Boolean) value).booleanValue());
        } else if (java.sql.Date.class.equals(requiredType)) {
            ps.setDate(index, (java.sql.Date) value);
        } else if (java.sql.Time.class.equals(requiredType)) {
            ps.setTime(index, (java.sql.Time) value);
        } else if (java.sql.Timestamp.class.equals(requiredType)) {
            ps.setTimestamp(index, (java.sql.Timestamp) value);
        } else if (java.util.Date.class.equals(requiredType)) {
            ps.setTimestamp(index, new java.sql.Timestamp(((java.util.Date) value).getTime()));
        } else if (Byte.class.equals(requiredType)) {
            ps.setByte(index, ((Number) value).byteValue());
        } else if (Short.class.equals(requiredType)) {
            ps.setShort(index, ((Number) value).shortValue());
        } else if (Long.class.equals(requiredType)) {
            ps.setLong(index, ((Number) value).longValue());
        } else if (java.math.BigDecimal.class.equals(requiredType)) {
            ps.setBigDecimal(index, (java.math.BigDecimal) value);
        } else if (Number.class.equals(requiredType)) {
            ps.setDouble(index, ((Number) value).doubleValue());
        } else if (byte[].class.equals(requiredType)) {
            ps.setBytes(index, (byte[]) value);
        } else if (java.sql.Blob.class.equals(requiredType)) {
            ps.setBlob(index, (java.sql.Blob) value);
        } else if (java.sql.Clob.class.equals(requiredType)) {
            ps.setClob(index, (java.sql.Clob) value);
        } else if (java.net.URL.class.equals(requiredType)) {
            ps.setURL(index, (java.net.URL) value);
        } else {
            // Some unknown type desired -> rely on getObject.
            ps.setObject(index, value);
        }
    }
}

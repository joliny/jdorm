package com.jd.framework.orm.common.conv;



import java.io.File;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.Date;
import org.apache.commons.lang3.ArrayUtils;

import com.jd.framework.orm.common.ClassConvertUtils;

/**
 * 将单个Object对象转换成不同的数据类型返回
 */
public abstract class DataConverter {
    public static final Date[] EMPTY_DATE_ARRAY = new Date[0];
    public static final java.sql.Date[] EMPTY_SQL_DATE_ARRAY = new java.sql.Date[0];
    public static final java.sql.Time[] EMPTY_SQL_TIME_ARRAY = new java.sql.Time[0];
    public static final java.sql.Timestamp[] EMPTY_SQL_TIMESTAMP_ARRAY = new java.sql.Timestamp[0];
    public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class<?>[0];
    public static final File[] EMPTY_FILE_ARRAY = new File[0];
    public static final URL[] EMPTY_URL_ARRAY = new URL[0];

    protected abstract Object value();

    protected abstract String[] values();

    public abstract boolean exist();

    public String asString() {
        return asString(null);
    }

    public String asString(String defaultValue) {
        Object value = value();
        return (value == null) ? defaultValue : ClassConvertUtils.convert(value, String.class);
    }

    public int asIntValue() {
        return asInt(0);
    }

    public Integer asInt() {
        return asInt(null);
    }
    public Integer asInt(Integer defaultValue) {
        Object value = value();
        return (value == null) ? defaultValue : ClassConvertUtils.convert(value, Integer.class);
    }

    public long asLongValue() {
        return asLong(0L);
    }

    public Long asLong() {
        return asLong(null);
    }

    public Long asLong(Long defaultValue) {
        Object value = value();
        return (value == null) ? defaultValue : ClassConvertUtils.convert(value, Long.class);
    }

    public double asDoubleValue() {
        return asDouble(0D);
    }

    public Double asDouble() {
        return asDouble(null);
    }

    public Double asDouble(Double defaultValue) {
        Object value = value();
        return (value == null) ? defaultValue : ClassConvertUtils.convert(value, Double.class);
    }

    public boolean asBooleanValue() {
        return asBoolean(false).booleanValue();
    }

    public Boolean asBoolean() {
        return asBoolean(null);
    }

    public Boolean asBoolean(Boolean defaultValue) {
        Object value = value();
        return (value == null) ? defaultValue : ClassConvertUtils.convert(value, Boolean.class);
    }

    public Date asDate() {
        return asDate(null);
    }

    public Date asDate(Date defaultValue) {
        Object value = value();
        return (value == null) ? defaultValue : ClassConvertUtils.convert(value, Date.class);
    }

    public java.sql.Date asSqlDate() {
        return asSqlDate(null);
    }

    public java.sql.Date asSqlDate(java.sql.Date defaultValue) {
        Object value = value();
        return (value == null) ? defaultValue : ClassConvertUtils.convert(value, java.sql.Date.class);
    }

    public java.sql.Time asSqlTime() {
        return asSqlTime(null);
    }

    public java.sql.Time asSqlTime(java.sql.Time defaultValue) {
        Object value = value();
        return (value == null) ? defaultValue : ClassConvertUtils.convert(value, java.sql.Time.class);
    }

    public java.sql.Timestamp asTimestamp() {
        return asTimestamp(null);
    }

    public java.sql.Timestamp asTimestamp(java.sql.Timestamp defaultValue) {
        Object value = value();
        return (value == null) ? defaultValue : ClassConvertUtils.convert(value, java.sql.Timestamp.class);
    }

    public Class<?> asClass() {
        Object value = value();
        return (value == null) ? null : ClassConvertUtils.convert(value, Class.class);
    }

    public File asFile() {
        Object value = value();
        return (value == null) ? null : ClassConvertUtils.convert(value, File.class);
    }

    public URL asURL() {
        Object value = value();
        return (value == null) ? null : ClassConvertUtils.convert(value, URL.class);
    }

    public <T> T asObject(Class<T> targetClass) {
        Object value = value();
        return (value == null) ? null : ClassConvertUtils.convert(value, targetClass);
    }

    public String[] asStrings() {
        String[] values = values();
        return (values == null) ? ArrayUtils.EMPTY_STRING_ARRAY : values;
    }

    public Integer[] asInts() {
        String[] values = values();
        return (values == null) ? ArrayUtils.EMPTY_INTEGER_OBJECT_ARRAY : ClassConvertUtils.convertArrays(values, Integer.class);
    }

    public Long[] asLongs() {
        String[] values = values();
        return (values == null) ? ArrayUtils.EMPTY_LONG_OBJECT_ARRAY : ClassConvertUtils.convertArrays(values, Long.class);
    }

    public Double[] asDoubles() {
        String[] values = values();
        return (values == null) ? ArrayUtils.EMPTY_DOUBLE_OBJECT_ARRAY : ClassConvertUtils.convertArrays(values, Double.class);
    }

    public Boolean[] asBooleans() {
        String[] values = values();
        return (values == null) ? ArrayUtils.EMPTY_BOOLEAN_OBJECT_ARRAY : ClassConvertUtils.convertArrays(values, Boolean.class);
    }

    public Date[] asDates() {
        String[] values = values();
        return (values == null) ? EMPTY_DATE_ARRAY : ClassConvertUtils.convertArrays(values, Date.class);
    }

    public java.sql.Date[] asSqlDates() {
        String[] values = values();
        return (values == null) ? EMPTY_SQL_DATE_ARRAY : ClassConvertUtils.convertArrays(values, java.sql.Date.class);
    }

    public java.sql.Time[] asSqlTimes() {
        String[] values = values();
        return (values == null) ? EMPTY_SQL_TIME_ARRAY : ClassConvertUtils.convertArrays(values, java.sql.Time.class);
    }

    public java.sql.Timestamp[] asTimestamps() {
        String[] values = values();
        return (values == null) ? EMPTY_SQL_TIMESTAMP_ARRAY : ClassConvertUtils.convertArrays(values, java.sql.Timestamp.class);
    }

    public Class<?>[] asClasses() {
        String[] values = values();
        return (values == null) ? EMPTY_CLASS_ARRAY : ClassConvertUtils.convertArrays(values, Class.class);
    }

    public File[] asFiles() {
        String[] values = values();
        return (values == null) ? EMPTY_FILE_ARRAY : ClassConvertUtils.convertArrays(values, File.class);
    }

    public URL[] asURLs() {
        String[] values = values();
        return (values == null) ? EMPTY_URL_ARRAY : ClassConvertUtils.convertArrays(values, URL.class);
    }

    @SuppressWarnings("unchecked")
    public <T> T[] asObjects(Class<T> componentType) {
        String[] values = values();
        return (T[]) ((values == null) ? Array.newInstance(componentType, 0) : ClassConvertUtils.convertArrays(values, componentType));
    }
}

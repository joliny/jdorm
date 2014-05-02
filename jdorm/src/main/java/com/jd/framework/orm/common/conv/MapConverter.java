package com.jd.framework.orm.common.conv;


import java.io.File;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.Date;
import org.apache.commons.lang3.ArrayUtils;
import com.jd.framework.orm.common.ClassConvertUtils;

/**
 * 将Map对象转换成不同的数据类型返回
 */
public abstract class MapConverter {

    protected abstract Object value(String key);

    protected abstract String[] values(String key);

    public abstract boolean exist(String key);

    public String asString(String key) {
        return asString(key, null);
    }

    public String asString(String key, String defaultValue) {
        Object value = value(key);
        return (value == null) ? defaultValue : ClassConvertUtils.convert(value, String.class);
    }

    public int asIntValue(String key) {
        return asInt(key, 0);
    }

    public Integer asInt(String key) {
        return asInt(key, null);
    }

    public Integer asInt(String key, Integer defaultValue) {
        Object value = value(key);
        return (value == null) ? defaultValue : ClassConvertUtils.convert(value, Integer.class);
    }

    public long asLongValue(String key) {
        return asLong(key, 0L);
    }

    public Long asLong(String key) {
        return asLong(key, null);
    }

    public Long asLong(String key, Long defaultValue) {
        Object value = value(key);
        return (value == null) ? defaultValue : ClassConvertUtils.convert(value, Long.class);
    }

    public double asDoubleValue(String key) {
        return asDouble(key, 0D);
    }

    public Double asDouble(String key) {
        return asDouble(key, null);
    }

    public Double asDouble(String key, Double defaultValue) {
        Object value = value(key);
        return (value == null) ? defaultValue : ClassConvertUtils.convert(value, Double.class);
    }

    public boolean asBooleanValue(String key) {
        return asBoolean(key, false).booleanValue();
    }

    public Boolean asBoolean(String key) {
        return asBoolean(key, null);
    }

    public Boolean asBoolean(String key, Boolean defaultValue) {
        Object value = value(key);
        return (value == null) ? defaultValue : ClassConvertUtils.convert(value, Boolean.class);
    }

    public Date asDate(String key) {
        return asDate(key, null);
    }

    public Date asDate(String key, Date defaultValue) {
        Object value = value(key);
        return (value == null) ? defaultValue : ClassConvertUtils.convert(value, Date.class);
    }

    public java.sql.Date asSqlDate(String key) {
        return asSqlDate(key, null);
    }

    public java.sql.Date asSqlDate(String key, java.sql.Date defaultValue) {
        Object value = value(key);
        return (value == null) ? defaultValue : ClassConvertUtils.convert(value, java.sql.Date.class);
    }

    public java.sql.Time asSqlTime(String key) {
        return asSqlTime(key, null);
    }

    public java.sql.Time asSqlTime(String key, java.sql.Time defaultValue) {
        Object value = value(key);
        return (value == null) ? defaultValue : ClassConvertUtils.convert(value, java.sql.Time.class);
    }

    public java.sql.Timestamp asTimestamp(String key) {
        return asTimestamp(key, null);
    }

    public java.sql.Timestamp asTimestamp(String key, java.sql.Timestamp defaultValue) {
        Object value = value(key);
        return (value == null) ? defaultValue : ClassConvertUtils.convert(value, java.sql.Timestamp.class);
    }

    public Class<?> asClass(String key) {
        Object value = value(key);
        return (value == null) ? null : ClassConvertUtils.convert(value, Class.class);
    }

    public File asFile(String key) {
        Object value = value(key);
        return (value == null) ? null : ClassConvertUtils.convert(value, File.class);
    }

    public URL asURL(String key) {
        Object value = value(key);
        return (value == null) ? null : ClassConvertUtils.convert(value, URL.class);
    }

    public <T> T asObject(String key, Class<T> targetClass) {
        Object value = value(key);
        return (value == null) ? null : ClassConvertUtils.convert(value, targetClass);
    }

    public String[] asStrings(String key) {
        String[] values = values(key);
        return (values == null) ? ArrayUtils.EMPTY_STRING_ARRAY : values;
    }

    public Integer[] asInts(String key) {
        String[] values = values(key);
        return (values == null) ? ArrayUtils.EMPTY_INTEGER_OBJECT_ARRAY : ClassConvertUtils.convertArrays(values, Integer.class);
    }

    public Long[] asLongs(String key) {
        String[] values = values(key);
        return (values == null) ? ArrayUtils.EMPTY_LONG_OBJECT_ARRAY : ClassConvertUtils.convertArrays(values, Long.class);
    }

    public Double[] asDoubles(String key) {
        String[] values = values(key);
        return (values == null) ? ArrayUtils.EMPTY_DOUBLE_OBJECT_ARRAY : ClassConvertUtils.convertArrays(values, Double.class);
    }

    public Boolean[] asBooleans(String key) {
        String[] values = values(key);
        return (values == null) ? ArrayUtils.EMPTY_BOOLEAN_OBJECT_ARRAY : ClassConvertUtils.convertArrays(values, Boolean.class);
    }

    public Date[] asDates(String key) {
        String[] values = values(key);
        return (values == null) ? DataConverter.EMPTY_DATE_ARRAY : ClassConvertUtils.convertArrays(values, Date.class);
    }

    public java.sql.Date[] asSqlDates(String key) {
        String[] values = values(key);
        return (values == null) ? DataConverter.EMPTY_SQL_DATE_ARRAY : ClassConvertUtils.convertArrays(values, java.sql.Date.class);
    }

    public java.sql.Time[] asSqlTimes(String key) {
        String[] values = values(key);
        return (values == null) ? DataConverter.EMPTY_SQL_TIME_ARRAY : ClassConvertUtils.convertArrays(values, java.sql.Time.class);
    }

    public java.sql.Timestamp[] asTimestamps(String key) {
        String[] values = values(key);
        return (values == null) ? DataConverter.EMPTY_SQL_TIMESTAMP_ARRAY : ClassConvertUtils.convertArrays(values, java.sql.Timestamp.class);
    }

    public Class<?>[] asClasses(String key) {
        String[] values = values(key);
        return (values == null) ? DataConverter.EMPTY_CLASS_ARRAY : ClassConvertUtils.convertArrays(values, Class.class);
    }

    public File[] asFiles(String key) {
        String[] values = values(key);
        return (values == null) ? DataConverter.EMPTY_FILE_ARRAY : ClassConvertUtils.convertArrays(values, File.class);
    }

    public URL[] asURLs(String key) {
        String[] values = values(key);
        return (values == null) ? DataConverter.EMPTY_URL_ARRAY : ClassConvertUtils.convertArrays(values, URL.class);
    }

    @SuppressWarnings("unchecked")
    public <T> T[] asObjects(String key, Class<T> componentType) {
        String[] values = values(key);
        return (T[]) ((values == null) ? Array.newInstance(componentType, 0) : ClassConvertUtils.convertArrays(values, componentType));
    }
}

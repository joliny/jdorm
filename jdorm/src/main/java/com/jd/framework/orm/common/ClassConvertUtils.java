package com.jd.framework.orm.common;

import java.lang.reflect.Array;
import java.util.Date;
import org.apache.commons.beanutils.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import com.jd.framework.orm.util.DateUtils;

/**
 * apache common-beans ConvertUtils类的增强实现
 */
public class ClassConvertUtils {
    static {
        final DateConverter dateConverter = new DateConverter();
        ConvertUtils.register(dateConverter, java.util.Date.class);
        ConvertUtils.register(dateConverter, java.sql.Timestamp.class);
        ConvertUtils.register(dateConverter, java.sql.Date.class);
        ConvertUtils.register(dateConverter, java.sql.Time.class);

        final BooleanConverter booleanConverter = new BooleanConverter();
        ConvertUtils.register(booleanConverter, Boolean.TYPE);
        ConvertUtils.register(booleanConverter, Boolean.class);
    }

    private static class BooleanConverter implements Converter {
        @Override
        @SuppressWarnings("rawtypes")
        public Object convert(Class type, Object value) {
            if (value == null) {
                return Boolean.TYPE.equals(type) ? false : null;
            }
            if (value instanceof Boolean) {
                return value;
            }
            value = value.toString().toLowerCase();
            if ("true".equals(value)) return true;
            if ("yes".equals(value)) return true;
            if ("on".equals(value)) return true;
            if ("t".equals(value)) return true;
            if ("y".equals(value)) return true;
            if ("1".equals(value)) return true;
            return false;
        }

    }

    private static class DateConverter implements Converter {
        //@formatter:off
		private static final String[] MASKS = new String[] { 
			"yyyy-MM-dd HH:mm:ss.SSS", 
			DateUtils.FORMAT_DATE_TIME, 
			DateUtils.FORMAT_DATE, 
			DateUtils.FORMAT_TIME
		};
		//@formatter:on

        @Override
        @SuppressWarnings("rawtypes")
        public Object convert(Class type, Object value) {
            if (value == null) return null;

            if (value instanceof Date) {
                return convertToDate(type, (Date) value);
            }
            if (value instanceof Number) {
                return convertToDate(type, new Date(((Number) value).longValue()));
            }

            Date date = DateUtils.parseUsingMask(MASKS, value.toString());
            if (date == null) {
                throw new ConversionException("DateConverter cannot convert to date type: " + value);
            }

            return convertToDate(type, date);
        }

        private Object convertToDate(Class<?> type, Date date) {
            if (java.util.Date.class.equals(type)) {
                return date;
            } else if (java.sql.Timestamp.class.equals(type)) {
                return new java.sql.Timestamp(date.getTime());
            } else if (java.sql.Date.class.equals(type)) {
                return new java.sql.Date(date.getTime());
            } else if (java.sql.Time.class.equals(type)) {
                return new java.sql.Time(date.getTime());
            } else {
                throw new ConversionException("DateConverter does not support type: " + type);
            }
        }
    }

    public static String[] convertArrays(Object value) {
        if (value == null) return null;

        if (value instanceof String[]) {
            return (String[]) value;
        }
        if (value.getClass().isArray()) {
            int length = Array.getLength(value);
            String[] values = new String[length];
            for (int i = 0; i < length; i++) {
                values[i] = ObjectUtils.toString(Array.get(value, i));
            }
            return values;
        } else {
            return StringUtils.split(value.toString(), ",");
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] convertArrays(String[] values, Class<T> componentType) {
        return (T[]) ConvertUtils.convert(values, componentType);
    }

    @SuppressWarnings("unchecked")
    public static <T> T convert(String value, Class<T> targetClass) {
        if (value == null) return null;
        if (String.class.equals(targetClass)) {
            return (T) value;
        }

        return (T) ConvertUtils.convert(value, targetClass);
    }

    @SuppressWarnings("unchecked")
    public static <T> T convert(Object value, Class<T> targetClass) {
        if (value == null) return null;
        if (targetClass.isAssignableFrom(value.getClass())) {
            return (T) value;
        }

        return (T) ConvertUtils.convert(value, targetClass);
    }

}

package com.jd.framework.orm.jdbc;

import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.iterators.ArrayIterator;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import com.jd.framework.orm.util.StringUtil;
public class PreparedStatementCreator {
    private static final Pattern namedParameterPattern = Pattern.compile("\\:([a-zA-Z0-9_]+)");
	public static PreparedStatement createPreparedStatement(Connection conn, String sql, Object... parameters) throws Exception {
        if (StringUtil.isNullOrEmpty(parameters)) {
            return createByIterator(conn, sql, null);
        }
        if (parameters.length == 1) {
            Object value = parameters[0];
            if (value == null||value.equals("null")) {
                return createByIterator(conn, sql, null);
            }
            Class<?> clazz = value.getClass();
            if (ClassUtils.isAssignable(clazz, Map.class)) {
                return createByMap(conn, sql, (Map<String, ?>) value);
            }else if (ClassUtils.isAssignable(clazz, Collection.class)) {
                return createByList(conn, sql, (ArrayList<Object>)value);
            } else if (clazz.isPrimitive() || clazz.getName().startsWith("java.")) {
                return createByIterator(conn, sql, new ArrayIterator(parameters));
            } else {//java bean
                return createByMap(conn, sql,PropertyUtils.describe(value) );
            }
        }else{ //array 数组参数
       	 return createByArray(conn, sql, parameters);
       }
    }
    /**
     * Support ? as parameter
     */
    protected static PreparedStatement createByArray(Connection conn, String sql, Object[] parameters) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);
        if (parameters != null) {
            int index = 1;
            for(Object parameter:parameters ){
            	if (parameter == null) {
                    ps.setObject(index, null);
                } else {
                    ps.setObject(index, parameter);
                }
                index++;
            }
        }
        return ps;
    }
    
    /**
     * Support ? as parameter
     */
    protected static PreparedStatement createByList(Connection conn, String sql, ArrayList<Object> parameters) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);
        if (parameters != null) {
            int index = 1;
            for(Object parameter:parameters ){
            	if (parameter == null) {
                    ps.setObject(index, null);
                } else {
                    ps.setObject(index, parameter);
                }
                index++;
            }
        }
        return ps;
    }
    
    /**
     * Support ? as parameter
     */
    protected static PreparedStatement createByIterator(Connection conn, String sql, Iterator<?> parameters) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);
        if (parameters != null) {
            int index = 1;
            while (parameters.hasNext()) {
                Object parameter = parameters.next();
                if (parameter == null) {
                    ps.setObject(index, null);
                } else {
                    ps.setObject(index, parameter);
                }
                index++;
            }
        }
        return ps;
    }

    /**
     * Support :name as parameter, and Array or Collection type
     */
    protected static PreparedStatement createByMap(Connection conn, String sql, Map<String, ?> parameters) throws SQLException {
        StringBuffer sb = new StringBuffer();
        List<Object> params = new ArrayList<Object>();

        Matcher m = namedParameterPattern.matcher(sql);
        while (m.find()) {
            String key = m.group(1);
            Object value = parameters.get(key);
            if (value == null) {
                params.add(null);
                m.appendReplacement(sb, "?");
            } else if (value instanceof Object[]) {
                Object[] array = (Object[]) value;
                if (array.length == 0) {
                    params.add(null);
                    m.appendReplacement(sb, "?");
                } else {
                    for (Object one : array) {
                        params.add(one);
                    }
                    m.appendReplacement(sb, StringUtils.repeat("?", ",", array.length));
                }
            } else if (value instanceof Collection) {
                Collection<?> collection = (Collection<?>) value;
                if (collection.size() == 0) {
                    params.add(null);
                    m.appendReplacement(sb, "?");
                } else {
                    for (Object one : collection) {
                        params.add(one);
                    }
                    m.appendReplacement(sb, StringUtils.repeat("?", ",", collection.size()));
                }
            } else {
                params.add(value);
                m.appendReplacement(sb, "?");
            }
        }
        m.appendTail(sb);
        return createByIterator(conn, sb.toString(), params.iterator());
    }
    
    
}

package com.jd.framework.orm.handle;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 将  ResultSet 转换成 T, 要求转换所有的行。
 */
public interface ResultSetHandler<T> {

    public T handle(ResultSet rs) throws SQLException;

}

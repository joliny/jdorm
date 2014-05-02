package com.jd.framework.orm.mapper;


import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.beanutils.ConvertUtils;

public class SingleColumnRowMapper<T> implements RowMapper<T> {

    private Class<T> targetClass;

    public SingleColumnRowMapper(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T handle(ResultSet rs) throws SQLException {
        Object result = rs.getObject(1);
        return (T) ConvertUtils.convert(result, targetClass);
    }

	@Override
	public Object hand(Class<?> clazz, ResultSet rs) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}

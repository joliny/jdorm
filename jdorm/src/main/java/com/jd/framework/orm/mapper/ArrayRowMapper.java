package com.jd.framework.orm.mapper;



import java.sql.ResultSet;
import java.sql.SQLException;

public class ArrayRowMapper implements RowMapper<Object[]> {

    @Override
    public Object[] handle(ResultSet rs) throws SQLException {
        int cols = rs.getMetaData().getColumnCount();
        Object[] result = new Object[cols];

        for (int i = 0; i < cols; i++) {
            result[i] = rs.getObject(i + 1);
        }

        return result;
    }

	@Override
	public Object hand(Class<?> clazz, ResultSet rs) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}

package com.jd.framework.orm.mapper;



import java.sql.*;
import java.util.Map;
import org.apache.commons.collections.map.CaseInsensitiveMap;

public class MapRowMapper implements RowMapper<Map<String, Object>> {

    @Override
    public Map<String, Object> handle(ResultSet rs) throws SQLException {
        @SuppressWarnings("unchecked")
        Map<String, Object> result = new CaseInsensitiveMap();
        ResultSetMetaData rsmd = rs.getMetaData();
        int cols = rsmd.getColumnCount();

        for (int i = 1; i <= cols; i++) {
            String columnName = rsmd.getColumnLabel(i);
            if (columnName == null || columnName.length() == 0) {
                columnName = rsmd.getColumnName(i);
            }
            result.put(columnName, rs.getObject(i));
        }

        return result;
    }

	@Override
	public Object hand(Class<?> clazz, ResultSet rs) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}

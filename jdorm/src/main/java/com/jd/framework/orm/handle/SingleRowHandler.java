package com.jd.framework.orm.handle;



import java.sql.ResultSet;
import java.sql.SQLException;

import com.jd.framework.orm.mapper.RowMapper;

public class SingleRowHandler<T> implements ResultSetHandler<T> {

    private RowMapper<T> mapper;

    public SingleRowHandler(RowMapper<T> mapper) {
        this.mapper = mapper;
    }

    @Override
    public T handle(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return mapper.handle(rs);
        }
        return null;
    }
}

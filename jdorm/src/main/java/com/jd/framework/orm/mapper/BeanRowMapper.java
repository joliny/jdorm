package com.jd.framework.orm.mapper;


import java.sql.*;
import java.util.Map;

import com.jd.framework.orm.entity.BaseEntity;
import com.jd.framework.orm.util.BeanSQLUtils;
import com.jd.framework.orm.util.StringUtil;

import net.sf.cglib.beans.BeanMap;

public class BeanRowMapper<T> implements RowMapper<T> {

    private Class<T> beanClass;

    public BeanRowMapper(Class<T> beanClass) {
        this.beanClass = beanClass;
    }

	@Override
    public T handle(ResultSet rs) throws SQLException {
        try {
            T bean = beanClass.newInstance();
            if(bean instanceof BaseEntity){
            	BaseEntity baseEntity=(BaseEntity) bean;
            	Map<String, String> rowmap=BeanSQLUtils.getMapperBySchemaInfo(baseEntity.getSchemaInfo());
                ResultSetMetaData metaData = rs.getMetaData();
    			int size = metaData.getColumnCount();
    			Map map = BeanMap.create(bean);
                for (int i = 1; i <= size; i++) {
                	String key=rowmap.get(metaData.getColumnLabel(i));
                	if(!StringUtil.isNullOrEmpty(key)){
                    	Object object=rs.getObject(i);
                    	map.put(key, object);
                	}
                	
                }
            }
            
            return bean;

        } catch (Throwable e) {
            throw new SQLException("Can't set bean property.", e);
        }
    }

	@Override
	public Object hand(Class<?> clazz, ResultSet rs) throws Exception {
		// TODO Auto-generated method stub
		Object entity = null;
		try {
			ResultSetMetaData metaData = rs.getMetaData();
			int size = metaData.getColumnCount();
			entity = clazz.newInstance();
			Map map = BeanMap.create(entity);
			 if(entity instanceof BaseEntity){
	            	BaseEntity baseEntity=(BaseEntity) entity;
	            	Map<String, String> rowmap=BeanSQLUtils.getMapperBySchemaInfo(baseEntity.getSchemaInfo());
	    			for (int i = 1; i <= size; i++) {
	    				String key=rowmap.get(metaData.getColumnLabel(i));
	                	if(!StringUtil.isNullOrEmpty(key)){
	                    	Object object=rs.getObject(i);
	                    	map.put(key, object);
	                	}
	    				//map.put(rowmap.get(metaData.getColumnLabel(i)), rs.getObject(i));
	    			}
			 }
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return entity;
	}

}

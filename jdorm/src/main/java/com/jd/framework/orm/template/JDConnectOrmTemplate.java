/**   
* @Title: JDConnectOrmTemplate.java 
* @Package com.jd.framework.orm.template 
* @Description: TODO(用一句话描述该文件做什么) 
* @author liubing1@jd.com	   
* @date 2014-5-1 下午7:26:19 
* @version V1.0   
*/ 
package com.jd.framework.orm.template;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jd.framework.orm.dialect.Dialect;
import com.jd.framework.orm.exception.SystemException;
import com.jd.framework.orm.handle.ResultSetHandler;
import com.jd.framework.orm.handle.RowListHandler;
import com.jd.framework.orm.handle.SingleRowHandler;
import com.jd.framework.orm.jdbc.PreparedStatementCreator;
import com.jd.framework.orm.mapper.ArrayRowMapper;
import com.jd.framework.orm.mapper.BeanRowMapper;
import com.jd.framework.orm.mapper.MapRowMapper;
import com.jd.framework.orm.mapper.RowMapper;
import com.jd.framework.orm.mapper.SingleColumnRowMapper;
import com.jd.framework.orm.util.JdbcUtils;
import com.jd.framework.orm.util.PageResult;
import com.jd.framework.orm.util.QueryCriteria;
import com.jd.framework.orm.util.SQLUtils;

/**
 * @author liubing1@jd.com
 *
 */
public class JDConnectOrmTemplate {
	
	public <T> List<T> queryAsList(Connection conn,RowMapper<T> rowMapper, String sql, Object... parameters) {
	       // AssertUtils.notNull(rowMapper, "rowMapper is null.");

	        ResultSetHandler<List<T>> rsh = new RowListHandler<T>(rowMapper);
	        return query(conn,rsh, sql, parameters);
	    }

	    public <T> List<T> queryAsList(Connection conn,Class<T> beanClass, String sql, Object... parameters) {
	       // AssertUtils.notNull(beanClass, "beanClass is null.");
	        RowMapper<T> rowMapper = getRowMapper(beanClass);
	        return queryAsList(conn,rowMapper, sql, parameters);
	    }

	    public <T> T queryAsObject(Connection conn,RowMapper<T> rowMapper, String sql, Object... parameters) {
	        ResultSetHandler<T> rsh = new SingleRowHandler<T>(rowMapper);
	        return query(conn,rsh, sql, parameters);
	    }

	    public <T> T queryAsObject(Connection conn,Class<T> beanClass, String sql, Object... parameters) {
	       // AssertUtils.notNull(beanClass, "beanClass is null.");

	        RowMapper<T> rowMapper = getRowMapper(beanClass);
	        return queryAsObject(conn,rowMapper, sql, parameters);
	    }

	    public Integer queryAsInt(Connection conn,String sql, Object... parameters) {
	        return queryAsObject(conn,Integer.class, sql, parameters);
	    }

	    public Long queryAsLong(Connection conn,String sql, Object... parameters) {
	        return queryAsObject(conn,Long.class, sql, parameters);
	    }
	    
	    public Float queryAsFloat(Connection conn,String sql, Object... parameters) {
	        return queryAsObject(conn,Float.class, sql, parameters);
	    }
	    
	    public String queryAsString(Connection conn,String sql, Object... parameters) {
	        return queryAsObject(conn,String.class, sql, parameters);
	    }

	    public Boolean queryAsBoolean(Connection conn,String sql, Object... parameters) {
	        return queryAsObject(conn,Boolean.class, sql, parameters);
	    }

	    public Date queryAsDate(Connection conn,String sql, Object... parameters) {
	        return queryAsObject(conn,Date.class, sql, parameters);
	    }

	    public Map<String, Object> queryAsMap(Connection conn,String sql, Object... parameters) {
	        return queryAsObject(conn,Map.class, sql, parameters);
	    }
	    /***
	     * 分页 
	     * @param beanClass
	     * @param sql
	     * @param queryCriteria
	     * @return
	     */
	    public <T> PageResult<T> queryAsPagelist(Connection conn,Class<T> beanClass, String sql, QueryCriteria queryCriteria) {
	    	PageResult<T> pr = new PageResult<T>();
			pr.setCurrentIndex(queryCriteria.getStartIndex());
			pr.setPageSize(queryCriteria.getPageSize());
			String countsql=SQLUtils.generateCountHql(sql, null);
			if (null != countsql) {//获取总页数
				pr.setTotalCount(this.queryAsLong(conn,countsql,queryCriteria.getQueryCondition()));
			}
			if (0 != queryCriteria.getPageSize()) {
				pr.setTotalPage((int) ((pr.getTotalCount() + queryCriteria.getPageSize() - 1) / queryCriteria.getPageSize()));
				pr.setCurrentPage((int) ((queryCriteria.getStartIndex() + queryCriteria.getPageSize()) / queryCriteria.getPageSize()));	
			}
			if(queryCriteria.getStartIndex()>pr.getTotalPage()){
				queryCriteria.setStartIndex(pr.getTotalPage());
			}
			sql=doGetDialet(conn).sql_pagelist(sql, queryCriteria.getStartIndex(), queryCriteria.getPageSize());
			
			pr.setContent(this.queryAsList(conn,beanClass, sql, queryCriteria.getQueryCondition()));
			return pr;
	    }
	    

	    public <T> T query(Connection conn,ResultSetHandler<T> rsh, String sql, Object... parameters) {
	        //Connection conn = null;
	        PreparedStatement ps = null;
	        ResultSet rs = null;
	        T result = null;

	        try {
	            //conn = getConnection(datasourcename);
	            ps = PreparedStatementCreator.createPreparedStatement(conn, sql, parameters);
	            rs = ps.executeQuery();
	            result = rsh.handle(rs);
	        } catch (Throwable e) {
	            throw SystemException.unchecked(e).set("sql", sql).set("parameters", parameters);
	        } finally {
	            JdbcUtils.closeQuietly(rs);
	            JdbcUtils.closeQuietly(ps);
	            //closeConnection(conn);
	        }
	        return result;
	    }

	    public int execute(Connection conn,String sql, Object... parameters) {
	        //AssertUtils.notNull(sql, "sql is null.");

	       // Connection conn = null;
	        //conn.set
	        PreparedStatement ps = null;
	        int rows = 0;

	        try {
	           // conn = getConnection(datasourcename);
	            ps = PreparedStatementCreator.createPreparedStatement(conn, sql, parameters);
	            rows = ps.executeUpdate();
	        } catch (Exception e) {
	            throw SystemException.unchecked(e).set("sql", sql).set("parameters", parameters);
	        } finally {
	            JdbcUtils.closeQuietly(ps);
	            //closeConnection(conn);
	        }
	        
	        return rows;
	    }

	    public int[] executeBatch(Connection conn,String sql, List<Object[]> parameters) {
	        //AssertUtils.notNull(sql, "sql is null.");

	       // Connection conn = null;
	        PreparedStatement ps = null;
	        int[] rows;

	        try {
	           // conn = getConnection(datasourcename);
	            ps = conn.prepareStatement(sql);
	            for (Object[] parameter : parameters) {
	                for (int i = 0; i < parameter.length; i++) {
	                    ps.setObject(i + 1, parameter[i]);
	                }
	                ps.addBatch();
	            }
	            rows = ps.executeBatch();
	        } catch (SQLException e) {
	            throw SystemException.unchecked(e).set("sql", sql).set("parameters", parameters);
	        } finally {
	            JdbcUtils.closeQuietly(ps);
	            //closeConnection(conn);
	        }

	        return rows;
	    }

	    /**
	     * 判断表是否已经存在
	     */
	    public boolean tableExist(Connection conn,String name) {
	       // Connection conn = null;
	        ResultSet rs = null;
	        try {
	           // conn = getConnection(datasourcename);
	            DatabaseMetaData metaData = conn.getMetaData();
	            rs = metaData.getTables(null, null, name.toUpperCase(), new String[] { "TABLE" });
	            return rs.next();
	        } catch (SQLException e) {
	            throw SystemException.unchecked(e);
	        } finally {
	            JdbcUtils.closeQuietly(rs);
	            //closeConnection(conn);
	        }
	    }

	    public Dialect doGetDialet(Connection conn) {
	        //Connection conn = null;
	        try {
	           // conn = this.getConnection(datasourcename);
	            String name = conn.getMetaData().getDatabaseProductName();
	            return Dialect.getDialect(name);
	        } catch (SQLException e) {
	            throw SystemException.unchecked(e);
	        } finally {
	        	//closeConnection(conn);
	           // JdbcUtils.closeQuietly(conn);
	        }
	    }

	    public <T> RowMapper<T> getRowMapper(Class<T> beanClass) {
	        RowMapper<T> rowMapper;
	        if (beanClass.isArray()) {
	            rowMapper = (RowMapper<T>) new ArrayRowMapper();
	        } else if (beanClass.getName().equals("java.util.Map")) {
	            rowMapper = (RowMapper<T>) new MapRowMapper();
	        } else if (beanClass.getName().startsWith("java.")) {
	            rowMapper = new SingleColumnRowMapper<T>(beanClass);
	        } else {
	            rowMapper = new BeanRowMapper<T>(beanClass);
	        }
	        return rowMapper;
	    }
}

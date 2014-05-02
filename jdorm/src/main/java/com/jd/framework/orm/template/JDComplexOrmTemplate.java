package com.jd.framework.orm.template;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;

import com.jd.framework.orm.dialect.Dialect;
import com.jd.framework.orm.exception.DbError;
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
import com.jd.framework.orm.transaction.JdbcNestedTransaction;
import com.jd.framework.orm.transaction.JdbcTransaction;
import com.jd.framework.orm.transaction.Transaction;
import com.jd.framework.orm.util.JdbcUtils;
import com.jd.framework.orm.util.PageResult;
import com.jd.framework.orm.util.QueryCriteria;
import com.jd.framework.orm.util.SQLUtils;
import com.jd.framework.orm.util.SpringContextUtil;

public class JDComplexOrmTemplate {
	private static final boolean ALLOW_NESTED_TRANSACTION = true;

    // 当前线程(事务)
    private static final ThreadLocal<JdbcTransaction> transationHandler = new ThreadLocal<JdbcTransaction>();
    private  DataSource dataSource;
   // private Dialect dialect;
	public  void setDataSource(String dataSourcename) {
		this.dataSource = (DataSource) SpringContextUtil.getBean(dataSourcename);
		//dialect=doGetDialet(dataSourcename);
	}
	public Dialect getDialect(String dataSourcename) {
		return doGetDialet(dataSourcename);
	}
	public DataSource getDataSource() {
        return dataSource;
    }
    /**
     * 启动一个事务(默认支持子事务)
     */
    public Transaction transaction(String datasourcename) {
        if (transationHandler.get() != null) {
            if (ALLOW_NESTED_TRANSACTION) {
                return new JdbcNestedTransaction(transationHandler.get().getConnection());
            }
            throw new SystemException("Can't begin a nested transaction.", DbError.TRANSACTION_ERROR);
        }
        try {
        	setDataSource(datasourcename);
            JdbcTransaction tx = new JdbcTransaction(dataSource.getConnection(), transationHandler);
            transationHandler.set(tx);
            return tx;
        } catch (SQLException e) {
            throw SystemException.unchecked(e, DbError.TRANSACTION_ERROR);
        }
    }
    /**
     * 获取一个当前线程的连接(事务中)，如果没有，则新建一个。
     */
    private Connection getConnection(String datasourcename) {
        JdbcTransaction tx = transationHandler.get();
        try {
            if (tx == null||tx.getConnection().isClosed()) {
                return  DataSourceUtils.getConnection((DataSource)SpringContextUtil.getBean(datasourcename));
            } else {
            	return tx.getConnection();
            }
        } catch (Exception e) {
            throw SystemException.unchecked(e);
        }
    }
    
    /**
     * 释放一个连接，如果不在conn不在事务中，则关闭它，否则不处理。
     */
    private void closeConnection(Connection conn) {
        if (transationHandler.get() == null) {
            // not in transaction
            JdbcUtils.closeQuietly(conn);
        }
    }

    public <T> List<T> queryAsList(String datasourcename,RowMapper<T> rowMapper, String sql, Object... parameters) {
       // AssertUtils.notNull(rowMapper, "rowMapper is null.");

        ResultSetHandler<List<T>> rsh = new RowListHandler<T>(rowMapper);
        return query(datasourcename,rsh, sql, parameters);
    }

    public <T> List<T> queryAsList(String datasourcename,Class<T> beanClass, String sql, Object... parameters) {
       // AssertUtils.notNull(beanClass, "beanClass is null.");
        RowMapper<T> rowMapper = getRowMapper(beanClass);
        return queryAsList(datasourcename,rowMapper, sql, parameters);
    }

    public <T> T queryAsObject(String datasourcename,RowMapper<T> rowMapper, String sql, Object... parameters) {
        ResultSetHandler<T> rsh = new SingleRowHandler<T>(rowMapper);
        return query(datasourcename,rsh, sql, parameters);
    }

    public <T> T queryAsObject(String datasourcename,Class<T> beanClass, String sql, Object... parameters) {
       // AssertUtils.notNull(beanClass, "beanClass is null.");

        RowMapper<T> rowMapper = getRowMapper(beanClass);
        return queryAsObject(datasourcename,rowMapper, sql, parameters);
    }

    public Integer queryAsInt(String datasourcename,String sql, Object... parameters) {
        return queryAsObject(datasourcename,Integer.class, sql, parameters);
    }

    public Long queryAsLong(String datasourcename,String sql, Object... parameters) {
        return queryAsObject(datasourcename,Long.class, sql, parameters);
    }
    
    public Float queryAsFloat(String datasourcename,String sql, Object... parameters) {
        return queryAsObject(datasourcename,Float.class, sql, parameters);
    }
    
    public String queryAsString(String datasourcename,String sql, Object... parameters) {
        return queryAsObject(datasourcename,String.class, sql, parameters);
    }

    public Boolean queryAsBoolean(String datasourcename,String sql, Object... parameters) {
        return queryAsObject(datasourcename,Boolean.class, sql, parameters);
    }

    public Date queryAsDate(String datasourcename,String sql, Object... parameters) {
        return queryAsObject(datasourcename,Date.class, sql, parameters);
    }

    public Map<String, Object> queryAsMap(String datasourcename,String sql, Object... parameters) {
        return queryAsObject(datasourcename,Map.class, sql, parameters);
    }
    /***
     * 分页 
     * @param beanClass
     * @param sql
     * @param queryCriteria
     * @return
     */
    public <T> PageResult<T> queryAsPagelist(String datasourcename,Class<T> beanClass, String sql, QueryCriteria queryCriteria) {
    	PageResult<T> pr = new PageResult<T>();
		pr.setCurrentIndex(queryCriteria.getStartIndex());
		pr.setPageSize(queryCriteria.getPageSize());
		String countsql=SQLUtils.generateCountHql(sql, null);
		if (null != countsql) {//获取总页数
			pr.setTotalCount(this.queryAsLong(datasourcename,countsql,queryCriteria.getQueryCondition()));
		}
		if (0 != queryCriteria.getPageSize()) {
			pr.setTotalPage((int) ((pr.getTotalCount() + queryCriteria.getPageSize() - 1) / queryCriteria.getPageSize()));
			pr.setCurrentPage((int) ((queryCriteria.getStartIndex() + queryCriteria.getPageSize()) / queryCriteria.getPageSize()));	
		}
		if(queryCriteria.getStartIndex()>pr.getTotalPage()){
			queryCriteria.setStartIndex(pr.getTotalPage());
		}
		sql=getDialect(datasourcename).sql_pagelist(sql, queryCriteria.getStartIndex(), queryCriteria.getPageSize());
		
		pr.setContent(this.queryAsList(datasourcename,beanClass, sql, queryCriteria.getQueryCondition()));
		return pr;
    }
    

    public <T> T query(String datasourcename,ResultSetHandler<T> rsh, String sql, Object... parameters) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        T result = null;

        try {
            conn = getConnection(datasourcename);
            ps = PreparedStatementCreator.createPreparedStatement(conn, sql, parameters);
            rs = ps.executeQuery();
            result = rsh.handle(rs);
        } catch (Throwable e) {
            throw SystemException.unchecked(e).set("sql", sql).set("parameters", parameters);
        } finally {
            JdbcUtils.closeQuietly(rs);
            JdbcUtils.closeQuietly(ps);
            closeConnection(conn);
        }
        return result;
    }

    public int execute(String datasourcename,String sql, Object... parameters) {
        //AssertUtils.notNull(sql, "sql is null.");

        Connection conn = null;
        //conn.set
        PreparedStatement ps = null;
        int rows = 0;

        try {
            conn = getConnection(datasourcename);
            ps = PreparedStatementCreator.createPreparedStatement(conn, sql, parameters);
            rows = ps.executeUpdate();
        } catch (Exception e) {
            throw SystemException.unchecked(e).set("sql", sql).set("parameters", parameters);
        } finally {
        	
            JdbcUtils.closeQuietly(ps);
            closeConnection(conn);
        }
        
        return rows;
    }

    public int[] executeBatch(String datasourcename,String sql, List<Object[]> parameters) {
        //AssertUtils.notNull(sql, "sql is null.");

        Connection conn = null;
        PreparedStatement ps = null;
        int[] rows;

        try {
            conn = getConnection(datasourcename);
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
            closeConnection(conn);
        }

        return rows;
    }

    /**
     * 判断表是否已经存在
     */
    public boolean tableExist(String datasourcename,String name) {
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = getConnection(datasourcename);
            DatabaseMetaData metaData = conn.getMetaData();
            rs = metaData.getTables(null, null, name.toUpperCase(), new String[] { "TABLE" });
            return rs.next();
        } catch (SQLException e) {
            throw SystemException.unchecked(e);
        } finally {
            JdbcUtils.closeQuietly(rs);
            closeConnection(conn);
        }
    }

    private Dialect doGetDialet(String datasourcename) {
        Connection conn = null;
        try {
            conn = this.getConnection(datasourcename);
            String name = conn.getMetaData().getDatabaseProductName();
            return Dialect.getDialect(name);
        } catch (SQLException e) {
            throw SystemException.unchecked(e);
        } finally {
        	closeConnection(conn);
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

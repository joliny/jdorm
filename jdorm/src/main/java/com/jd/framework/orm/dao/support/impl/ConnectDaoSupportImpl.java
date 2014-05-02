/**   
* @Title: ConnectDaoSupportImpl.java 
* @Package com.jd.framework.orm.dao.support.impl 
* @Description: TODO(用一句话描述该文件做什么) 
* @author liubing1@jd.com	   
* @date 2014-5-1 下午7:39:39 
* @version V1.0   
*/ 
package com.jd.framework.orm.dao.support.impl;

import java.sql.Connection;
import java.util.Date;

import com.jd.framework.orm.dao.support.IConnectDaoSupport;
import com.jd.framework.orm.template.JDConnectOrmTemplate;
/**
 * @author liubing1@jd.com
 *
 */
public class ConnectDaoSupportImpl implements IConnectDaoSupport {
	
	public JDConnectOrmTemplate getDao() {
		JDConnectOrmTemplate  dao=new JDConnectOrmTemplate();
		return dao;
	}
	/* (non-Javadoc)
	 * @see com.jd.framework.orm.dao.support.IConnectDaoSupport#tableExist(java.sql.Connection, java.lang.String)
	 */
	@Override
	public boolean tableExist(Connection conn, String tableName) {
		// TODO Auto-generated method stub
		return getDao().tableExist(conn,tableName);
	}

	/* (non-Javadoc)
	 * @see com.jd.framework.orm.dao.support.IConnectDaoSupport#queryAsInt(java.sql.Connection, java.lang.String, java.lang.Object[])
	 */
	@Override
	public Integer queryAsInt(Connection conn, String sql, Object... parameters) {
		// TODO Auto-generated method stub
		return getDao().queryAsInt(conn,sql, parameters);
	}

	/* (non-Javadoc)
	 * @see com.jd.framework.orm.dao.support.IConnectDaoSupport#queryAsLong(java.sql.Connection, java.lang.String, java.lang.Object[])
	 */
	@Override
	public Long queryAsLong(Connection conn, String sql, Object... parameters) {
		// TODO Auto-generated method stub
		return getDao().queryAsLong(conn,sql, parameters);
	}

	/* (non-Javadoc)
	 * @see com.jd.framework.orm.dao.support.IConnectDaoSupport#queryAsFloat(java.sql.Connection, java.lang.String, java.lang.Object[])
	 */
	@Override
	public Float queryAsFloat(Connection conn, String sql, Object... parameters) {
		// TODO Auto-generated method stub
		return getDao().queryAsFloat(conn,sql, parameters);
	}

	/* (non-Javadoc)
	 * @see com.jd.framework.orm.dao.support.IConnectDaoSupport#queryAsString(java.sql.Connection, java.lang.String, java.lang.Object[])
	 */
	@Override
	public String queryAsString(Connection conn, String sql,
			Object... parameters) {
		// TODO Auto-generated method stub
		return getDao().queryAsString(conn,sql, parameters);
	}

	/* (non-Javadoc)
	 * @see com.jd.framework.orm.dao.support.IConnectDaoSupport#queryAsBoolean(java.sql.Connection, java.lang.String, java.lang.Object[])
	 */
	@Override
	public Boolean queryAsBoolean(Connection conn, String sql,
			Object... parameters) {
		// TODO Auto-generated method stub
		return getDao().queryAsBoolean(conn,sql, parameters);
	}

	/* (non-Javadoc)
	 * @see com.jd.framework.orm.dao.support.IConnectDaoSupport#queryAsDate(java.sql.Connection, java.lang.String, java.lang.Object[])
	 */
	@Override
	public Date queryAsDate(Connection conn, String sql, Object... parameters) {
		// TODO Auto-generated method stub
		return getDao().queryAsDate(conn,sql, parameters);
	}

	/* (non-Javadoc)
	 * @see com.jd.framework.orm.dao.support.IConnectDaoSupport#execute(java.sql.Connection, java.lang.String, java.lang.Object[])
	 */
	@Override
	public int execute(Connection conn, String sql, Object... parameters) {
		// TODO Auto-generated method stub
		return getDao().execute(conn,sql, parameters);
	}

	/* (non-Javadoc)
	 * @see com.jd.framework.orm.dao.support.IConnectDaoSupport#renameTable(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	@Override
	public void renameTable(Connection conn, String oldName, String newName) {
		// TODO Auto-generated method stub
		String sql=getDao().doGetDialet(conn).sql_table_rename(oldName, newName);
		getDao().execute(conn,sql, null);
	}

	/* (non-Javadoc)
	 * @see com.jd.framework.orm.dao.support.IConnectDaoSupport#addTableColumn(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void addTableColumn(Connection conn, String table,
			String column_definition, String column_position) {
		// TODO Auto-generated method stub
		String sql=getDao().doGetDialet(conn).sql_column_add(table, column_definition, column_position);
		getDao().execute(conn,sql, null);
	}

	/* (non-Javadoc)
	 * @see com.jd.framework.orm.dao.support.IConnectDaoSupport#modifyTableColumn(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void modifyTableColumn(Connection conn, String table,
			String column_definition, String column_position) {
		// TODO Auto-generated method stub
		String sql=getDao().doGetDialet(conn).sql_column_modify(table, column_definition, column_position);
		getDao().execute(conn,sql, null);
	}

	/* (non-Javadoc)
	 * @see com.jd.framework.orm.dao.support.IConnectDaoSupport#dropTable(java.sql.Connection, java.lang.String)
	 */
	@Override
	public void dropTable(Connection conn, String table) {
		// TODO Auto-generated method stub
		String sql=getDao().doGetDialet(conn).sql_table_drop(table);
		getDao().execute(conn,sql, null);
	}

	/* (non-Javadoc)
	 * @see com.jd.framework.orm.dao.support.IConnectDaoSupport#dropTableColumn(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	@Override
	public void dropTableColumn(Connection conn, String table, String column) {
		// TODO Auto-generated method stub
		String sql=getDao().doGetDialet(conn).sql_column_drop(table, column);
		getDao().execute(conn,sql, null);
	}

}

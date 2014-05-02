package com.jd.framework.orm.dao.support.impl;

import java.util.Date;
import com.jd.framework.orm.dao.support.IComplexDaoSupport;
import com.jd.framework.orm.template.JDComplexOrmTemplate;
import com.jd.framework.orm.transaction.Transaction;

public class ComplexDaoSupportImpl implements IComplexDaoSupport {

    
    public JDComplexOrmTemplate getDao() {
    	JDComplexOrmTemplate  dao=new JDComplexOrmTemplate();
		return dao;
	}
	
	@Override
	public boolean tableExist(String datasourcename,String tableName) {
		// TODO Auto-generated method stub
		return getDao().tableExist(datasourcename,tableName);
	}
	@Override
	public Transaction transaction(String datasourcename) {
		// TODO Auto-generated method stub
		return getDao().transaction(datasourcename);
	}
	@Override
	public Integer queryAsInt(String datasourcename, String sql,
			Object... parameters) {
		// TODO Auto-generated method stub
		return getDao().queryAsInt(datasourcename,sql, parameters);
	}
	@Override
	public Long queryAsLong(String datasourcename, String sql,
			Object... parameters) {
		// TODO Auto-generated method stub
		return getDao().queryAsLong(datasourcename,sql, parameters);
	}
	@Override
	public Float queryAsFloat(String datasourcename, String sql,
			Object... parameters) {
		// TODO Auto-generated method stub
		return getDao().queryAsFloat(datasourcename,sql, parameters);
	}
	@Override
	public String queryAsString(String datasourcename, String sql,
			Object... parameters) {
		// TODO Auto-generated method stub
		return getDao().queryAsString(datasourcename,sql, parameters);
	}
	@Override
	public Boolean queryAsBoolean(String datasourcename, String sql,
			Object... parameters) {
		// TODO Auto-generated method stub
		return getDao().queryAsBoolean(datasourcename,sql, parameters);
	}
	@Override
	public Date queryAsDate(String datasourcename, String sql,
			Object... parameters) {
		// TODO Auto-generated method stub
		return getDao().queryAsDate(datasourcename,sql, parameters);
	}
	@Override
	public int execute(String datasourcename, String sql, Object... parameters) {
		// TODO Auto-generated method stub
		return getDao().execute(datasourcename,sql, parameters);
	}
	@Override
	public void renameTable(String datasourcename, String oldName,
			String newName) {
		// TODO Auto-generated method stub
		String sql=getDao().getDialect(datasourcename).sql_table_rename(oldName, newName);
		getDao().execute(datasourcename,sql, null);
	}
	@Override
	public void addTableColumn(String datasourcename, String table,
			String column_definition, String column_position) {
		// TODO Auto-generated method stub
		String sql=getDao().getDialect(datasourcename).sql_column_add(table, column_definition, column_position);
		getDao().execute(datasourcename,sql, null);
	}
	@Override
	public void modifyTableColumn(String datasourcename, String table,
			String column_definition, String column_position) {
		// TODO Auto-generated method stub
		String sql=getDao().getDialect(datasourcename).sql_column_modify(table, column_definition, column_position);
		getDao().execute(datasourcename,sql, null);
	}
	@Override
	public void dropTable(String datasourcename, String table) {
		// TODO Auto-generated method stub
		String sql=getDao().getDialect(datasourcename).sql_table_drop(table);
		getDao().execute(datasourcename,sql, null);
	}
	@Override
	public void dropTableColumn(String datasourcename, String table,
			String column) {
		// TODO Auto-generated method stub
		String sql=getDao().getDialect(datasourcename).sql_column_drop(table, column);
		getDao().execute(datasourcename,sql, null);
	}

}

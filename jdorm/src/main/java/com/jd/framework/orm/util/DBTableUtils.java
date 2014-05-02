/**
 *  数据库操作类
 */
package com.jd.framework.orm.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jd.framework.orm.dao.support.impl.ComplexDaoSupportImpl;
import com.jd.framework.orm.entity.BaseEntity;

public class DBTableUtils {
	private ComplexDaoSupportImpl DAO;
	private static final Logger logger=LoggerFactory.getLogger(DBTableUtils.class); 
	
	public ComplexDaoSupportImpl getDAO() {
		return DAO;
	}
	public void setDAO(ComplexDaoSupportImpl dAO) {
		DAO = dAO;
	}
	/**
	 * 系统初始化批量创建表格
	 * @param objs
	 */
	public void createTable(List<Object> objs,String datasourname) {
		 try{
			 for(Object obj:objs){
				 BaseEntity baseEntity=(BaseEntity) obj;
				 //SchemaInfo schemaInfo =BeanSQLUtils.getSchemaInfo(obj, BeanSQLUtils.QUERY_TYPE);
				 String createsql=BeanSQLUtils.get_sql_table_create(baseEntity.getSchemaInfo(), DAO.getDao().getDialect(datasourname));
				 DAO.getDao().execute(createsql, null);
			 }
		 }catch(Exception e){
			 logger.error(e.getMessage());
		 }
	}
	/**
	 * 删除表格
	 * @param table
	 */
	public void dropTable(String datasourname,String table){
		String dropsql=DAO.getDao().getDialect(datasourname).sql_table_drop(table);
		DAO.getDao().execute(dropsql, null);
	}
	/**
	 * 重新命名
	 * @param oldName
	 * @param newName
	 */
	public void renameTableColumnName(String datasourname,String oldName, String newName){
		String renameSql=DAO.getDao().getDialect(datasourname).sql_table_rename(oldName, newName);
		DAO.getDao().execute(renameSql, null);
	}
	/**
	 * 增加一列
	 * @param table
	 * @param column_definition
	 * @param column_position
	 */
	public void addTableColumn(String datasourname,String table, String column_definition, String column_position){
		String addTableSql=DAO.getDao().getDialect(datasourname).sql_column_add(table, column_definition, column_position);
		DAO.getDao().execute(addTableSql, null);
	}
	/**
	 * 修改列名
	 * @param table
	 * @param column_definition
	 * @param column_position
	 */
	public void modifyTableColumn(String datasourname,String table, String column_definition, String column_position){
		String modifyTableSql=DAO.getDao().getDialect(datasourname).sql_column_modify(table, column_definition, column_position);
		DAO.getDao().execute(modifyTableSql, null);
	}
	/**
	 * 删除 列名
	 * @param table
	 * @param column
	 */
	public void dropTableColumn(String datasourname,String table, String column){
		String dropTableSql=DAO.getDao().getDialect(datasourname).sql_column_drop(table, column);
		DAO.getDao().execute(dropTableSql, null);
	}
	
	
}

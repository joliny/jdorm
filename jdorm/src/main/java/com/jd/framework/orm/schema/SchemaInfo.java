package com.jd.framework.orm.schema;

import java.util.List;

/**
 * 提供只读的数据库Table对象， 要初始化SchemaInfo，请使用 SchemaInfoImpl
 */
public class SchemaInfo {

	protected List<SchemaColumn> columns;

	protected String tableName;
	protected Class<?> tableClass;
	protected String readDataBase;
	protected String writeDataBase;

	public String getTableName() {
		return tableName;
	}

	public Class<?> getTableClass() {
		return tableClass;
	}

	/**
	 * @param tableName
	 *            the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @param tableClass
	 *            the tableClass to set
	 */
	public void setTableClass(Class<?> tableClass) {
		this.tableClass = tableClass;
	}

	/**
	 * @return the columns
	 */
	public List<SchemaColumn> getColumns() {
		return columns;
	}

	/**
	 * @param columns
	 *            the columns to set
	 */
	public void setColumns(List<SchemaColumn> columns) {
		this.columns = columns;
	}

	/**
	 * @return the readDataBase
	 */
	public String getReadDataBase() {
		return readDataBase;
	}

	/**
	 * @param readDataBase the readDataBase to set
	 */
	public void setReadDataBase(String readDataBase) {
		this.readDataBase = readDataBase;
	}

	/**
	 * @return the writeDataBase
	 */
	public String getWriteDataBase() {
		return writeDataBase;
	}

	/**
	 * @param writeDataBase the writeDataBase to set
	 */
	public void setWriteDataBase(String writeDataBase) {
		this.writeDataBase = writeDataBase;
	}
	
}

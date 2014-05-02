/**   
* @Title: IDaoSupport.java 
* @Package com.jd.framework.orm.dao 
* @Description: TODO(用一句话描述该文件做什么) 
* @author liubing1@jd.com	   
* @date 2014-4-27 下午9:33:59 
* @version V1.0   
*/ 
package com.jd.framework.orm.dao.support;

import java.util.Date;

import com.jd.framework.orm.transaction.Transaction;

/**
 * @author liubing1@jd.com
 *
 */
public interface IComplexDaoSupport {
	

	    // ----- table ---------------------------------------
	    public boolean tableExist(String datasourcename,String tableName);
	    
	    public Transaction transaction(String datasourcename);
	    
	    // ----- query ---------------------------------------
	    public Integer queryAsInt(String datasourcename,String sql, Object... parameters);

	    public Long queryAsLong(String datasourcename,String sql, Object... parameters);
	    
	    public Float queryAsFloat(String datasourcename,String sql,Object... parameters);
	    
	    public String queryAsString(String datasourcename,String sql, Object... parameters);

	    public Boolean queryAsBoolean(String datasourcename,String sql, Object... parameters);

	    public Date queryAsDate(String datasourcename,String sql, Object... parameters);


	    // ----- execute ---------------------------------------
	    public int execute(String datasourcename,String sql, Object... parameters);

	    //public void execute(SimpleDaoHelperCallback<SimpleDaoHelper> callback);
	    
	    // ------DDL Operate-------------------------------------------------
	    public void renameTable(String datasourcename,String oldName, String newName);
	    
	    public void addTableColumn(String datasourcename,String table, String column_definition, String column_position);
	    
	    public void modifyTableColumn(String datasourcename,String table, String column_definition, String column_position);
	    
	    public void dropTable(String datasourcename,String table);
	    
	    public void dropTableColumn(String datasourcename,String table, String column);
}

/**   
* @Title: IConnectDaoSupport.java 
* @Package com.jd.framework.orm.dao.support 
* @Description: TODO(用一句话描述该文件做什么) 
* @author liubing1@jd.com	   
* @date 2014-5-1 下午7:36:58 
* @version V1.0   
*/ 
package com.jd.framework.orm.dao.support;

import java.sql.Connection;
import java.util.Date;
/**
 * @author liubing1@jd.com
 *
 */
public interface IConnectDaoSupport {

    // ----- table ---------------------------------------
    public boolean tableExist(Connection conn,String tableName);
    
    // ----- query ---------------------------------------
    public Integer queryAsInt(Connection conn,String sql, Object... parameters);

    public Long queryAsLong(Connection conn,String sql, Object... parameters);
    
    public Float queryAsFloat(Connection conn,String sql,Object... parameters);
    
    public String queryAsString(Connection conn,String sql, Object... parameters);

    public Boolean queryAsBoolean(Connection conn,String sql, Object... parameters);

    public Date queryAsDate(Connection conn,String sql, Object... parameters);


    // ----- execute ---------------------------------------
    public int execute(Connection conn,String sql, Object... parameters);

    //public void execute(SimpleDaoHelperCallback<SimpleDaoHelper> callback);
    
    // ------DDL Operate-------------------------------------------------
    public void renameTable(Connection conn,String oldName, String newName);
    
    public void addTableColumn(Connection conn,String table, String column_definition, String column_position);
    
    public void modifyTableColumn(Connection conn,String table, String column_definition, String column_position);
    
    public void dropTable(Connection conn,String table);
    
    public void dropTableColumn(Connection conn,String table, String column);
}

/**   
* @Title: IBaseConnectDao.java 
* @Package com.jd.framework.orm.dao 
* @Description: TODO(用一句话描述该文件做什么) 
* @author liubing1@jd.com	   
* @date 2014-5-1 下午7:49:07 
* @version V1.0   
*/ 
package com.jd.framework.orm.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.jd.framework.orm.dao.support.IConnectDaoSupport;
import com.jd.framework.orm.entity.BaseEntity;
import com.jd.framework.orm.util.PageResult;
import com.jd.framework.orm.util.QueryCriteria;

/**
 * @author liubing1@jd.com
 *
 */
public interface IBaseConnectDao<T extends BaseEntity> extends IConnectDaoSupport {
	/**
	 * 
	* <p>Title: </p> 
	* <p>Description:增加 </p> 
	* @param t
	* @throws Exception
	 */
	public void doCreateEntity(Connection conn,T t) throws Exception;
	/**
	 * 
	* <p>Title: </p> 
	* <p>Description:增加 </p> 
	* @param t
	* @throws Exception
	 */
	public void doCreateBySql(Connection conn,String sql,Object...params)throws Exception;
	/**
	 * 
	* <p>Title: </p> 
	* <p>Description: 更新</p> 
	* @param t
	* @throws Exception
	 */
	public void doUpdateEntity(Connection conn,T t) throws Exception;
	/**
	 * 
	* <p>Title: </p> 
	* <p>Description: 更新</p> 
	* @param t
	* @throws Exception
	 */
	public void doUpdateBySql(Connection conn,String sql,Object...params)throws Exception;
	/**
	 * 删除
	* <p>Title: </p> 
	* <p>Description: </p> 
	* @param t
	* @throws Exception
	 */
	public void doRemoveEntity(Connection conn,T t) throws Exception;
	/**
	 * 删除
	* <p>Title: </p> 
	* <p>Description: </p> 
	* @param t
	* @throws Exception
	 */
	public void doRemoveBySql(Connection conn,String sql,Object...params)throws Exception;
	/**	
	* <p>Title: </p> 
	* <p>Description: 根据主键查询</p> 
	* @param id
	* @return 查询结果对象
	* @throws Exception
	 */
	public T findById(Connection conn,Serializable id) throws Exception;
	/**
	* <p>Title: </p> 
	* <p>Description: 查询所有</p> 
	* @return 查询结果对象
	* @throws Exception
	 */
	public List<T> findAll(Connection conn) throws Exception;
	/**
	 * 根据bean 作为参数查询
	* <p>Title: </p> 
	* <p>Description: </p> 
	* @param t
	* @return
	* @throws Exception
	 */
	public List<T> findByBean(Connection conn,T t)throws Exception;
	
	/**
	 * 根据数组查询
	* <p>Title: </p> 
	* <p>Description: </p> 
	* @param params
	* @return
	* @throws Exception
	 */
	public List<T> findByMap(Connection conn,Map<String, Object> params) throws Exception;
	
	/**
	 * 
	* <p>Title: </p> 
	* <p>Description:根据SQL ,params 作为参数查询，返回结果集是list类型， </p> 
	* @param sql
	* @param params
	* @return
	 */
	public List<T> findReturnClassByParamsAndSql(Connection conn,String sql,Class<?> returnentityclass,Object... params)throws Exception;
	
	/**
	 * 
	* <p>Title: </p> 
	* <p>Description:根据SQL ,params 作为参数查询，返回结果集是Integer类型， </p> 
	* @param sql
	* @param object
	* @return
	 */
	public Object findReturnObjectByParamsAndSql(Connection conn,String sql,Class<?>cls,Object...params )throws Exception;
	/**
	 * 
	* <p>Title: </p> 
	* <p>Description:根据SQL ,params 作为参数查询，返回结果集是Integer类型， </p> 
	* @param sql
	* @param object
	* @return
	 */
	public Integer findAsInt(Connection conn,String sql, Object... parameters)throws Exception;
	/**
	 * 
	* <p>Title: </p> 
	* <p>Description:根据SQL ,params 作为参数查询，返回结果集是Integer类型， </p> 
	* @param sql
	* @param object
	* @return
	 */
	public Long findAsLong(Connection conn,String sql, Object... parameters)throws Exception;
	/**
	 * 
	* <p>Title: </p> 
	* <p>Description:根据SQL ,params 作为参数查询，返回结果集是Integer类型， </p> 
	* @param sql
	* @param object
	* @return
	 */
	public Float findAsFloat(Connection conn,String sql, Object... parameters) throws Exception;
	/**
	 * 
	* <p>Title: </p> 
	* <p>Description:根据SQL ,params 作为参数查询，返回结果集是Integer类型， </p> 
	* @param sql
	* @param object
	* @return
	 */
	public String findAsString(Connection conn,String sql, Object... parameters) throws Exception;
	/**
	 * 
	* <p>Title: </p> 
	* <p>Description:根据SQL ,params 作为参数查询，返回结果集是Integer类型， </p> 
	* @param sql
	* @param object
	* @return
	 */
	 public Map<String, Object> findAsMap(Connection conn,String sql, Object... parameters) throws Exception;
	 /**
		 * 
		* <p>Title: </p> 
		* <p>Description:根据SQL ,params 作为参数查询，返回结果集是Integer类型， </p> 
		* @param sql
		* @param object
		* @return
		 */
	 public PageResult<T> findByCriteria(Connection conn,QueryCriteria queryCriteria) throws Exception;
}

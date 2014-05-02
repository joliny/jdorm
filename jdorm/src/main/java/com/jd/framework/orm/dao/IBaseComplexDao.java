/**   
* @Title: IBaseComplexDao.java 
* @Package com.jd.framework.orm.dao 
* @Description: TODO(用一句话描述该文件做什么) 
* @author liubing1@jd.com	   
* @date 2014-5-1 下午5:26:09 
* @version V1.0   
*/ 
package com.jd.framework.orm.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import com.jd.framework.orm.dao.support.IComplexDaoSupport;
import com.jd.framework.orm.entity.BaseEntity;
import com.jd.framework.orm.util.PageResult;
import com.jd.framework.orm.util.QueryCriteria;

/**
 * @author liubing1@jd.com
 *
 */
public interface IBaseComplexDao<T extends BaseEntity> extends IComplexDaoSupport {
	/**
	 * 
	* <p>Title: </p> 
	* <p>Description:增加 </p> 
	* @param t
	* @throws Exception
	 */
	public void doCreateEntity(String datasourcename,T t) throws Exception;
	/**
	 * 
	* <p>Title: </p> 
	* <p>Description:增加 </p> 
	* @param t
	* @throws Exception
	 */
	public void doCreateBySql(String datasourcename,String sql,Object...params)throws Exception;
	/**
	 * 
	* <p>Title: </p> 
	* <p>Description: 更新</p> 
	* @param t
	* @throws Exception
	 */
	public void doUpdateEntity(String datasourcename,T t) throws Exception;
	/**
	 * 
	* <p>Title: </p> 
	* <p>Description: 更新</p> 
	* @param t
	* @throws Exception
	 */
	public void doUpdateBySql(String datasourcename,String sql,Object...params)throws Exception;
	/**
	 * 删除
	* <p>Title: </p> 
	* <p>Description: </p> 
	* @param t
	* @throws Exception
	 */
	public void doRemoveEntity(String datasourcename,T t) throws Exception;
	/**
	 * 删除
	* <p>Title: </p> 
	* <p>Description: </p> 
	* @param t
	* @throws Exception
	 */
	public void doRemoveBySql(String datasourcename,String sql,Object...params)throws Exception;
	/**	
	* <p>Title: </p> 
	* <p>Description: 根据主键查询</p> 
	* @param id
	* @return 查询结果对象
	* @throws Exception
	 */
	public T findById(String datasourcename,Serializable id) throws Exception;
	/**
	* <p>Title: </p> 
	* <p>Description: 查询所有</p> 
	* @return 查询结果对象
	* @throws Exception
	 */
	public List<T> findAll(String datasourcename) throws Exception;
	/**
	 * 根据bean 作为参数查询
	* <p>Title: </p> 
	* <p>Description: </p> 
	* @param t
	* @return
	* @throws Exception
	 */
	public List<T> findByBean(String datasourcename,T t)throws Exception;
	
	/**
	 * 根据数组查询
	* <p>Title: </p> 
	* <p>Description: </p> 
	* @param params
	* @return
	* @throws Exception
	 */
	public List<T> findByMap(String datasourcename,Map<String, Object> params) throws Exception;
	
	/**
	 * 
	* <p>Title: </p> 
	* <p>Description:根据SQL ,params 作为参数查询，返回结果集是list类型， </p> 
	* @param sql
	* @param params
	* @return
	 */
	public List<T> findReturnClassByParamsAndSql(String datasourcename,String sql,Class<?> returnentityclass,Object... params)throws Exception;
	
	/**
	 * 
	* <p>Title: </p> 
	* <p>Description:根据SQL ,params 作为参数查询，返回结果集是Integer类型， </p> 
	* @param sql
	* @param object
	* @return
	 */
	public Object findReturnObjectByParamsAndSql(String datasourcename,String sql,Class<?>cls,Object...params )throws Exception;
	/**
	 * 
	* <p>Title: </p> 
	* <p>Description:根据SQL ,params 作为参数查询，返回结果集是Integer类型， </p> 
	* @param sql
	* @param object
	* @return
	 */
	public Integer findAsInt(String datasourcename,String sql, Object... parameters)throws Exception;
	/**
	 * 
	* <p>Title: </p> 
	* <p>Description:根据SQL ,params 作为参数查询，返回结果集是Integer类型， </p> 
	* @param sql
	* @param object
	* @return
	 */
	public Long findAsLong(String datasourcename,String sql, Object... parameters)throws Exception;
	/**
	 * 
	* <p>Title: </p> 
	* <p>Description:根据SQL ,params 作为参数查询，返回结果集是Integer类型， </p> 
	* @param sql
	* @param object
	* @return
	 */
	public Float findAsFloat(String datasourcename,String sql, Object... parameters) throws Exception;
	/**
	 * 
	* <p>Title: </p> 
	* <p>Description:根据SQL ,params 作为参数查询，返回结果集是Integer类型， </p> 
	* @param sql
	* @param object
	* @return
	 */
	public String findAsString(String datasourcename,String sql, Object... parameters) throws Exception;
	/**
	 * 
	* <p>Title: </p> 
	* <p>Description:根据SQL ,params 作为参数查询，返回结果集是Integer类型， </p> 
	* @param sql
	* @param object
	* @return
	 */
	 public Map<String, Object> findAsMap(String datasourcename,String sql, Object... parameters) throws Exception;
	 /**
		 * 
		* <p>Title: </p> 
		* <p>Description:根据SQL ,params 作为参数查询，返回结果集是Integer类型， </p> 
		* @param sql
		* @param object
		* @return
		 */
	 public PageResult<T> findByCriteria(String datasourcename,QueryCriteria queryCriteria) throws Exception;
}

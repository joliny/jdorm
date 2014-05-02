package com.jd.framework.orm.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jd.framework.orm.dao.IBaseConnectDao;
import com.jd.framework.orm.dao.support.impl.ConnectDaoSupportImpl;
import com.jd.framework.orm.dialect.Dialect;
import com.jd.framework.orm.entity.BaseEntity;
import com.jd.framework.orm.schema.SchemaInfo;
import com.jd.framework.orm.util.BeanSQLUtils;
import com.jd.framework.orm.util.PageResult;
import com.jd.framework.orm.util.QueryCriteria;
import com.jd.framework.orm.util.StringUtil;

public class BaseConnectDaoImpl<T extends BaseEntity> extends ConnectDaoSupportImpl implements
		IBaseConnectDao<T> {
	public  final Log log = LogFactory.getLog(getClass());
	private Class<T> entityClass;
	public BaseConnectDaoImpl() {
		if (ParameterizedType.class.isAssignableFrom(getClass()
				.getGenericSuperclass().getClass())) {
			Type[] actualTypeArguments = ((ParameterizedType) getClass()
					.getGenericSuperclass()).getActualTypeArguments();
			entityClass = (Class<T>) actualTypeArguments[0];
		}
	}
	@Override
	public void doCreateEntity(Connection conn, T t) throws Exception {
		// TODO Auto-generated method stub
		SchemaInfo schemaInfo=t.getSchemaInfo();
		String insertSql=BeanSQLUtils.get_sql_insert(schemaInfo, super.getDao().doGetDialet(conn));
		super.execute(conn,insertSql, null);
	}

	@Override
	public void doCreateBySql(Connection conn, String sql, Object... params)
			throws Exception {
		// TODO Auto-generated method stub
		super.execute(conn,sql, params);
	}

	@Override
	public void doUpdateEntity(Connection conn, T t) throws Exception {
		// TODO Auto-generated method stub
		SchemaInfo schemaInfo=t.getSchemaInfo();
		String updatesql=BeanSQLUtils.get_sql_update(schemaInfo, super.getDao().doGetDialet(conn));
		super.execute(conn,updatesql, null);
	}

	@Override
	public void doUpdateBySql(Connection conn, String sql, Object... params)
			throws Exception {
		// TODO Auto-generated method stub
		super.execute(conn,sql, params);
	}

	@Override
	public void doRemoveEntity(Connection conn, T t) throws Exception {
		// TODO Auto-generated method stub
		SchemaInfo schemaInfo=t.getSchemaInfo();
		String deletesql=BeanSQLUtils.get_sql_delete(schemaInfo, super.getDao().doGetDialet(conn));
		super.execute(conn,deletesql, null);
	}

	@Override
	public void doRemoveBySql(Connection conn, String sql, Object... params)
			throws Exception {
		// TODO Auto-generated method stub
		super.execute(conn,sql, params);
	}

	@Override
	public T findById(Connection conn, Serializable id) throws Exception {
		// TODO Auto-generated method stub
		entityClass.newInstance();
		if(entityClass.newInstance() instanceof BaseEntity){
			BaseEntity baseEntity=entityClass.newInstance();
			SchemaInfo schemaInfo=baseEntity.getSchemaInfo();
			Dialect dialect=super.getDao().doGetDialet(conn);
			String sql=BeanSQLUtils.get_sql_select_object(schemaInfo,dialect );
			return super.getDao().queryAsObject(conn,entityClass, sql, id);
		}
		return null;
	}

	@Override
	public List<T> findAll(Connection conn) throws Exception {
		// TODO Auto-generated method stub
		if(entityClass.newInstance() instanceof BaseEntity){
			BaseEntity baseEntity=entityClass.newInstance();
			SchemaInfo schemaInfo=baseEntity.getSchemaInfo();
			String sql=BeanSQLUtils.get_sql_select_all_object(schemaInfo, super.getDao().doGetDialet(conn));
			return super.getDao().queryAsList(conn,entityClass, sql, null);//根据主键查询
		}
		return null;
	}

	@Override
	public List<T> findByBean(Connection conn, T t) throws Exception {
		// TODO Auto-generated method stub
		SchemaInfo schemaInfo=t.getSchemaInfo();
		Map<String, Object>params=PropertyUtils.describe(t);
		String sql=BeanSQLUtils.get_complex_sql_select_object(schemaInfo, super.getDao().doGetDialet(conn), params, null, null);
		return super.getDao().queryAsList(conn,entityClass, sql, params);//根据主键查询
	}

	@Override
	public List<T> findByMap(Connection conn, Map<String, Object> params)
			throws Exception {
		// TODO Auto-generated method stub
		if(entityClass.newInstance() instanceof BaseEntity){
			BaseEntity baseEntity=entityClass.newInstance();
			SchemaInfo schemaInfo=baseEntity.getSchemaInfo();
			String sql=BeanSQLUtils.get_complex_sql_select_object(schemaInfo, super.getDao().doGetDialet(conn), params, null, null);
			return super.getDao().queryAsList(conn,entityClass, sql, null);//根据主键查询
		}
		return null;
	}

	@Override
	public List<T> findReturnClassByParamsAndSql(Connection conn, String sql,
			Class<?> returnentityclass, Object... params) throws Exception {
		// TODO Auto-generated method stub
		return super.getDao().queryAsList(conn,entityClass, sql, params);
	}

	@Override
	public Object findReturnObjectByParamsAndSql(Connection conn, String sql,
			Class<?> cls, Object... params) throws Exception {
		// TODO Auto-generated method stub
		return super.getDao().queryAsObject(conn,cls, sql, params);
	}

	@Override
	public Integer findAsInt(Connection conn, String sql, Object... parameters)
			throws Exception {
		// TODO Auto-generated method stub
		return super.getDao().queryAsInt(conn,sql, parameters);
	}

	@Override
	public Long findAsLong(Connection conn, String sql, Object... parameters)
			throws Exception {
		// TODO Auto-generated method stub
		return super.getDao().queryAsLong(conn,sql, parameters);
	}

	@Override
	public Float findAsFloat(Connection conn, String sql, Object... parameters)
			throws Exception {
		// TODO Auto-generated method stub
		return super.getDao().queryAsFloat(conn,sql, parameters);
	}

	@Override
	public String findAsString(Connection conn, String sql,
			Object... parameters) throws Exception {
		// TODO Auto-generated method stub
		return super.getDao().queryAsString(conn,sql, parameters);
	}

	@Override
	public Map<String, Object> findAsMap(Connection conn, String sql,
			Object... parameters) throws Exception {
		// TODO Auto-generated method stub
		return super.getDao().queryAsMap(conn,sql, parameters);

	}

	@Override
	public PageResult<T> findByCriteria(Connection conn,
			QueryCriteria queryCriteria) throws Exception {
		// TODO Auto-generated method stub
		if(entityClass.newInstance() instanceof BaseEntity){
			BaseEntity baseEntity=entityClass.newInstance();
			SchemaInfo schemaInfo=baseEntity.getSchemaInfo();
			String orderField="";
	    	if(!BeanSQLUtils.validateTransient(schemaInfo.getColumns(), queryCriteria.getOrderField())){
	    		if(!StringUtil.isNullOrEmpty(queryCriteria.getOrderField())){
		    		orderField=queryCriteria.getOrderField().substring(0, queryCriteria.getOrderField().indexOf("name"));
	    		}
	    	}else{
	    		if(!StringUtil.isNullOrEmpty(queryCriteria.getOrderField())){
	    			orderField=queryCriteria.getOrderField();
	    		}
	    		
	    	}
	    	orderField=BeanSQLUtils.getOrderField(schemaInfo.getColumns(), orderField);
			String sql=BeanSQLUtils.get_complex_sql_select_object(schemaInfo, super.getDao().doGetDialet(conn), queryCriteria.getQueryCondition(), orderField, queryCriteria.getOrderDirection());
	    	return super.getDao().queryAsPagelist(conn,entityClass, sql, queryCriteria);
		}
		return null;
	}

}

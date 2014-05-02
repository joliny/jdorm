package com.jd.framework.orm.complex.transaction.support.jdbc;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.jd.framework.orm.complex.transaction.JDOrmException;
import com.jd.framework.orm.complex.transaction.ResourceManager;
import com.jd.framework.orm.complex.transaction.TransactionDefinition;



/**
 * jdbc资源管理器
 * @author liubing1@jd.com
 * 
 */
public class JdbcResourceManager implements ResourceManager<JdbcSession> {
	protected final DataSource dataSource;

	/**
	 * 根据DataSourceFactory创建对象
	 */
	public JdbcResourceManager(DataSourceFactory factory) {
		this.dataSource = factory.getDataSource();
	}
	
	/**
	 * 根据DataSource创建对象
	 * @param dataSource
	 */
	public JdbcResourceManager(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Class<JdbcSession> getResourceType() {
		return JdbcSession.class;
	}

	public JdbcSession beginTransaction(TransactionDefinition definition, boolean active) {
		JdbcSession session = new JdbcSession(dataSource);
		if (active) {
			session.beginTransaction(definition);
		}
		return session;
	}

	public void commitTransaction(JdbcSession resource) {
		if (resource.isTransactionActive()) {
			resource.commitTransaction();
		}
		resource.closeSession();
	}

	public void rollbackTransaction(JdbcSession resource) {
		try {
			if (resource.isTransactionActive()) {
				resource.rollbackTransaction();
			}
		} catch (Exception ex) {
			throw new JDOrmException(ex);
		} finally {
			resource.closeSession();
		}
	}

	public void close() {
		//ignore
		try {
			dataSource.getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

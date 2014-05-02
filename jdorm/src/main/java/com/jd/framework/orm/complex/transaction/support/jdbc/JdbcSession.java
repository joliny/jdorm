package com.jd.framework.orm.complex.transaction.support.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

import com.jd.framework.orm.complex.transaction.JDOrmException;
import com.jd.framework.orm.complex.transaction.TransactionDefinition;


/**
 * jdbc方式的会话
 * @author liubing1@jd.com
 * @since 0.1
 */
public class JdbcSession {
	/**
	 * 数据源
	 */
	protected final DataSource dataSource;
	/**
	 * 数据库访问链接
	 */
	protected Connection connection;
	/**
	 * 事务活动状态
	 */
	protected boolean txActive;
	/**
	 * 事务定义
	 */
	protected TransactionDefinition definition;

	/**
	 * 根据数据源创建会话
	 */
	public JdbcSession(DataSource dataSource) {
		this.dataSource = dataSource;
		txActive = false;
	}

	/**
	 * 关闭会话
	 */
	public void closeSession() {
		if (connection != null) {
			if (txActive == true) {
				throw new JDOrmException("TX was not closed before closing the session");
			}
			connection = null;
		}
	}

	/**
	 * 返回当前的连接,如果当前无连接,则从数据源获取新的连接,设置非自动提交,以无事务方式执行
	 */
	public Connection getConnection() {
		if(connection == null) {
			try {
				connection = dataSource.getConnection();
				txActive = false;
				connection.setAutoCommit(false);
			} catch (SQLException sex) {
				throw new JDOrmException("Failed to open non-TX connection", sex);
			}
		}
		return connection;
	}

	/**
	 * 事务是否为活动状态
	 */
	public boolean isTransactionActive() {
		return txActive;
	}

	/**
	 * 根据事务定义开启事务
	 */
	public void beginTransaction(TransactionDefinition definition) {
		this.definition = definition;
		openTx();
	}

	/**
	 * 提交事务
	 */
	public void commitTransaction() {
		try {
			connection.commit();
		} catch (SQLException sex) {
			throw new JDOrmException("Commit TX failed", sex);
		} finally {
			closeTx();
		}
	}

	/**
	 * 回滚事务
	 */
	public void rollbackTransaction() {
		try {
			connection.rollback();
		} catch (SQLException sex) {
			throw new JDOrmException("Rollback TX failed", sex);
		} finally {
			closeTx();
		}
	}

	/**
	 * 打开事务
	 */
	protected void openTx() {
		try {
			if (connection == null) {
				connection = dataSource.getConnection();
			}
			txActive = true;
			connection.setAutoCommit(false);
			if (definition.getIsolationLevel().value() != TransactionDefinition.ISOLATION_DEFAULT) {
				connection.setTransactionIsolation(definition.getIsolationLevel().value());
			}
			connection.setReadOnly(definition.isReadOnly());
		} catch (SQLException sex) {
			throw new JDOrmException("Open TX failed", sex);
		}
	}
	
	/**
	 * 关闭事务
	 */
	protected void closeTx() {
		txActive = false;
		try {
			connection.setAutoCommit(true);
		} catch (SQLException sex) {
			throw new JDOrmException("Close TX failed", sex);
		}
	}
}

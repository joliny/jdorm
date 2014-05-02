package com.jd.framework.orm.complex.transaction;

import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;


/**
 * 事务状态类
 * 由{@link TransactionManager}创建并控制,被创建后则绑定到当前线程中
 * @author liubing1@jd.com
 * 
 */
public class TransactionStatus {
	/**
	 * 事务管理器
	 */
	protected final TransactionManager txManager;
	/**
	 * 事务定义
	 */
	protected final TransactionDefinition definition;
	/**
	 * 事务绑定的资源集合
	 */
	protected final Set<ResourceAccess<?>> resources;
	/**
	 * 请求标识
	 */
	protected final Object scope;
	/**
	 * 超时设置-秒
	 */
	protected final long deadline;
	/**
	 * 是否以活动状态启动
	 */
	protected final boolean startAsActive;
	/**
	 * 回滚异常
	 */
	protected Throwable rollbackCause;
	/**
	 * 状态
	 */
	protected Status status;

	/**
	 * 事务对象构造器
	 * @param txManager 事务管理器
	 * @param definition 事务定义
	 * @param scope 请求标识
	 * @param active 是否活动状态
	 */
	public TransactionStatus(TransactionManager txManager, TransactionDefinition definition, Object scope, boolean active) {
		this.txManager = txManager;
		this.definition = definition;
		this.scope = scope;
		this.resources = new HashSet<ResourceAccess<?>>();
		this.deadline = definition.getTransactionTimeout() == TransactionDefinition.DEFAULT_TIMEOUT ?
				TransactionDefinition.DEFAULT_TIMEOUT :
				System.currentTimeMillis() + (definition.getTransactionTimeout() * 1000L);
		this.status = active ? Status.STATUS_ACTIVE : Status.STATUS_NO_TRANSACTION;
		this.startAsActive = active;
		txManager.associateTransaction(this);
	}

	/**
	 * 返回事务的定义
	 */
	public TransactionDefinition getTransactionDefinition() {
		return definition;
	}

	/**
	 * 返回事务管理器
	 */
	public TransactionManager getTransactionManager() {
		return txManager;
	}

	/**
	 * 返回事务请求标识
	 */
	public Object getScope() {
		return scope;
	}

	/**
	 * 返回当前事务状态
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * 事务是否以活动状态启动
	 */
	public boolean isStartAsActive() {
		return startAsActive;
	}

	/**
	 * 事务是否为活动状态
	 */
	public boolean isActive() {
		return status == Status.STATUS_ACTIVE;
	}

	/**
	 * 事务是否为禁止状态
	 */
	public boolean isNoTransaction() {
		return status == Status.STATUS_NO_TRANSACTION;
	}

	/**
	 * 事务是否为提交状态
	 */
	public boolean isCommitted() {
		return status == Status.STATUS_COMMITTED;
	}

	/**
	 * 事务是否为回滚状态
	 */
	public boolean isRolledback() {
		return status == Status.STATUS_ROLLEDBACK;
	}

	/**
	 * 事务是否已完成
	 */
	public boolean isCompleted() {
		return status == Status.STATUS_COMMITTED || status == Status.STATUS_ROLLEDBACK;
	}

	/**
	 * 设置回滚状态(非异常导致)
	 */
	public void setRollbackOnly() {
		setRollbackOnly(null);
	}

	/**
	 * 设置回滚状态
	 */
	public void setRollbackOnly(Throwable th) {
		if (isNoTransaction() == false) {
			if ((status != Status.STATUS_MARKED_ROLLBACK) && (status != Status.STATUS_ACTIVE)) {
				throw new JDOrmException("No active TX that can be marked as rollback only");
			}
		}
		rollbackCause = th;
		status = Status.STATUS_MARKED_ROLLBACK;
	}

	/**
	 * 判断事务是否被标记为回滚状态
	 */
	public boolean isRollbackOnly() {
		return status == Status.STATUS_MARKED_ROLLBACK;
	}

	/**
	 * 如果设置了事务的timeout，则创建事务状态对象时则限定了deadline，此处检查是否已超时
	 * 超时则标记当前事务状态为回滚状态
	 */
	protected void checkTimeout() {
		if (deadline == TransactionDefinition.DEFAULT_TIMEOUT) {
			return;
		}
		if (this.deadline - System.currentTimeMillis() < 0) {
			setRollbackOnly();
			throw new JDOrmException("TX timed out, marked as rollback only");
		}
	}

	/**
	 * 提交并完成当前事务
	 */
	public void commit() {
		checkTimeout();
		commitOrRollback(true);
	}

	/**
	 * 回滚并完成当前事务
	 */
	public void rollback() {
		commitOrRollback(false);
	}

	/**
	 * 所有绑定资源的提交回滚处理
	 */
	protected void commitOrRollback(boolean doCommit) {
		boolean forcedRollback = false;
		if (isNoTransaction() == false) {
			if (isRollbackOnly()) {
				if (doCommit == true) {
					doCommit = false;
					forcedRollback = true;
				}
			} else if (isActive() == false) {
				if (isCompleted()) {
					throw new JDOrmException("TX is already completed, commit or rollback should be called once per TX");
				}
				throw new JDOrmException("No active TX to " + (doCommit ? "commit" : "rollback"));
			}
		}
		if (doCommit == true) {
			commitAllResources();
		} else {
			rollbackAllResources(forcedRollback);
		}
	}

	/**
	 * 提交并关闭所有绑定的资源
	 */
	protected void commitAllResources() {
		status = Status.STATUS_COMMITTING;
		Exception lastException = null;
		Iterator<ResourceAccess<?>> it = resources.iterator();
		while (it.hasNext()) {
			ResourceAccess<?> resource = it.next();
			try {
				resource.commitTransaction();
				it.remove();
			} catch (Exception ex) {
				lastException = ex;
			}
		}
		if (lastException != null) {
			setRollbackOnly(lastException);
			throw new JDOrmException("Commit failed: one or more TX resources couldn't commit a TX", lastException);
		}
		txManager.removeTransaction(this);
		status = Status.STATUS_COMMITTED;
	}

	/**
	 * 回滚并关闭所有绑定的资源
	 */
	protected void rollbackAllResources(boolean wasForced) {
		status = Status.STATUS_ROLLING_BACK;
		Exception lastException = null;
		Iterator<ResourceAccess<?>> it = resources.iterator();
		while (it.hasNext()) {
			ResourceAccess<?> resource = it.next();
			try {
				resource.rollbackTransaction();
			} catch (Exception ex) {
				lastException = ex;
			} finally {
				it.remove();
			}
		}
		txManager.removeTransaction(this);
		status = Status.STATUS_ROLLEDBACK;
		if (lastException != null) {
			status = Status.STATUS_UNKNOWN;
			throw new JDOrmException("Rollback failed: one or more TX resources couldn't rollback a TX", lastException);
		}
		if (wasForced) {
			throw new JDOrmException("TX rolled back because it has been marked as rollback-only", rollbackCause);
		}
	}

	/**
	 * 根据资源类型，请求获取资源.如果在当前事务中未找到对应资源，则创建新的资源并开启事务
	 * @param resourceType 资源类型
	 * @return 资源
	 */
	public <E> E requestResource(Class<E> resourceType) {
		if (isCompleted()) {
			throw new JDOrmException("TX is already completed, resource are not available after commit or rollback");
		}
		if (isRollbackOnly()) {
			throw new JDOrmException("TX is marked as rollback only, resource are not available", rollbackCause);
		}
		if (!isNoTransaction() && !isActive()) {
			throw new JDOrmException("Resources are not available since TX is not active");
		}
		checkTimeout();
		E resource = lookupResource(resourceType);
		if (resource == null) {
			int maxResources = txManager.getMaxResourcesPerTransaction();
			if ((maxResources != -1) && (resources.size() >= maxResources)) {
				throw new JDOrmException("TX already has attached max. number of resources");
			}
			ResourceManager<E> resourceManager = txManager.lookupResourceManager(resourceType);
			resource = resourceManager.beginTransaction(definition, isActive());
			resources.add(new ResourceAccess<E>(this, resourceManager, resource));
		}
		return resource;
	}

	/**
	 * 根据资源类型查找对应的资源
	 * @param resourceType 资源类型
	 * @return 资源
	 */
	@SuppressWarnings("unchecked")
	protected <E> E lookupResource(Class<E> resourceType) {
		for (ResourceAccess<?> resource : resources) {
			if (JDOrmClassHelper.isSubclass(resourceType, resource.getResource().getClass())) {
				return (E) resource.getResource();
			}
		}
		return null;
	}
}

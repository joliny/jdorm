package com.jd.framework.orm.complex.transaction;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * 事务管理器
 * 负责处理事务的传播特性、资源管理,并将事务的状态以thread-local中的集合持有
 * @author liubing1@jd.com
 * 
 */
public class TransactionManager {
	/**
	 * 定义每个事务最大的资源数量
	 */
	protected int maxResourcesPerTransaction;
	/**
	 * 单个资源管理器
	 */
	protected boolean oneResourceManager;
	/**
	 * 是否验证已存在事务
	 */
	protected boolean validateExistingTransaction;
	/**
	 * 是否忽略请求标识
	 */
	protected boolean ignoreScope;
	/**
	 * 资源管理器集合{@link #registerResourceManager}
	 */
	protected Map<Class, ResourceManager> resourceManagers;
	/**
	 * 当前线程持有的事务对象集合
	 */
	protected final ThreadLocal<ArrayList<TransactionStatus>> txStack = new ThreadLocal<ArrayList<TransactionStatus>>();

	/**
	 * 创建事务管理器的构造函数
	 */
	public TransactionManager() {
		this.maxResourcesPerTransaction = -1;
		this.resourceManagers = new HashMap<Class, ResourceManager>();
	}

	/**
	 * 获取每个事务支持的最大资源数量
	 */
	public int getMaxResourcesPerTransaction() {
		return maxResourcesPerTransaction;
	}

	/**
	 * 设置每个事务支持的最大资源数量
	 */
	public void setMaxResourcesPerTransaction(int maxResourcesPerTransaction) {
		this.maxResourcesPerTransaction = maxResourcesPerTransaction;
	}

	/**
	 * 是否对已存在事务进行验证
	 */
	public boolean isValidateExistingTransaction() {
		return validateExistingTransaction;
	}

	/**
	 * 设置是否对已存在事务进行验证
	 * @param validateExistingTransaction
	 */
	public void setValidateExistingTransaction(boolean validateExistingTransaction) {
		this.validateExistingTransaction = validateExistingTransaction;
	}

	/**
	 * 事务管理器中是否为单个资源管理器
	 */
	public boolean isSingleResourceManager() {
		return oneResourceManager;
	}
	/**
	 * 设置事务管理器只允许单个资源管理器
	 */
	public void setSingleResourceManager(boolean oneResourceManager) {
		this.oneResourceManager = oneResourceManager;
	}

	/**
	 * 事务请求标识是否被忽略
	 */
	public boolean isIgnoreScope() {
		return ignoreScope;
	}

	/**
	 * 设置事务请求是否被忽略
	 */
	public void setIgnoreScope(boolean ignoreScope) {
		this.ignoreScope = ignoreScope;
	}

	/**
	 * 返回当前线程中所有事务对象
	 */
	public int totalThreadTransactions() {
		ArrayList<TransactionStatus> txList = txStack.get();
		if (txList == null) {
			return 0;
		}
		return txList.size();
	}

	/**
	 * 根据状态返回当前线程中所有符合条件的事务对象个数
	 */
	public int totalThreadTransactionsWithStatus(Status status) {
		ArrayList<TransactionStatus> txlist = txStack.get();
		if (txlist == null) {
			return 0;
		}
		int count = 0;
		for (TransactionStatus tx : txlist) {
			if (tx.getStatus() == status) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 返回当前线程中所有活动状态的事务对象个数
	 */
	public int totalActiveThreadTransactions() {
		return totalThreadTransactionsWithStatus(Status.STATUS_ACTIVE);
	}

	/**
	 * 判断指定的事务对象是否关联到当前线程
	 */
	public boolean isAssociatedWithThread(TransactionStatus tx) {
		ArrayList<TransactionStatus> txList = txStack.get();
		if (txList == null) {
			return false;
		}
		return txList.contains(tx);
	}

	/**
	 * 从当前线程中移除指定的事务对象
	 */
	protected boolean removeTransaction(TransactionStatus tx) {
		ArrayList<TransactionStatus> txList = txStack.get();
		if (txList == null) {
			return false;
		}

		boolean removed = txList.remove(tx);
		if (removed) {
			totalTransactions--;
		}

		if (txList.isEmpty()) {
			txStack.remove();
		}

		return removed;
	}


	/**
	 * 获取当前线程最后绑定的事务对象
	 */
	public TransactionStatus getTransaction() {
		ArrayList<TransactionStatus> txlist = txStack.get();
		if (txlist == null) {
			return null;
		}
		if (txlist.isEmpty() == true) {
			return null;
		}
		return txlist.get(txlist.size() - 1);
	}

	/**
	 * 关联事务对象到当前线程
	 */
	protected void associateTransaction(TransactionStatus tx) {
		totalTransactions++;
		ArrayList<TransactionStatus> txList = txStack.get();
		if (txList == null) {
			txList = new ArrayList<TransactionStatus>();
			txStack.set(txList);
		}
		txList.add(tx);
	}

	protected int totalTransactions;

	/**
	 * 返回事务管理器所持有的事务对象总数
	 */
	public int totalTransactions() {
		return totalTransactions;
	}

	/**
	 * 创建新的事务对象
	 */
	protected TransactionStatus createNewTransaction(TransactionDefinition definition, Object scope, boolean active) {
		return new TransactionStatus(this, definition, scope, active);
	}

	/**
	 * 根据事务定义(传播特性)返回事务对象
	 */
	public TransactionStatus requestTransaction(TransactionDefinition definition) {
		return requestTransaction(definition, null);
	}

	/**
	 * 根据事务定义(传播特性)、请求标识返回事务对象(已存在或新建)
	 */
	public TransactionStatus requestTransaction(TransactionDefinition definition, Object scope) {
		TransactionStatus currentTx = getTransaction();
		if (isNewTxScope(currentTx, scope) == false) {
			return currentTx;
		}
		switch (definition.getPropagationBehavior()) {
			case PROPAGATION_REQUIRED: return propRequired(currentTx, definition, scope);
			case PROPAGATION_SUPPORTS: return propSupports(currentTx, definition, scope);
			case PROPAGATION_MANDATORY: return propMandatory(currentTx, definition, scope);
			case PROPAGATION_REQUIRES_NEW: return propRequiresNew(currentTx, definition, scope);
			case PROPAGATION_NOT_SUPPORTED: return propNotSupported(currentTx, definition, scope);
			case PROPAGATION_NEVER: return propNever(currentTx, definition, scope);
		}
		throw new JDOrmException("Invalid TX propagation value: " + definition.getPropagationBehavior().value());
	}

	/**
	 * 根据目标请求标识与当前事务的请求标识是否一致
	 */
	protected boolean isNewTxScope(TransactionStatus currentTx, Object destScope) {
		if (ignoreScope == true) {
			return true;
		}
		if (currentTx == null) {
			return true;
		}
		if (destScope == null) {
			return true;
		}
		if (currentTx.getScope() == null) {
			return true;
		}
		return !destScope.equals(currentTx.getScope());
	}

	/**
	 * 事务传播特性检查
	 * @see #setValidateExistingTransaction(boolean) 
	 */
	protected void continueTx(TransactionStatus sourceTx, TransactionDefinition destdefinition) {
		if (validateExistingTransaction == false) {
			return;
		}
		TransactionDefinition sourcedefinition = sourceTx.getTransactionDefinition();
		JDOrmIsolation destIsolationLevel = destdefinition.getIsolationLevel();
		if (destIsolationLevel != JDOrmIsolation.ISOLATION_DEFAULT) {
			JDOrmIsolation currentIsolationLevel = sourcedefinition.getIsolationLevel();
			if (currentIsolationLevel != destIsolationLevel) {
				throw new JDOrmException("Participating TX specifies isolation level: " + destIsolationLevel +
						" which is incompatible with existing TX: " + currentIsolationLevel);
			}
		}
		if ((destdefinition.isReadOnly() == false) && (sourcedefinition.isReadOnly())) {
			throw new JDOrmException("Participating TX is not marked as read-only, but existing TX is");
		}
	}


	/**
	 * 传播特性: REQUIRED
	 * <pre>
	 * None -> T2
	 * T1   -> T1
	 * </pre>
	 */
	protected TransactionStatus propRequired(TransactionStatus currentTx, TransactionDefinition definition, Object scope) {
		if ((currentTx == null) || (currentTx.isNoTransaction() == true)) {
			currentTx = createNewTransaction(definition, scope, true);
		} else {
			continueTx(currentTx, definition);
		}
		return currentTx;
	}

	/**
	 * 传播特性: REQUIRES_NEW
	 * <pre>
	 * None -> T2
	 * T1   -> T2
	 * </pre>
	 */
	protected TransactionStatus propRequiresNew(TransactionStatus currentTx, TransactionDefinition definition, Object scope) {
		return createNewTransaction(definition, scope, true);
	}

	/**
	 * 传播特性: SUPPORTS
	 * <pre>
	 * None -> None
	 * T1   -> T1
	 * </pre>
	 */
	protected TransactionStatus propSupports(TransactionStatus currentTx, TransactionDefinition definition, Object scope) {
		if ((currentTx != null) && (currentTx.isNoTransaction() != true)) {
			continueTx(currentTx, definition);
		}
		if (currentTx == null) {
			currentTx = createNewTransaction(definition, scope, false);
		}
		return currentTx;
	}

	/**
	 * 传播特性: MANDATORY
	 * <pre>
	 * None -> Error
	 * T1   -> T1
	 * </pre>
	 */
	protected TransactionStatus propMandatory(TransactionStatus currentTx, TransactionDefinition definition, Object scope) {
		if ((currentTx == null) || (currentTx.isNoTransaction() == true)) {
			throw new JDOrmException("No existing TX found for TX marked with propagation 'mandatory'");
		}
		continueTx(currentTx, definition);
		return currentTx;
	}

	/**
	 * 传播特性: NOT_SUPPORTED
	 * <pre>
	 * None -> None
	 * T1   -> None
	 * </pre>
	 */
	protected TransactionStatus propNotSupported(TransactionStatus currentTx, TransactionDefinition definition, Object scope) {
		if (currentTx == null) {
			return createNewTransaction(definition, scope, false);
		}
		if (currentTx.isNoTransaction() == true) {
			return currentTx;
		}
		return createNewTransaction(definition, scope, false);
	}

	/**
	 * 传播特性: NEVER
	 * <pre>
	 * None -> None
	 * T1   -> Error
	 * </pre>
	 */
	protected TransactionStatus propNever(TransactionStatus currentTx, TransactionDefinition definition, Object scope) {
		if ((currentTx != null) && (currentTx.isNoTransaction() == false)) {
			throw new JDOrmException("Existing TX found for TX marked with propagation 'never'");
		}
		if (currentTx == null) {
			currentTx = createNewTransaction(definition, scope, false);
		}
		return currentTx;
	}

	/**
	 * 注册资源管理器
	 */
	public void registerResourceManager(ResourceManager<?> resourceManager) {
		if ((oneResourceManager == true) && (resourceManagers.isEmpty() == false)) {
			throw new JDOrmException("TX manager allows only one resource manager");
		}
		this.resourceManagers.put(resourceManager.getResourceType(), resourceManager);
	}

	/**
	 * 根据给定的类型查找资源管理器,未找到时抛出异常
	 */
	@SuppressWarnings("unchecked")
	protected <E> ResourceManager<E> lookupResourceManager(Class<E> resourceType) {
		ResourceManager<E> resourceManager = this.resourceManagers.get(resourceType);
		if (resourceManager == null) {
			throw new JDOrmException("No registered resource manager for resource type: " + resourceType.getSimpleName());
		}
		return resourceManager;
	}

	/**
	 * 关闭并清空所有的资源管理器
	 */
	public void close() {
		for (ResourceManager<?> resourceManager : this.resourceManagers.values()) {
			try {
				resourceManager.close();
			} catch (Exception ex) {
				// ignore exception
			}
		}
		resourceManagers.clear();
	}
}


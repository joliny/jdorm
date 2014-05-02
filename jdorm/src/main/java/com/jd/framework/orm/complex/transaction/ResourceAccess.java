package com.jd.framework.orm.complex.transaction;

/**
 * 资源访问类
 * 持有资源对象及所属的资源管理器
 * @author liubing1@jd.com
 * @since 0.1
 */
public final class ResourceAccess<E> {
	final TransactionStatus transaction;
	final ResourceManager<E> resourceManager;
	private final E resource;

	/**
	 * 创建资源访问对象
	 * @param transaction 事务对象
	 * @param resourceManager 资源管理器
	 * @param resource 资源对象
	 */
	ResourceAccess(TransactionStatus transaction, ResourceManager<E> resourceManager, E resource) {
		this.transaction = transaction;
		this.resourceManager = resourceManager;
		this.resource = resource;
	}

	/**
	 * 提交事务
	 */
	void commitTransaction() {
		resourceManager.commitTransaction(resource);
	}

	/**
	 * 回滚事务
	 */
	void rollbackTransaction() {
		resourceManager.rollbackTransaction(resource);
	}

	/**
	 * 获取资源对象
	 * @return E 资源
	 */
	public E getResource() {
		return resource;
	}
}
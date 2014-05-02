package com.jd.framework.orm.complex.transaction;

/**
 * 资源管理器接口
 * @author liubing1@jd.com
 */
public interface ResourceManager<E> {
	/**
	 * 资源类型
	 * @return Class 类型
	 */
	Class<E> getResourceType();

	/**
	 * 根据活动状态active确定是否创建新的资源并且启动新的事务
	 * 通畅由事务的传播特性来决定
	 * @param definition 事务定义
	 * @param active 活动状态
	 * @return E 资源
	 */
	E beginTransaction(TransactionDefinition definition, boolean active);

	/**
	 * 提交并关闭资源
	 * @param resource 资源
	 */
	void commitTransaction(E resource);

	/**
	 * 回滚并关闭资源
	 * @param resource 资源
	 */
	void rollbackTransaction(E resource);

	/**
	 * 关闭管理器并释放所有资源
	 */
	void close();
}

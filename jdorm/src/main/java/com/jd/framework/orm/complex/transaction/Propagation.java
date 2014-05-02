package com.jd.framework.orm.complex.transaction;

/**
 * 事务传播特性枚举
 * @author liubing1@jd.com
 */
public enum Propagation {
	/**
	 * 支持当前事务，如果当前不存在则新建事务
	 */
	PROPAGATION_REQUIRED(1),

	/**
	 * 支持当前事务，如果当前不存在则以无事务方式执行
	 */
	PROPAGATION_SUPPORTS(2),

	/**
	 * 支持当前事务，如果当前不存在则抛出异常
	 */
	PROPAGATION_MANDATORY(3),

	/**
	 * 存在事务时暂停当前事务，创建新的事务
	 */
	PROPAGATION_REQUIRES_NEW(4),

	/**
	 * 无事务执行，如果当前存在事务则暂停
	 */
	PROPAGATION_NOT_SUPPORTED(5),

	/**
	 * 无事务执行，如果存在事务则抛出异常
	 */
	PROPAGATION_NEVER(6);

	private int value;

	Propagation(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}
}

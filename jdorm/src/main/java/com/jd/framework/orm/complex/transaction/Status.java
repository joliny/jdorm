package com.jd.framework.orm.complex.transaction;

/**
 * 事务状态枚举
 * @author liubing1@jd.com
 * @since 0.1
 */
public enum Status {

	/**
	 * 活动状态
	 */
	STATUS_ACTIVE(0),

	/**
	 * 标记回滚状态
     */
    STATUS_MARKED_ROLLBACK(1),

	/**
	 * 不支持.用于两阶段提交
     */
	STATUS_PREPARED(2),

	/**
	 * 提交状态
	 */
	STATUS_COMMITTED(3),

	/**
	 * 回滚状态
	 */
	STATUS_ROLLEDBACK(4),

	/**
     * 不确定状态
     */
	STATUS_UNKNOWN(5),

	/**
	 * 无事务状态(自动提交模式)
	 */
	STATUS_NO_TRANSACTION(6),

	/**
	 * 不支持.用于两阶段提交
	 */
	STATUS_PREPARING(7),
	
	/**
	 * 提交中状态
	 */
	STATUS_COMMITTING(8),

	/**
	 * 回滚中状态
	 */
	STATUS_ROLLING_BACK(9);
	

	private int value;

	Status(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}
}

package com.jd.framework.orm.complex.transaction;

/**
 * 事务隔离级别枚举
 * @author liubing1@jd.com
 * 
 */
public enum JDOrmIsolation {
	ISOLATION_DEFAULT(TransactionDefinition.ISOLATION_DEFAULT),
	ISOLATION_NONE(TransactionDefinition.ISOLATION_NONE),
	ISOLATION_READ_UNCOMMITTED(TransactionDefinition.ISOLATION_READ_UNCOMMITTED),
	ISOLATION_READ_COMMITTED(TransactionDefinition.ISOLATION_READ_COMMITTED),
	ISOLATION_REPEATABLE_READ(TransactionDefinition.ISOLATION_REPEATABLE_READ),
	ISOLATION_SERIALIZABLE(TransactionDefinition.ISOLATION_SERIALIZABLE);

	private int value;

	JDOrmIsolation(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}
}

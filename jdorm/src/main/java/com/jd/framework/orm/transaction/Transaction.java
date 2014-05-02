package com.jd.framework.orm.transaction;

/**
 * 事务对象
 */
public interface Transaction {
    /**
     * 提交一个事务
     */
    public void commit();

    /**
     * 回滚一个事务
     */
    public void rollback();

    /**
     * 结束一个事务
     */
    public void close();
}

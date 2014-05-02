package com.jd.framework.orm.complex.transaction;

import com.jd.framework.orm.complex.transaction.proxy.AnnotationTransactionManager;


/**
 * jdorm 帮助类
 * 1.获取{@link TransactionManager}
 * 2.获取{@link AnnotationTransactionManager}
 * @author liubing1@jd.com
 */
public class JDOrmHelper {
	private static TransactionManager transactionManager = new TransactionManager();
	private static AnnotationTransactionManager annotationTransactionManager;
	public static TransactionManager getTxManager() {
		return transactionManager;
	}
	
	public static AnnotationTransactionManager getAnnotationTxManager() {
		if(annotationTransactionManager == null) {
			annotationTransactionManager = new AnnotationTransactionManager(transactionManager);
		}
		return annotationTransactionManager;
	}
}

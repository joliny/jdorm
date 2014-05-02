package com.jd.framework.orm.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

import com.jd.framework.orm.complex.transaction.JDOrmIsolation;
import com.jd.framework.orm.complex.transaction.*;
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface TransactionAnnotation {

	/**
	 * 事务传播特性. 默认: <code>PROPAGATION_REQUIRED</code>.
	 */
	Propagation propagation() default Propagation.PROPAGATION_REQUIRED;

	/**
	 * 事务隔离级别. 默认: <code>ISOLATION_DEFAULT</code>.
	 */
	JDOrmIsolation isolation() default JDOrmIsolation.ISOLATION_DEFAULT;

	/**
	 * 只读模式. 默认: <code>false</code>
	 */
	boolean readOnly() default false;

	/**
	 * 超时设置. 默认: -1
	 */
	int timeout() default TransactionDefinition.DEFAULT_TIMEOUT;
	
}

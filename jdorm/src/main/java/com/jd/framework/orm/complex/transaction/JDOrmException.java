package com.jd.framework.orm.complex.transaction;
/**
 * jdorm异常
 * @author liubing1@jd.com
 * 
 */
public class JDOrmException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3582820603654445935L;
	public JDOrmException() {
		super();
	}

	public JDOrmException(String msg, Throwable cause) {
		super(msg);
		super.initCause(cause);
	}

	public JDOrmException(String msg) {
		super(msg);
	}

	public JDOrmException(Throwable cause) {
		super();
		super.initCause(cause);
	}
}

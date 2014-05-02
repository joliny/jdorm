/**
 * 
 */
package com.jd.framework.orm.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
 * 自定义 列名注解 
 * @ClassName: Column  
 * @author:liubing  284520459@qq.com 
 * @date 2013-12-22 下午7:42:32 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {
	 /**
	  * 列名
	  * @return
	  */
	 String name() default "";
	 /**
	  * 是否是主键
	  * @return
	  */
	 public boolean isKey() default false;//默认 不是主键
	 /**
	  * 是否可以为空
	  * @return
	  */
	 public boolean isNull() default true;// 默认为空 
	 /**
	  * 字段长度
	  * @return
	  */
	 public int typeLength() default 0;// 默认长度为0
	 /**
	  * 范围
	  * @return
	  */
	 public int typeScale() default 0;// 默认长度为0 
	 
	 public boolean istransient() default false;//是否不存数据库
}

package com.jd.framework.orm.annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 自定义表名注解 
  * @ClassName: Table  
  * @author:liubing  284520459@qq.com 
  * @date 2013-12-22 下午7:41:39
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table {
	 
	String name() ;//对应数据库表名
	
	String readschema();//读取数据库
	
	String writeschema();//写入数据库
}

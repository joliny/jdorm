package com.jd.framework.orm.complex.transaction;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Class帮助类
 * @author liubing1@jd.com
 */
public class JDOrmClassHelper {
	/**
	 * 读取指定注解的name对应的值
	 */
	public static Object readAnnotationValue(Annotation annotation, String name) {
		try {
			Method method  = annotation.annotationType().getDeclaredMethod(name);
			return method.invoke(annotation);
		} catch (Exception ignore) {
			return null;
		}
	}
	/**
	 * 判断给定的类型是否与目标类型一致，或者未目标类型的子类
	 */
	public static boolean isSubclass(Class<?> thisClass, Class<?> target) {
		if (target.isInterface()) {
			return isInterfaceImpl(thisClass, target);
		}
		for (Class<?> x = thisClass; x != null; x = x.getSuperclass()) {
			if (x == target) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断给定的类thisClass是否为targetInterface接口的实现类
	 */
	public static boolean isInterfaceImpl(Class<?> thisClass, Class<?> targetInterface) {
		for (Class<?> x = thisClass; x != null; x = x.getSuperclass()) {
			Class<?>[] interfaces = x.getInterfaces();
			for (Class<?> i : interfaces) {
				if (i == targetInterface) {
					return true;
				}
				if (isInterfaceImpl(i, targetInterface)) {
					return true;
				}
			}
		}
		return false;
	}
}

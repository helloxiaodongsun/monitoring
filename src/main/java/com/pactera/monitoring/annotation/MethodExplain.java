package com.pactera.monitoring.annotation;


import com.pactera.monitoring.enums.MethodType;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface MethodExplain {

	/**
	 * @return 要记录方法的名称。
	 */
	MethodType methodType();
	/**
	 * @return 要记录方法的中文描述。
	 */
	String methodName() default "";

}

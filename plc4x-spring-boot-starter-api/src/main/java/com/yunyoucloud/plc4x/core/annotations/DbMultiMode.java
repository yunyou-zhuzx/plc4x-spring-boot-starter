package com.yunyoucloud.plc4x.core.annotations;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yykj
 */
@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DbMultiMode {
	
	/**
	 * 多个DB块地址.
	 *
	 * @return 地址
	 */
	String[] values();
}

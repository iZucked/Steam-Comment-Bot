/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.tabular;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The TableField annotation applies to getter methods; it's used for the reflective content provider to find out which accessors to use for which columns.
 * 
 * @author hinton
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TableField {
	/**
	 * A hint for where the column for this field should go
	 * 
	 * @return an integer; lower = further left.
	 */
	int order() default 0;

	/**
	 * The name for this column
	 * 
	 * @return a column name
	 */
	String columnText() default "";
}

package com.mmxlabs.lingo.reports.modelbased.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation specifies the initial column sort order in LiNGO
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ColumnSortOrder {
	public int value();
	boolean ascending() default true;
}
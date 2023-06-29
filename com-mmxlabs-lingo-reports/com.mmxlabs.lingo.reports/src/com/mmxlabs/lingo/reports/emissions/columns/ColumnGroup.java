package com.mmxlabs.lingo.reports.emissions.columns;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to combine emission reports columns into groups
 * @author Andrey Popov
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ColumnGroup {
	
	/**
	 * Used to identify column internally
	 */
	public String id() default "";
	
	/**
	 * Displayed title
	 */
	public String headerTitle() default "";

	/**
	 * Higher order positioning of column groups within tables
	 */
    public ColumnOrder position() default ColumnOrder.START;
}
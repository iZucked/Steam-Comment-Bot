package com.mmxlabs.lingo.reports.emissions.columns;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to set the level of priority of order of columns
 * in the C02 emissions reports
 * @author Andrey Popov
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ColumnOrderLevel {
    public ColumnOrder value() default ColumnOrder.START;
}

package com.mmxlabs.lingo.reports.emissions.columns;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ColumnOrderLevel {
    public ColumnOrder level() default ColumnOrder.START;
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.options;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Option {
	String EMPTY_DEFAULT_STRING_HACK = "872846712349876219846192874698126348916723498671234987";

	public String help();

	public String name() default "";

	public String defaultValue() default EMPTY_DEFAULT_STRING_HACK;

	public int order() default 0;
}

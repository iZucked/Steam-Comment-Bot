/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.parser;

import org.eclipse.jdt.annotation.NonNull;

public interface IPrefixOperatorFactory<T> {
	@NonNull
	IExpression<T> createPrefixOperator(char operator, @NonNull IExpression<T> argument);

	boolean isPrefixOperator(char operator);
}

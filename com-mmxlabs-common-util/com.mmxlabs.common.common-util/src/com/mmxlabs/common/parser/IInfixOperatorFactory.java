/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.parser;

import org.eclipse.jdt.annotation.NonNull;

public interface IInfixOperatorFactory<T> {

	@NonNull
	IExpression<T> createInfixOperator(char operator, @NonNull IExpression<T> lhs, @NonNull IExpression<T> rhs);

	/**
	 * Returns true iff operator a binds more strongly than operator b.
	 * 
	 * @param a
	 * @param b
	 * @return true if a > b (e.g. if a='*' and b='+')
	 */
	boolean isOperatorHigherPriority(final char a, final char b);

	boolean isInfixOperator(final char operator);
}

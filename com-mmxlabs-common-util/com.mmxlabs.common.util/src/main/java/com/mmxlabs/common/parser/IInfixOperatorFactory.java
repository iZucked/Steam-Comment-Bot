/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.common.parser;

public interface IInfixOperatorFactory<T> {
	public IExpression<T> createInfixOperator(final char operator, final IExpression<T> lhs, final IExpression<T> rhs);

	/**
	 * Returns true iff operator a binds more strongly than operator b.
	 * 
	 * @param a
	 * @param b
	 * @return true if a > b (e.g. if a='*' and b='+')
	 */
	public boolean isOperatorHigherPriority(final char a, final char b);

	public boolean isInfixOperator(final char operator);
}

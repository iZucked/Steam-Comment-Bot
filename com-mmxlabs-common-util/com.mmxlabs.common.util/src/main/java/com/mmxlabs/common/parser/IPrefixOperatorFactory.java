package com.mmxlabs.common.parser;

public interface IPrefixOperatorFactory<T> {
	public IExpression<T> createPrefixOperator(final char operator, final IExpression<T> argument);
	public boolean isPrefixOperator(final char operator);
}

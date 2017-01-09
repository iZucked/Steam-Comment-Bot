/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.parser;

public interface IExpressionParser<T> {
	public void setPrefixOperatorFactory(final IPrefixOperatorFactory<T> factory);

	public void setInfixOperatorFactory(final IInfixOperatorFactory<T> factory);

	public void setFunctionFactory(final IFunctionFactory<T> factory);

	public void setTermFactory(final ITermFactory<T> factory);

	public IExpression<T> parse(final String s);
}

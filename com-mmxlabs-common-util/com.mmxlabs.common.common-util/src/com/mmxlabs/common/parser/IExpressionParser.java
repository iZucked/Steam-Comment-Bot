/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.common.parser;

public interface IExpressionParser<T> {
	
	void setPrefixOperatorFactory(IPrefixOperatorFactory<T> factory);

	void setInfixOperatorFactory(IInfixOperatorFactory<T> factory);

	void setFunctionFactory(IFunctionFactory<T> factory);

	void setTermFactory(ITermFactory<T> factory);

	IExpression<T> parse(String s);
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.parser;

import java.util.List;

public interface IFunctionFactory<T> {
	public IExpression<T> createFunction(final String name, final List<IExpression<T>> arguments);
}

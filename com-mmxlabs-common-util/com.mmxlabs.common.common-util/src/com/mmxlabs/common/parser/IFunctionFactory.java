/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.parser;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

public interface IFunctionFactory<T> {
	@NonNull IExpression<T> createFunction(@NonNull String name, @NonNull List<IExpression<T>> arguments);
}

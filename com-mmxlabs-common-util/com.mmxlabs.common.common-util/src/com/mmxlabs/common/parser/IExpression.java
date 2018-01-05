/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.common.parser;

import org.eclipse.jdt.annotation.NonNull;

public interface IExpression<T> {
	@NonNull
	T evaluate();
}

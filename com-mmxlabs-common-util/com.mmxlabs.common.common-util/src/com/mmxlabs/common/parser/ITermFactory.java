/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.common.parser;

public interface ITermFactory<T> {
	public IExpression<T> createTerm(final String term);
}

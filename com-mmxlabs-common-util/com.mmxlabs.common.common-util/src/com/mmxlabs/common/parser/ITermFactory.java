/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.common.parser;

import org.eclipse.jdt.annotation.NonNull;

public interface ITermFactory<T> {
	@NonNull IExpression<T> createTerm(@NonNull String term);
}

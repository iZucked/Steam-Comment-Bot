/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser;

import org.eclipse.jdt.annotation.NonNull;

public interface IExpression<T> {
	@NonNull
	T evaluate();

	boolean canEvaluate();

//	@NonNull
//	T evaluate(Pair<ZonedDateTime, ZonedDateTime> earliestAndLatestTime);
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.common.parser;

import java.time.ZonedDateTime;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;

public interface IExpression<T> {
	@NonNull
	T evaluate();

//	@NonNull
//	T evaluate(Pair<ZonedDateTime, ZonedDateTime> earliestAndLatestTime);
}

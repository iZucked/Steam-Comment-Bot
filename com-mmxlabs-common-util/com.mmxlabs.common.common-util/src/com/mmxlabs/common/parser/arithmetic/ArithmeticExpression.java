/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.common.parser.arithmetic;


import java.time.ZonedDateTime;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.IExpression;

public abstract class ArithmeticExpression implements IExpression<@NonNull Double> {
	
	@Override
	public Double evaluate(Pair<ZonedDateTime, ZonedDateTime> earliestAndLatestTime) {
		return evaluate();
	}
}

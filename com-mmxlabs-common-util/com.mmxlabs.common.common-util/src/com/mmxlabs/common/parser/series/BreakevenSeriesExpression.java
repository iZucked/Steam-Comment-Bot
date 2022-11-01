/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;

public class BreakevenSeriesExpression implements IExpression<ISeries> {

	@Override
	public @NonNull ISeries evaluate() {
		return new ISeries() {

			@Override
			public int[] getChangePoints() {
				return null;
			}

			@Override
			public Number evaluate(int point) {
				return null;
			}
		};
	}

	@Override
	public boolean canEvaluate() {
		return true;
	}

}

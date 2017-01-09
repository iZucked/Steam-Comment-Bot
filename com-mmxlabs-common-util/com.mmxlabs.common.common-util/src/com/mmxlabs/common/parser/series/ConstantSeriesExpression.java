/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;

public class ConstantSeriesExpression implements IExpression<ISeries> {
	protected static final int[] NONE = new int[0];
	private Number constant;

	public ConstantSeriesExpression(final Number constant) {
		this.constant = constant;
	}

	@Override
	public @NonNull ISeries evaluate() {
		return new ISeries() {
			@Override
			public int[] getChangePoints() {
				return NONE;
			}

			@Override
			public Number evaluate(int point) {
				return constant;
			}
		};
	}
}

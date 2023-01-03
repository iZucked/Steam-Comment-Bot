/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;

public class ConstantSeriesExpression implements IExpression<ISeries> {
	protected static final int[] NONE = new int[0];
	private final Number constant;

	public ConstantSeriesExpression(final Number constant) {
		this.constant = constant;
	}

	@Override
	public @NonNull ISeries evaluate() {
		return new ISeries() {

			@Override
			public boolean isParameterised() {
				return false;
			}

			@Override
			public Set<String> getParameters() {
				return Collections.emptySet();
			}

			@Override
			public int[] getChangePoints() {
				return NONE;
			}

			@Override
			public Number evaluate(final int timePoint, final Map<String, String> params) {
				return constant;
			}
		};
	}

	public Number getConstant() {
		// TODO Auto-generated method stub
		return constant;
	}

	@Override
	public boolean canEvaluate() {
		return true;
	}

}

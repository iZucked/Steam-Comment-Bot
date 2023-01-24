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

public class BreakevenSeriesExpression implements IExpression<ISeries> {

	@Override
	public @NonNull ISeries evaluate() {
		return new ISeries() {

			@Override
			public int[] getChangePoints() {
				return null;
			}

			@Override
			public boolean isParameterised() {
				return false;
			}

			@Override
			public @NonNull Set<String> getParameters() {
				return Collections.emptySet();
			}

			@Override
			public Number evaluate(int timePoint, @NonNull Map<String, String> parameters) {
				return null;
			}
		};
	}

	@Override
	public boolean canEvaluate() {
		return true;
	}

}

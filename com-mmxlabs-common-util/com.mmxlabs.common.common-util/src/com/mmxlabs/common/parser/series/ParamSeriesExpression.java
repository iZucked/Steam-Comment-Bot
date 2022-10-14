/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.IExpression;

public class ParamSeriesExpression implements IExpression<ISeries> {
	protected static final int[] NONE = new int[0];

	private final Number defaultValue;
	private final String name;

	private final Set<String> parameters;

	public ParamSeriesExpression(final String name) {
		this(name, null);
	}

	public ParamSeriesExpression(final String name, final Number defaultValue) {
		this.name = name;
		this.defaultValue = defaultValue;
		this.parameters = Collections.singleton(name);
	}

	@Override
	public @NonNull ISeries evaluate() {
		return new ISeries() {

			@Override
			public boolean isParameterised() {
				return true;
			}

			@Override
			public Set<String> getParameters() {
				return parameters;
			}

			@Override
			public int[] getChangePoints() {
				return NONE;
			}

			@Override
			public Number evaluate(final int timePoint, final Map<String, String> params) {
				final String v = params.get(name);
				if (v == null) {
					return defaultValue;
				}
				return Double.parseDouble(v);
			}
		};
	}

	@Override
	public boolean canEvaluate() {
		return true;
	}
}

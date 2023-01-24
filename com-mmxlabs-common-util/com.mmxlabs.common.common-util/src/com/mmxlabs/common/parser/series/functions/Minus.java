/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series.functions;

import java.util.Map;
import java.util.Set;

import com.mmxlabs.common.parser.series.ISeries;

public class Minus implements ISeries {

	private final ISeries argument;

	public Minus(ISeries argument) {
		this.argument = argument;
	}

	@Override
	public boolean isParameterised() {
		return argument.isParameterised();
	}

	@Override
	public Set<String> getParameters() {
		return argument.getParameters();
	}

	@Override
	public Number evaluate(final int timePoint, final Map<String, String> params) {
		Number n = argument.evaluate(timePoint, params);
		if (n instanceof Double) {
			return -n.doubleValue();
		} else if (n instanceof Integer) {
			return -n.intValue();
		} else if (n instanceof Long) {
			return -n.longValue();
		} else if (n instanceof Float) {
			return -n.floatValue();
		} else if (n instanceof Short) {
			return -n.shortValue();
		}
		return -n.doubleValue();
	}

	@Override
	public int[] getChangePoints() {
		return argument.getChangePoints();
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

public class EmptySeries implements ISeries {

	public static final @NonNull ISeries INSTANCE = new EmptySeries();

	private final int[] changePoints = new int[0];

	private EmptySeries() {

	}

	@Override
	public boolean isParameterised() {
		return false;
	}

	@Override
	public Set<String> getParameters() {
		return Collections.emptySet();
	}

	public int[] getChangePoints() {
		return changePoints;
	}

	public Number evaluate(int timePoint, @NonNull Map<String, String> parameters) {
		return Integer.valueOf(0);
	}
}

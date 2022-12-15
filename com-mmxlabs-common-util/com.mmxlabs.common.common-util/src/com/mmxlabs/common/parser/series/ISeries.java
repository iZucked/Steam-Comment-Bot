/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

public interface ISeries {

	boolean isParameterised();

	@NonNull
	Set<String> getParameters();

	int[] getChangePoints();

	Number evaluate(int timePoint, @NonNull Map<String, String> parameters);
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.evaluation;

import org.eclipse.jdt.annotation.NonNull;

public interface IEvaluationProcessFactory {
	@NonNull
	String getName();

	@NonNull
	IEvaluationProcess instantiate();
}

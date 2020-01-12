/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class OptimiserConstants {

	public static final String SEQUENCE_TYPE_SEQUENCE_BUILDER = "Sequence-Builder-Sequences";
	public static final String SEQUENCE_TYPE_INITIAL = "Initial-Sequences";
	public static final String SEQUENCE_TYPE_INPUT = "Input-Sequences";

	/**
	 * Some optimisation types require a default vessel for initial cargo allocations.
	 */
	public static final String DEFAULT_VESSEL = "default-vessel";

	public static final String G_AI_fitnessComponents = "general-info-components";

	public static final String G_AI_runtime = "general-info-runtime";

	public static final String G_AI_iterations = "general-info-iters";
	
	public static final String LOGGING_DATA_STORE = "logging-data-store";
}

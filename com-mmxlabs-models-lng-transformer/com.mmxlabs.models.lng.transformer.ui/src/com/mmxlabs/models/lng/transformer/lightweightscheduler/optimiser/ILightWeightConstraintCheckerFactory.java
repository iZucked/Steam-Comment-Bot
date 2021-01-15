/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser;

public interface ILightWeightConstraintCheckerFactory {
	String getName();
	ILightWeightConstraintChecker createConstraintChecker();
}

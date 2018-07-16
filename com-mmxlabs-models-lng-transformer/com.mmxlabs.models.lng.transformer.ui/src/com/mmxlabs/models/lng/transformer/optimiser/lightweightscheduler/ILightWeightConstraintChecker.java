/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler;

import java.util.List;

public interface ILightWeightConstraintChecker {
	boolean checkSequence(List<Integer> sequence, int vessel);
}

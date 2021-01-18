/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser;

import java.util.List;

public interface ILightWeightConstraintChecker {
	boolean checkSequence(List<Integer> sequence, int vessel);
}

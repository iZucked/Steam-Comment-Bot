/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser;

import java.util.List;

public interface IFullLightWeightConstraintChecker {
	boolean checkSequences(List<List<Integer>> sequences);
}

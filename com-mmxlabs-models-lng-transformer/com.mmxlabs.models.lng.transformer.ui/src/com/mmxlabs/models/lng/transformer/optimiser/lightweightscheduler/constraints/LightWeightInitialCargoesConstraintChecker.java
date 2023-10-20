/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.constraints;

import java.util.List;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.LightWeightSchedulerStage2Module;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightConstraintChecker;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightOptimisationData;

public class LightWeightInitialCargoesConstraintChecker implements ILightWeightConstraintChecker {

	@Inject
	ILightWeightOptimisationData lightWeightOptimisationData;
	
	@Inject
	@Named(LightWeightSchedulerStage2Module.LIGHTWEIGHT_INTIAL_SEQUENCES)
	List<List<Integer>> initialSequences;

	public LightWeightInitialCargoesConstraintChecker() {
	}

	@Override
	public boolean checkSequence(List<Integer> sequence, int vessel) {
		return sequence.containsAll(initialSequences.get(vessel));
	}
}

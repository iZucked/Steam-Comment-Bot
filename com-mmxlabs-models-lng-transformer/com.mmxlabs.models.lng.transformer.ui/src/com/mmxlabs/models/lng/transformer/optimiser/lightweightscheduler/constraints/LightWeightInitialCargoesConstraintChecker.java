/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.constraints;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.LightWeightSchedulerStage2Module;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightConstraintChecker;

public class LightWeightInitialCargoesConstraintChecker implements ILightWeightConstraintChecker {

	@Inject
	@Named(LightWeightSchedulerStage2Module.LIGHTWEIGHT_INTIAL_SEQUENCES)
	private List<List<Integer>> initialSequences;

	@Override
	public boolean checkSequence(List<Integer> sequence, int vessel) {
		return sequence.containsAll(initialSequences.get(vessel));
	}
}

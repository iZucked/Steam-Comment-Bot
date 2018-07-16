package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler;

import java.util.List;

public interface ILightWeightConstraintChecker {
	boolean checkSequence(List<Integer> sequence, int vessel);
}

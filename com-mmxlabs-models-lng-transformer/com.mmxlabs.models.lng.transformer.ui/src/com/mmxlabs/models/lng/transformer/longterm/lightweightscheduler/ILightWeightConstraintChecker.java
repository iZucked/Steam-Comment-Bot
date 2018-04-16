package com.mmxlabs.models.lng.transformer.longterm.lightweightscheduler;

import java.util.List;

public interface ILightWeightConstraintChecker {
	boolean checkSequence(List<Integer> sequence, int vessel);
}

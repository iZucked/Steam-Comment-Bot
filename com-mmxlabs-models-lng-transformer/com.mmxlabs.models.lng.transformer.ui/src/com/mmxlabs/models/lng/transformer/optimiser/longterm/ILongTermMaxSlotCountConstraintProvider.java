package com.mmxlabs.models.lng.transformer.optimiser.longterm;

import java.util.Map;
import java.util.Set;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;

public interface ILongTermMaxSlotCountConstraintProvider {
	Map<Set<IDischargeOption>, Integer> getAllMaxDischargeGroupCounts();
	Map<Set<IDischargeOption>, Integer> getAllMinDischargeGroupCounts();
}

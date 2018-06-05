package com.mmxlabs.scheduler.optimiser.providers;

import java.util.List;
import java.util.Set;

import com.mmxlabs.common.Pair;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;

public interface IMaxSlotCountConstraintProvider {
	List<Pair<Set<ILoadOption>, Integer>> getAllMinLoadGroupCounts();
	List<Pair<Set<ILoadOption>, Integer>> getAllMaxLoadGroupCounts();

	List<Pair<Set<IDischargeOption>, Integer>> getAllMinDischargeGroupCounts();
	List<Pair<Set<IDischargeOption>, Integer>> getAllMaxDischargeGroupCounts();
}

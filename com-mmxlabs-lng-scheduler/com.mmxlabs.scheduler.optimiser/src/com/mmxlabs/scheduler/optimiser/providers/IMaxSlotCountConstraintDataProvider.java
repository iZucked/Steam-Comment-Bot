/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.List;
import java.util.Set;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;

public interface IMaxSlotCountConstraintDataProvider extends IDataComponentProvider {
	List<Pair<Set<ILoadOption>, Integer>> getAllMinLoadGroupCounts();
	List<Pair<Set<ILoadOption>, Integer>> getAllMaxLoadGroupCounts();

	List<Pair<Set<IDischargeOption>, Integer>> getAllMinDischargeGroupCounts();
	List<Pair<Set<IDischargeOption>, Integer>> getAllMaxDischargeGroupCounts();
}

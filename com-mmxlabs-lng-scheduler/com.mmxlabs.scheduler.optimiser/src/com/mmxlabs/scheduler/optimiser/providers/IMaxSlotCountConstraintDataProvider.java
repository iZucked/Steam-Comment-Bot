/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.List;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;

public interface IMaxSlotCountConstraintDataProvider<P, C> extends IDataComponentProvider {
	
	List<ConstraintInfo<P, C, ILoadOption>> getAllMinLoadGroupCounts();

	List<ConstraintInfo<P, C, ILoadOption>> getAllMaxLoadGroupCounts();

	List<ConstraintInfo<P, C, IDischargeOption>> getAllMinDischargeGroupCounts();

	List<ConstraintInfo<P, C, IDischargeOption>> getAllMaxDischargeGroupCounts();
}

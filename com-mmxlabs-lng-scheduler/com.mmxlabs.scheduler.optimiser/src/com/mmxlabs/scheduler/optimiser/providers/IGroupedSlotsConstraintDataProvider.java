/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.List;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;

public interface IGroupedSlotsConstraintDataProvider extends IDataComponentProvider {
	
	List<GroupedSlotsConstraintInfo<IDischargeOption>> getAllMinDischargeGroupCounts();
}

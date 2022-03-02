/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;

@NonNullByDefault
public interface IGroupedSlotsConstraintDataProvider extends IDataComponentProvider {
	
	List<GroupedSlotsConstraintInfo<IDischargeOption>> getAllMinDischargeGroupCounts();
}

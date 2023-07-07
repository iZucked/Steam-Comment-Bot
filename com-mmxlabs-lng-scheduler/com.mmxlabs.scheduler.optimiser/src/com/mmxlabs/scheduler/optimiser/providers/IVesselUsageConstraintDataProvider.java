/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Set;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;

public interface IVesselUsageConstraintDataProvider<P, C> extends IDataComponentProvider {
	
	Set<VesselUsageConstraintInfo<P, C, ILoadOption>> getAllLoadVesselUses();

	Set<VesselUsageConstraintInfo<P, C, IDischargeOption>> getAllDischargeVesselUses();
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.Set;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

/**
 * Provides groups of slots to apply count restrictions to.
 * 
 * @author tobi
 *
 */
public interface IVesselUsageConstraintDataProviderEditor<P, C> extends IVesselUsageConstraintDataProvider<P, C> {
	void addLoadSlotVesselUses(P contractProfile, C profileConstraint, Set<ILoadOption> slots, Set<? extends IVessel> vessels, int num);

	void addDischargeSlotVesselUses(P contractProfile, C profileConstraint, Set<IDischargeOption> slots, Set<? extends IVessel> vessels, int num);

}

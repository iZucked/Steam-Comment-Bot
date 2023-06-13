/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashSet;
import java.util.Set;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.IVesselUsageConstraintDataProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.VesselUsageConstraintInfo;

public class DefaultVesselUsageConstraintDataProviderEditor<P, C> implements IVesselUsageConstraintDataProviderEditor<P, C> {

	Set<VesselUsageConstraintInfo<P, C, ILoadOption>> loadVesselUsageRestrictions = new HashSet<>();
	Set<VesselUsageConstraintInfo<P, C, IDischargeOption>> dischargeVesselUsageRestrictions = new HashSet<>();

	@Override
	public void addLoadSlotVesselUses(P contractProfile, C profileConstraint, Set<ILoadOption> slots, Set<? extends IVessel> vessels, int num) {
		loadVesselUsageRestrictions.add(new VesselUsageConstraintInfo<>(contractProfile, profileConstraint, slots, vessels, num));
	}

	@Override
	public void addDischargeSlotVesselUses(P contractProfile, C profileConstraint, Set<IDischargeOption> slots, Set<? extends IVessel> vessels, int num) {
		dischargeVesselUsageRestrictions.add(new VesselUsageConstraintInfo<>(contractProfile, profileConstraint, slots, vessels, num));
	}

	@Override
	public Set<VesselUsageConstraintInfo<P, C, ILoadOption>> getAllLoadVesselUses() {
		return loadVesselUsageRestrictions;
	}

	@Override
	public Set<VesselUsageConstraintInfo<P, C, IDischargeOption>> getAllDischargeVesselUses() {
		return dischargeVesselUsageRestrictions;
	}
}
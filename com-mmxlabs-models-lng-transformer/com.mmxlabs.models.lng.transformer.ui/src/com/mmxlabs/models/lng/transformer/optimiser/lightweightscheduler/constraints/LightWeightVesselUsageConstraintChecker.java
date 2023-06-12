/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.constraints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.IFullLightWeightConstraintChecker;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselUsageConstraintDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.VesselUsageConstraintInfo;

public class LightWeightVesselUsageConstraintChecker implements IFullLightWeightConstraintChecker {

	@Inject
	ILightWeightOptimisationData lightWeightOptimisationData;

	@Inject
	IPortSlotProvider portSlotProvider;

	@Inject
	IVesselUsageConstraintDataProvider vesselUsageConstraintDataProvider;

	@Override
	public boolean checkSequences(List<List<Integer>> sequences) {
		final List<IVesselCharter> vesselCharters = lightWeightOptimisationData.getVessels();
		final List<VesselUsageConstraintInfo<?, ?, IDischargeOption>> allDischargeVesselUsesConstraintInfos = new ArrayList<>(vesselUsageConstraintDataProvider.getAllDischargeVesselUses());
		final List<VesselUsageConstraintInfo<?, ?, ILoadOption>> allLoadVesselConstraintInfos = new ArrayList<>(vesselUsageConstraintDataProvider.getAllLoadVesselUses());
		if (allLoadVesselConstraintInfos.isEmpty() && allDischargeVesselUsesConstraintInfos.isEmpty()) {
			return true;
		}

		final Map<Object, Integer> counts = new HashMap<>();
		for (int i = 0; i < sequences.size(); i++) {
			final IVesselCharter vc = vesselCharters.get(i);
			final List<Integer> cargoIndices = sequences.get(i);

			final IVessel vessel = vc.getVessel();
			for (final int cargoIndex : cargoIndices) {
				List<IPortSlot> cargo = lightWeightOptimisationData.getShippedCargoes().get(cargoIndex);
				for (IPortSlot slot : cargo) {
					for (final VesselUsageConstraintInfo<?, ?, ILoadOption> constraint : allLoadVesselConstraintInfos) {
						if (constraint.getSlots().contains(slot) && constraint.getVessels().contains(vessel)) {
							final int newCount = counts.compute(constraint.getProfileConstraintDistribution(), (k, v) -> v == null ? 1 : v + 1);
							if (newCount > constraint.getBound()) {
								return false;
							}
						}
					}
					for (final VesselUsageConstraintInfo<?, ?, IDischargeOption> constraint : allDischargeVesselUsesConstraintInfos) {
						if (constraint.getSlots().contains(slot) && constraint.getVessels().contains(vessel)) {
							final int newCount = counts.compute(constraint.getProfileConstraintDistribution(), (k, v) -> v == null ? 1 : v + 1);
							if (newCount > constraint.getBound()) {
								return false;
							}
						}
					}
				}
			}
		}
		return true;
	}
}

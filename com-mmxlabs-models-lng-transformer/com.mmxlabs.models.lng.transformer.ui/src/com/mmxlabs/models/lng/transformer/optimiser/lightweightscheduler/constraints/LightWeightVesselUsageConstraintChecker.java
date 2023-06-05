/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.constraints;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.IFullLightWeightConstraintChecker;
import com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.ILightWeightOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselUsageConstraintDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
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
		final Set<VesselUsageConstraintInfo<?, ?, IDischargeOption>> allDischargeVesselUsesConstraintInfos = vesselUsageConstraintDataProvider.getAllDischargeVesselUses();
		final Set<VesselUsageConstraintInfo<?, ?, ILoadOption>> allLoadVesselConstraintInfos = vesselUsageConstraintDataProvider.getAllLoadVesselUses();

		final Map<Object, Integer> counts = new HashMap<>();
		for (int i = 0; i < sequences.size(); i++) {
			final IVesselCharter vc = vesselCharters.get(i);
			final List<Integer> cargoIndices = sequences.get(i);

			final IVessel vessel = vc.getVessel();
			for (final int cargoIndex : cargoIndices) {
				List<IPortSlot> cargo = lightWeightOptimisationData.getShippedCargoes().get(cargoIndex);
				for (IPortSlot slot : cargo) {
					Stream.concat(allLoadVesselConstraintInfos.stream(), allDischargeVesselUsesConstraintInfos.stream())//
							.filter(x -> x.getSlots().contains(slot) && x.getVessels().contains(vessel))//
							.map(x -> x.getProfileConstraintDistribution())//
							.forEach(x -> counts.compute(x, (k, v) -> v == null ? 1 : v + 1));
				}
			}
		}
		for (final VesselUsageConstraintInfo<?, ?, ILoadOption> constraintInfo : allLoadVesselConstraintInfos) {
			final int vesselUses = Objects.requireNonNullElse(counts.get(constraintInfo.getProfileConstraintDistribution()), 0);

			if (vesselUses > constraintInfo.getBound()) {
				return false;
			}
		}
		for (final VesselUsageConstraintInfo<?, ?, IDischargeOption> constraintInfo : allDischargeVesselUsesConstraintInfos) {
			final int vesselUses = Objects.requireNonNullElse(counts.get(constraintInfo.getProfileConstraintDistribution()), 0);

			if (vesselUses > constraintInfo.getBound()) {
				return false;
			}
		}
		return true;
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselUsageConstraintDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.providers.VesselUsageConstraintInfo;

/**
 * {@link IConstraintChecker} to keep groups of slots in their required ranges.
 * 
 * @author achurchill
 */
public class VesselUsageSlotGroupConstraintChecker implements IPairwiseConstraintChecker {

	@Inject
	private IVesselUsageConstraintDataProvider vesselUsageConstraintDataProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IVesselProvider vesselProvider;

	private final @NonNull String name;

	public VesselUsageSlotGroupConstraintChecker(@NonNull final String name) {
		this.name = name;
	}

	@Override
	public @NonNull String getName() {
		return name;
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences sequences, final Collection<@NonNull IResource> changedResources, final List<String> messages) {
		final Set<VesselUsageConstraintInfo<?, ?, IDischargeOption>> allDischargeVesselUsesConstraintInfos = vesselUsageConstraintDataProvider.getAllDischargeVesselUses();
		final Set<VesselUsageConstraintInfo<?, ?, ILoadOption>> allLoadVesselConstraintInfos = vesselUsageConstraintDataProvider.getAllLoadVesselUses();

		final Map<Object, Integer> counts = new HashMap<>();
		final Map<Object, Integer> nominalSlotsCount = new HashMap<>();
		for (final IResource resource : sequences.getResources()) {
			final ISequence sequence = sequences.getSequence(resource);
			final IVesselCharter vesselCharter = vesselProvider.getVesselCharter(resource);
			final IVessel vessel = vesselCharter.getVessel();
			for (final ISequenceElement element : sequence) {
				final IPortSlot slot = portSlotProvider.getPortSlot(element);
				if (slot.getPortType() == PortType.Load || slot.getPortType() == PortType.Discharge) {
					if (vesselCharter.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
						Stream.concat(allLoadVesselConstraintInfos.stream(), allDischargeVesselUsesConstraintInfos.stream()) //
								.filter(x -> x.getSlots().contains(slot) && x.getVessels().contains(vessel)) //
								.map(x -> x.getContractProfile()) //
								.forEach(x -> nominalSlotsCount.compute(x, (k, v) -> v == null ? 1 : v + 1));
					} else {
						Stream.concat(allLoadVesselConstraintInfos.stream(), allDischargeVesselUsesConstraintInfos.stream())//
								.filter(x -> x.getSlots().contains(slot) && x.getVessels().contains(vessel))//
								.map(x -> x.getProfileConstraintDistribution())//
								.forEach(x -> counts.compute(x, (k, v) -> v == null ? 1 : v + 1));
					}
				}
			}

		}
		for (final VesselUsageConstraintInfo<?, ?, ILoadOption> constraintInfo : allLoadVesselConstraintInfos) {
			final int vesselUses = Objects.requireNonNullElse(counts.get(constraintInfo.getProfileConstraintDistribution()), 0);

			if (vesselUses > constraintInfo.getBound()) {
				if (messages != null) {
					messages.add(String.format("%s: Load Slot Vessel Usage not equal to constraint", this.name));
				}
				return false;
			} else if (vesselUses < constraintInfo.getBound()) {
				int remainingNominalsCount = nominalSlotsCount.getOrDefault(constraintInfo.getContractProfile(), 0);
				final int additionalCargoesNeeded = constraintInfo.getBound() - vesselUses;
				if (remainingNominalsCount < additionalCargoesNeeded) {
					return false;
				}
				nominalSlotsCount.compute(constraintInfo.getContractProfile(), (k, v) -> v == null ? 0 : v - additionalCargoesNeeded);
			}
		}
		for (final VesselUsageConstraintInfo<?, ?, IDischargeOption> constraintInfo : allDischargeVesselUsesConstraintInfos) {
			final int vesselUses = Objects.requireNonNullElse(counts.get(constraintInfo.getProfileConstraintDistribution()), 0);

			if (vesselUses > constraintInfo.getBound()) {
				if (messages != null) {
					messages.add(String.format("%s: Discharge Slot Vessel Usage not equal to constraint", this.name));
				}
				return false;
			} else if (vesselUses < constraintInfo.getBound()) {
				int remainingNominalsCount = nominalSlotsCount.getOrDefault(constraintInfo.getContractProfile(), 0);
				final int additionalCargoesNeeded = constraintInfo.getBound() - vesselUses;
				if (remainingNominalsCount < additionalCargoesNeeded) {
					return false;
				}
				nominalSlotsCount.compute(constraintInfo.getContractProfile(), (k, v) -> v == null ? 0 : v - additionalCargoesNeeded);
			}
		}

		return true;

	}

	@Override
	public boolean checkPairwiseConstraint(@NonNull ISequenceElement first, @NonNull ISequenceElement second, @NonNull IResource resource, @Nullable List<@NonNull String> messages) {
		return true;
	}

}

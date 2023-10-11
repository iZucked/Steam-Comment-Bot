/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.scheduler.optimiser.InternalNameMapper;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselUsageConstraintDataProvider;
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

	@Inject
	private InternalNameMapper internalNameMapper;

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
		final List<VesselUsageConstraintInfo<?, ?, IDischargeOption>> allDischargeVesselUsesConstraintInfos = new ArrayList<>(vesselUsageConstraintDataProvider.getAllDischargeVesselUses());
		final List<VesselUsageConstraintInfo<?, ?, ILoadOption>> allLoadVesselConstraintInfos = new ArrayList<>(vesselUsageConstraintDataProvider.getAllLoadVesselUses());
		if (allLoadVesselConstraintInfos.isEmpty() && allDischargeVesselUsesConstraintInfos.isEmpty()) {
			return true;
		}

		final Map<Object, Integer> counts = new HashMap<>();
		for (final IResource resource : sequences.getResources()) {
			final IVesselCharter vesselCharter = vesselProvider.getVesselCharter(resource);
			final IVessel vessel = vesselCharter.getVessel();
			for (final VesselUsageConstraintInfo<?, ?, ILoadOption> constraint : allLoadVesselConstraintInfos) {
				if (constraint.getVessels().contains(vessel)) {
					final ISequence sequence = sequences.getSequence(resource);
					for (final ISequenceElement element : sequence) {
						final IPortSlot slot = portSlotProvider.getPortSlot(element);
						if (constraint.getSlots().contains(slot)) {
							final int newCount = counts.compute(constraint.getProfileConstraintDistribution(), (k, v) -> v == null ? 1 : v + 1);
							if (newCount > constraint.getBound()) {
								if (messages != null) {
									messages.add(String.format("%s: Load Slot Vessel Usage not equal to constraint", this.name));
								}
								return false;
							}
						}

					}
				}
			}
			for (final VesselUsageConstraintInfo<?, ?, IDischargeOption> constraint : allDischargeVesselUsesConstraintInfos) {
				if (constraint.getVessels().contains(vessel)) {
					final ISequence sequence = sequences.getSequence(resource);
					for (final ISequenceElement element : sequence) {
						final IPortSlot slot = portSlotProvider.getPortSlot(element);
						if (constraint.getSlots().contains(slot)) {
							final int newCount = counts.compute(constraint.getProfileConstraintDistribution(), (k, v) -> v == null ? 1 : v + 1);
							if (newCount > constraint.getBound()) {
								if (messages != null) {
									messages.add(String.format("%s: Discharge Slot Vessel Usage not equal to constraint", this.name));
								}
								return false;
							}
						}
					}
				}
			}
		}
		return true;

	}

	@Override
	public boolean checkPairwiseConstraint(@NonNull ISequenceElement first, @NonNull ISequenceElement second, @NonNull IResource resource, @Nullable List<@NonNull String> messages) {
		return true;
	}

}

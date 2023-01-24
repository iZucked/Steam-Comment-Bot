/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * {@link IPairwiseConstraintChecker} to prevent chaining on spot vessels.
 * 
 * @author achurchill
 */
public class MaxSlotsOnSpotChartersConstraintChecker implements IConstraintChecker {
	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	private static final int MAX_SPOT_COUNT = 2;

	private final @NonNull String name;

	public MaxSlotsOnSpotChartersConstraintChecker(final @NonNull String name) {
		this.name = name;
	}

	@Override
	public @NonNull String getName() {
		return name;
	}

	@Override
	public boolean checkConstraints(@NonNull ISequences sequences, @Nullable Collection<@NonNull IResource> changedResources, List<String> messages) {
		for (IResource resource : sequences.getResources()) {
			final IVesselCharter vesselCharter = vesselProvider.getVesselCharter(resource);
			if (vesselCharter.getVesselInstanceType() != VesselInstanceType.SPOT_CHARTER && vesselCharter.getVesselInstanceType() != VesselInstanceType.ROUND_TRIP) {
				continue;
			}
			int spots = 0;
			for (ISequenceElement element : sequences.getSequence(resource)) {
				if (portSlotProvider.getPortSlot(element).getPortType() == PortType.Load || portSlotProvider.getPortSlot(element).getPortType() == PortType.Discharge) {
					spots++;
				}
			}
			if (spots > MAX_SPOT_COUNT) {
				if (messages != null)
					messages.add(String.format("%s: More than one cargo on the round trip charter vessel %s.", this.name, vesselCharter.getVessel().getName()));
				return false;
			}
		}
		return true;
	}

}

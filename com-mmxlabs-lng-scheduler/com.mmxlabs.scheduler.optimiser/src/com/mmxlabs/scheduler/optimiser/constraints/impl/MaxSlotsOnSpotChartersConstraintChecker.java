/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.w3c.dom.views.AbstractView;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.ISpotMarketSlotsProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * {@link IPairwiseConstraintChecker} to prevent chaining on spot vessels.
 * 
 * @author achurchill
 */
public class MaxSlotsOnSpotChartersConstraintChecker implements IPairwiseConstraintChecker {
	@Inject
	@NonNull
	private IVesselProvider vesselProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	private static final int MAX_SPOT_COUNT = 2;

	private final @NonNull String name;

	public MaxSlotsOnSpotChartersConstraintChecker(final @NonNull String name) {
		this.name = name;
	}

	/**
	 * Note: this does not make sense for this constraint
	 */
	@Override
	public boolean checkPairwiseConstraint(final ISequenceElement first, final ISequenceElement second, final IResource resource) {
		return true;
	}

	@Override
	public String explain(final ISequenceElement first, final ISequenceElement second, final IResource resource) {
		return null;
	}

	@Override
	public @NonNull String getName() {
		return name;
	}

	@Override
	public boolean checkConstraints(@NonNull ISequences sequences, @Nullable Collection<@NonNull IResource> changedResources) {
		for (IResource resource : sequences.getResources()) {
			final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
			if (vesselAvailability.getVesselInstanceType() != VesselInstanceType.SPOT_CHARTER && vesselAvailability.getVesselInstanceType() != VesselInstanceType.ROUND_TRIP) {
				continue;
			}
			int spots = 0;
			for (ISequenceElement element : sequences.getSequence(resource)) {
				if (portSlotProvider.getPortSlot(element).getPortType() == PortType.Load || portSlotProvider.getPortSlot(element).getPortType() == PortType.Discharge) {
					spots++;
				}
			}
			if (spots > MAX_SPOT_COUNT) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean checkConstraints(@NonNull ISequences sequences, @Nullable Collection<@NonNull IResource> changedResources, @Nullable List<String> messages) {
		return checkConstraints(sequences, changedResources);
	}

}

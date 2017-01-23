/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPromptPeriodProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRoundTripVesselPermissionProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * A constraint checker which determines whether every port in the sequence is legal for the given vessel. Whether this is the best possible solution is debatable, as it involves a few more lookups
 * than the minimum possible number; we could instead store a map from vessels to excluded <em>elements</em> instead of <em>ports</em>, saving a lookup at the expense of builder complexity.
 * 
 * @author hinton
 * 
 * @param
 */
public class PromptRoundTripVesselPermissionConstraintChecker implements IPairwiseConstraintChecker {

	@Inject
	@NonNull
	private IVesselProvider vesselProvider;

	@Inject
	@NonNull
	private IRoundTripVesselPermissionProvider roundTripVesselPermissionProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IPromptPeriodProvider promptPeriodProvider;

	@NonNull
	private final String name;

	public PromptRoundTripVesselPermissionConstraintChecker(@NonNull final String name) {
		this.name = name;
	}

	@Override
	@NonNull
	public String getName() {
		return name;
	}

	public boolean checkSequence(@NonNull final ISequence sequence, @NonNull final IResource resource) {
		return checkSequence(sequence, resource, null);
	}

	public boolean checkSequence(@NonNull final ISequence sequence, @NonNull final IResource resource, @Nullable final List<String> messages) {
		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		if (vesselAvailability.getVesselInstanceType() != VesselInstanceType.ROUND_TRIP) {
			return true;
		}

		ISequenceElement prevElement = null;
		for (final ISequenceElement element : sequence) {
			if (!checkElement(element, resource)) {
				return false; // fail fast.
			}

			if (prevElement != null) {
				if (!roundTripVesselPermissionProvider.isBoundPair(prevElement, element)) {
					return false;
				}
			}
			prevElement = element;
		}
		return true;
	}

	/*
	 * This is a fail-fast version of the method below
	 */
	@Override
	public boolean checkConstraints(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources) {
		return checkConstraints(sequences, changedResources, null);
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources, @Nullable final List<String> messages) {
		final Collection<@NonNull IResource> loopResources;
		if (changedResources == null) {
			loopResources = sequences.getResources();
		} else {
			loopResources = changedResources;
		}
		boolean valid = true;
		for (final IResource resource : loopResources) {
			final ISequence sequence = sequences.getSequence(resource);
			if (!checkSequence(sequence, resource, messages)) {
				if (messages == null) {
					return false;
				} else {
					valid = false;
				}
			}
		}

		return valid;
	}

	@Override
	public boolean checkPairwiseConstraint(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource) {

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		if (vesselAvailability.getVesselInstanceType() != VesselInstanceType.ROUND_TRIP) {
			return true;
		}

		final boolean valid = checkElement(first, resource) && checkElement(second, resource);
		if (valid) {
			return roundTripVesselPermissionProvider.isBoundPair(first, second);
		}
		return false;
	}

	protected boolean checkElement(final @NonNull ISequenceElement element, final @NonNull IResource resource) {

		final IPortSlot portSlot = portSlotProvider.getPortSlot(element);
		final PortType portType = portSlot.getPortType();

		if (portType == PortType.Start) {
			return true;
		}
		if (portType == PortType.End) {
			return true;
		}
		if (portType == PortType.Round_Trip_Cargo_End) {
			return true;
		}
		final boolean permitted = roundTripVesselPermissionProvider.isPermittedOnResource(element, resource);
		if (!permitted) {
			return false;
		}
		// Not strictly correct - should be first load in cargo sequence;
		if (portType == PortType.Load) {
			final int endOfPromptPeriod = promptPeriodProvider.getEndOfPromptPeriod();
			// If timewindow start is in prompt, then we want to remove the cargo.
			final ITimeWindow timeWindow = portSlot.getTimeWindow();

			if (timeWindow != null && (timeWindow.getInclusiveStart() < endOfPromptPeriod)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String explain(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource) {
		return null;
	}
}

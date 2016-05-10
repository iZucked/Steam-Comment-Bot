/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
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
public class RoundTripVesselPermissionConstraintChecker implements IPairwiseConstraintChecker {

	@Inject
	@NonNull
	private IVesselProvider vesselProvider;

	@Inject
	@NonNull
	private IRoundTripVesselPermissionProvider roundTripVesselPermissionProvider;

	@Inject
	private IPortTypeProvider portTypeProvider;
	@NonNull
	private final String name;

	public RoundTripVesselPermissionConstraintChecker(@NonNull final String name) {
		super();
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

		for (ISequenceElement element : sequence) {

			if (!checkElement(element, resource)) {
				return false; // fail fast.
			}
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

		return checkElement(first, resource) && checkElement(second, resource);
	}

	protected boolean checkElement(final @NonNull ISequenceElement element, final @NonNull IResource resource) {
		if (portTypeProvider.getPortType(element) == PortType.Start) {
			return true;
		}
		if (portTypeProvider.getPortType(element) == PortType.End) {
			return true;
		}
		if (portTypeProvider.getPortType(element) == PortType.Round_Trip_Cargo_End) {
			return true;
		}
		return roundTripVesselPermissionProvider.isPermittedOnResource(element, resource);
	}

	@Override
	public String explain(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource) {
		return null;
	}
}

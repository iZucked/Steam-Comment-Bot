/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRoundTripVesselPermissionProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * 
 * @author Simon Goodall
 * 
 * @param
 */
public class RoundTripVesselPermissionConstraintChecker implements IPairwiseConstraintChecker {

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IRoundTripVesselPermissionProvider roundTripVesselPermissionProvider;

	@Inject
	private IPortTypeProvider portTypeProvider;

	@NonNull
	private final String name;

	public RoundTripVesselPermissionConstraintChecker(@NonNull final String name) {
		this.name = name;
	}

	@Override
	@NonNull
	public String getName() {
		return name;
	}

	public boolean checkSequence(@NonNull final ISequence sequence, @NonNull final IResource resource, final List<String> messages) {
		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		if (vesselAvailability.getVesselInstanceType() != VesselInstanceType.ROUND_TRIP) {
			return true;
		}

		ISequenceElement prevElement = null;
		for (final ISequenceElement element : sequence) {
			if (!checkElement(element, resource, messages)) {
				return false; // fail fast.
			}

			if (prevElement != null) {
				if (!roundTripVesselPermissionProvider.isBoundPair(prevElement, element)) {
					messages.add(String.format("%s: Sequence elements pair (%s, %s) is no a bound pair!", this.name, prevElement.getName(), element.getName()));
					return false;
				}
			}
			prevElement = element;
		}
		return true;
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources, final List<String> messages) {
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
				return false;
			}
		}

		return valid;
	}

	@Override
	public boolean checkPairwiseConstraint(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource, final List<String> messages) {

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		if (vesselAvailability.getVesselInstanceType() != VesselInstanceType.ROUND_TRIP) {
			return true;
		}

		final boolean valid = checkElement(first, resource, messages) && checkElement(second, resource, messages);
		if (valid) {
			final boolean result = roundTripVesselPermissionProvider.isBoundPair(first, second);
			if (!result)
				messages.add(String.format("%s: Sequence elements pair (%s, %s) is no a bound pair!", this.name, first.getName(), second.getName()));
			return result;
		}
		return false;
	}

	protected boolean checkElement(final @NonNull ISequenceElement element, final @NonNull IResource resource, final List<String> messages) {
		if (portTypeProvider.getPortType(element) == PortType.Start) {
			return true;
		}
		if (portTypeProvider.getPortType(element) == PortType.End) {
			return true;
		}
		if (portTypeProvider.getPortType(element) == PortType.Round_Trip_Cargo_End) {
			return true;
		}
		final boolean permitted = roundTripVesselPermissionProvider.isPermittedOnResource(element, resource);
		if (!permitted) {
			messages.add(String.format("%s: Element %s is not permitted on the resource %s!", this.name, element.getName(), resource.getName()));
			return false;
		}

		return true;
	}
}

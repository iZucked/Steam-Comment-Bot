/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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
import com.mmxlabs.optimiser.core.constraints.IResourceElementConstraintChecker;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IAllowedVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.INominatedVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class AllowedVesselPermissionConstraintChecker implements IPairwiseConstraintChecker, IResourceElementConstraintChecker {

	@Inject
	@NonNull
	private IVesselProvider vesselProvider;

	@Inject
	@NonNull
	private INominatedVesselProvider nominatedVesselProvider;

	@Inject
	@NonNull
	private IAllowedVesselProvider allowedVesselProvider;

	@Inject
	private IPortTypeProvider portTypeProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@NonNull
	private final String name;

	public AllowedVesselPermissionConstraintChecker(@NonNull final String name) {
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

		for (final ISequenceElement element : sequence) {

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
			final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
			// Ignore compatibility on non-shipped cargoes
			if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {
				continue;
			}
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

		return checkElement(first, resource) && checkElement(second, resource);
	}

	@Override
	public boolean checkElement(final @NonNull ISequenceElement element, final @NonNull IResource resource) {
		// Skip these element types
		if (portTypeProvider.getPortType(element) == PortType.Start) {
			return true;
		}
		if (portTypeProvider.getPortType(element) == PortType.End) {
			return true;
		}
		if (portTypeProvider.getPortType(element) == PortType.Round_Trip_Cargo_End) {
			return true;
		}

		final IPortSlot portSlot = portSlotProvider.getPortSlot(element);
		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);

		IVessel vessel = null;

		if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.FLEET || vesselAvailability.getVesselInstanceType() == VesselInstanceType.TIME_CHARTER) {
			vessel = vesselAvailability.getVessel();
		} else if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.SPOT_CHARTER || vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
			vessel = vesselAvailability.getVessel();
			// Vessel events should not be moved onto spot charters
			if (portTypeProvider.getPortType(element) == PortType.DryDock || portTypeProvider.getPortType(element) == PortType.CharterOut
					|| portTypeProvider.getPortType(element) == PortType.CharterOut) {
				return false;
			}
		} else if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {
			vessel = nominatedVesselProvider.getNominatedVessel(resource);
		}
		return allowedVesselProvider.isPermittedOnVessel(portSlot, vessel);
	}

	@Override
	public String explain(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource) {
		return null;
	}
}

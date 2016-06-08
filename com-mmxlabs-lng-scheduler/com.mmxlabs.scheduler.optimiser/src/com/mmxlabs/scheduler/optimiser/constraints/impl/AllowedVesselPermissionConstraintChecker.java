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
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IAllowedVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.INominatedVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class AllowedVesselPermissionConstraintChecker implements IPairwiseConstraintChecker {

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

	protected boolean checkElement(final @NonNull ISequenceElement element, final @NonNull IResource resource) {
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
		IVesselClass vesselClass = null;

		if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.FLEET || vesselAvailability.getVesselInstanceType() == VesselInstanceType.TIME_CHARTER) {
			vessel = vesselAvailability.getVessel();
			vesselClass = vessel.getVesselClass();
		} else if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.SPOT_CHARTER || vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
			// vessel = null; // Not a real vessel
			vesselClass = vesselAvailability.getVessel().getVesselClass();
		} else if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {

			vessel = nominatedVesselProvider.getNominatedVessel(resource);
			if (vessel != null) {
				vesselClass = vessel.getVesselClass();
			}
		}

		boolean allowedOnVessel = true;
		boolean allowedOnVesselClass = true;

		@Nullable
		final Collection<@NonNull IVessel> permittedVessels = allowedVesselProvider.getPermittedVessels(portSlot);
		if (permittedVessels != null) {
			if (vessel == null) {
				allowedOnVessel = false;
			} else if (!permittedVessels.contains(vessel)) {
				allowedOnVessel = false;
			}
		}

		@Nullable
		final Collection<@NonNull IVesselClass> permittedVesselClasses = allowedVesselProvider.getPermittedVesselClasses(portSlot);
		if (permittedVesselClasses != null) {
			if (vesselClass == null) {
				allowedOnVesselClass = false;
			} else if (!permittedVesselClasses.contains(vesselClass)) {
				allowedOnVesselClass = false;
			}
		}

		if (permittedVessels == null && permittedVesselClasses == null) {
			// No restrictions
			return true;
		} else if (permittedVessels != null && permittedVesselClasses == null) {
			return allowedOnVessel;
		} else if (permittedVessels == null && permittedVesselClasses != null) {
			return allowedOnVesselClass;
		} else {
			return allowedOnVessel || allowedOnVesselClass;
		}
	}

	@Override
	public String explain(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource) {
		return null;
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IAllowedVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.INominatedVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class AllowedVesselPermissionConstraintChecker implements IPairwiseConstraintChecker, IResourceElementConstraintChecker {

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private INominatedVesselProvider nominatedVesselProvider;

	@Inject
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

	public boolean checkSequence(@NonNull final ISequence sequence, @NonNull final IResource resource, final List<String> messages) {
		for (final ISequenceElement element : sequence) {
			if (!checkElement(element, resource, messages)) {
				return false; // fail fast.
			}
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
			final IVesselCharter vesselCharter = vesselProvider.getVesselCharter(resource);
			// Ignore compatibility on non-shipped cargoes
			if (vesselCharter.getVesselInstanceType().isNonShipped()) {
				continue;
			}
			final ISequence sequence = sequences.getSequence(resource);
			if (!checkSequence(sequence, resource, messages)) {
				return false;
			}
		}

		if (valid && messages != null) {
			messages.add(String.format("%s : all sequences satisfied the constraint!", this.name));
		}
		return valid;
	}

	@Override
	public boolean checkPairwiseConstraint(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource, final List<String> messages) {
		return checkElement(first, resource, messages) && checkElement(second, resource, messages);
	}

	@Override
	public boolean checkElement(final @NonNull ISequenceElement element, final @NonNull IResource resource, final List<String> messages) {
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
		final IVesselCharter vesselCharter = vesselProvider.getVesselCharter(resource);

		IVessel vessel = null;

		if (vesselCharter.getVesselInstanceType() == VesselInstanceType.FLEET || vesselCharter.getVesselInstanceType() == VesselInstanceType.TIME_CHARTER) {
			vessel = vesselCharter.getVessel();
		} else if (vesselCharter.getVesselInstanceType() == VesselInstanceType.SPOT_CHARTER || vesselCharter.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
			vessel = vesselCharter.getVessel();
			final PortType type = portTypeProvider.getPortType(element);
			// Vessel events should not be moved onto spot charters
			if (type == PortType.DryDock //
					|| type == PortType.Maintenance //
					|| type == PortType.CharterOut //
			) {
				if (messages != null)
					messages.add(String.format("%s : Vessel %s is a spot charter and should not have %s in the schedule!", this.name, vessel != null ? vessel.getName() : "unknown vessel", type.toString()));
				return false;
			}
		} else if (vesselCharter.getVesselInstanceType().isNonShipped()) {
			vessel = nominatedVesselProvider.getNominatedVessel(resource);
		}
		final boolean result = allowedVesselProvider.isPermittedOnVessel(portSlot, vessel);
		if (!result && messages != null) {
			messages.add(String.format("%s: Slot %s is not allowed on vessel %s!", this.name, portSlot.getId(), vessel != null ? vessel.getName() : "unknown vessel"));
		}
		return result;
	}
}

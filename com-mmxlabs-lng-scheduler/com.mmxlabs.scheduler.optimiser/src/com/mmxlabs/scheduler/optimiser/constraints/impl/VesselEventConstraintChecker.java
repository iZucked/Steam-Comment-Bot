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
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * Constraint checker to ensure Vessel events are on FLEET or TIME_CHARTER vessel types.
 */
public class VesselEventConstraintChecker implements IPairwiseConstraintChecker {

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IPortTypeProvider portTypeProvider;

	@NonNull
	private final String name;

	public VesselEventConstraintChecker(@NonNull final String name) {
		super();
		this.name = name;
	}

	@Override
	@NonNull
	public String getName() {
		return name;
	}

	public boolean checkSequence(@NonNull final ISequence sequence, @NonNull final IResource resource, final List<String> messages) {
		final IVesselCharter vesselCharter = vesselProvider.getVesselCharter(resource);
		if (vesselCharter.getVesselInstanceType() != VesselInstanceType.ROUND_TRIP) {
			return true;
		}

		for (ISequenceElement element : sequence) {

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

		for (final IResource resource : loopResources) {
			final ISequence sequence = sequences.getSequence(resource);
			if (!checkSequence(sequence, resource, messages)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean checkPairwiseConstraint(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource, final List<String> messages) {
		return checkElement(first, resource, messages) && checkElement(second, resource, messages);
	}

	protected boolean checkElement(final @NonNull ISequenceElement element, final @NonNull IResource resource, final List<String> messages) {
		if (portTypeProvider.getPortType(element) == PortType.DryDock || portTypeProvider.getPortType(element) == PortType.Maintenance
				|| portTypeProvider.getPortType(element) == PortType.CharterOut) {

			final IVesselCharter vesselCharter = vesselProvider.getVesselCharter(resource);
			final boolean result = (vesselCharter.getVesselInstanceType() == VesselInstanceType.FLEET || vesselCharter.getVesselInstanceType() == VesselInstanceType.TIME_CHARTER);
			if (!result && messages != null)
				messages.add(String.format("%s : For element %s vessel availability is fleet or time charter!", this.name, element.getName()));
			return result;
		}
		return true;
	}
}

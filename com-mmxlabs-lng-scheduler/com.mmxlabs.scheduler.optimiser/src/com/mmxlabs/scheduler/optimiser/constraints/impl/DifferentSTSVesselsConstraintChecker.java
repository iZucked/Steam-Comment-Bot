/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IShipToShipBindingProvider;

/**
 * Constraint checker to ensure that a pair of STS slots are on different routes.
 * 
 * @author Simon Goodall
 */
public class DifferentSTSVesselsConstraintChecker implements IConstraintChecker {

	@NonNull
	private final String name;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IShipToShipBindingProvider shipBindingProvider;

	public DifferentSTSVesselsConstraintChecker(@NonNull final String name) {
		this.name = name;
	}

	@Override
	@NonNull
	public String getName() {
		return name;
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources, @NonNull final List<@NonNull String> messages) {

		final Collection<@NonNull IResource> loopResources;
		if (changedResources == null) {
			loopResources = sequences.getResources();
		} else {
			loopResources = changedResources;
		}

		for (final IResource resource : loopResources) {
			final ISequence sequence = sequences.getSequence(resource);

			// TODO: Skip special routes

			final Set<@NonNull IPortSlot> sequenceElements = new HashSet<>();

			// Loop over all sequence elements
			for (final ISequenceElement element : sequence) {

				// Get the port slot
				final IPortSlot portSlot = portSlotProvider.getPortSlot(element);

				// Record it
				sequenceElements.add(portSlot);

				// Get the STS if present
				final IPortSlot converseTransferElement = shipBindingProvider.getConverseTransferElement(portSlot);

				// If the STS is already in the route, then this is invalid - and it will cause the EnumeratingSequenceScheduler to fall over.
				// Note - this check will be triggered on the second helf of the STS binding in the route
				if (converseTransferElement != null && sequenceElements.contains(converseTransferElement)) {
					final String message = 
							String.format("%s: Sequence element %s the STS slot %s is already in the route!", this.name, element.getName(), converseTransferElement.getId());
					messages.add(message);
					return false;
				}
			}

		}

		final String message = String.format("%s: %s", this.name, "All STS slots are on different routes");
		messages.add(message);
		
		return true;
	}
}

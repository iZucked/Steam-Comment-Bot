/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IShipToShipBindingProvider;

/**
 * Constraint checker to ensure that a pair of STS slots are on different routes.
 * 
 * @author Simon Goodall
 */
public class DifferentSTSVesselsConstraintChecker implements IConstraintChecker {

	private final String name;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IShipToShipBindingProvider shipBindingProvider;

	public DifferentSTSVesselsConstraintChecker(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean checkConstraints(final ISequences sequences) {
		return checkConstraints(sequences, null);
	}

	@Override
	public boolean checkConstraints(final ISequences sequences, final List<String> messages) {

		for (final IResource resource : sequences.getResources()) {
			final ISequence sequence = sequences.getSequence(resource);

			// TODO: Skip special routes
			
			final Set<IPortSlot> sequenceElements = new HashSet<IPortSlot>();

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
					return false;
				}
			}

		}

		return true;
	}

	@Override
	public void setOptimisationData(final IOptimisationData optimisationData) {

	}

}

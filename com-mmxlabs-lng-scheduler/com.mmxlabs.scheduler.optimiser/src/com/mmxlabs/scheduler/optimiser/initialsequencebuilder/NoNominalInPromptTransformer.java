/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.initialsequencebuilder;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPromptPeriodProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class NoNominalInPromptTransformer {

	@Inject
	private IPromptPeriodProvider promptPeriodProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	public @NonNull ISequences run(@NonNull final ISequences rawSequences) {

		final IModifiableSequences newSequences = new ModifiableSequences(rawSequences);
		final int endOfPromptPeriod = promptPeriodProvider.getEndOfPromptPeriod();

		for (final IResource resource : newSequences.getResources()) {
			final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);

			// Only look at nominal resources
			if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {

				// List of elements (cargoes) to remove
				final List<@NonNull ISequenceElement> elementsToRemove = new LinkedList<>();

				// Loop over list of element identifying cargo strips (TODO: Yet another place we do this)
				boolean isCargo = false; // Currently stepping over a sequence of cargo elements
				boolean foundDischarge = false; // Have we found a discharge yet? (Supporting LLD cargoes).
				boolean isRemoving = false; // Are we removing these elements?
				for (final ISequenceElement e : newSequences.getSequence(resource)) {
					final IPortSlot portSlot = portSlotProvider.getPortSlot(e);
					final PortType portType = portSlot.getPortType();

					// Examine element for where we are in the cargo sequence - start or within
					boolean startOfSequence = false;
					if (portType == PortType.Load) {
						if (!isCargo) {
							startOfSequence = true;
						} else if (isCargo && foundDischarge) {
							// Start of a new cargo.
							startOfSequence = true;
						}
						isCargo = true;
					} else if (portType == PortType.Discharge) {
						foundDischarge = true;
					} else {
						// ?
						startOfSequence = true;
						isCargo = false;
					}

					// Is this the first element in the cargo? Reset flags and check whether or not it is a prompt cargo.
					if (startOfSequence) {
						foundDischarge = false;
						isRemoving = false;
						if (isCargo) {
							// If timewindow start is in prompt, then we want to remove the cargo.
							final ITimeWindow timeWindow = portSlot.getTimeWindow();
							if (timeWindow != null && (timeWindow.getInclusiveStart() < endOfPromptPeriod)) {
								isRemoving = true;
							}
						}
					}

					// Add element to list to remove if needed
					if (isRemoving) {
						elementsToRemove.add(e);
					}
				}

				// Now move cargoes elements to the unused list.
				if (!elementsToRemove.isEmpty()) {
					@NonNull
					final IModifiableSequence modifiableSequence = newSequences.getModifiableSequence(resource);
					for (final ISequenceElement e : elementsToRemove) {
						modifiableSequence.remove(e);
						newSequences.getModifiableUnusedElements().add(e);
					}
				}
			}

		}
		return newSequences;
	}
}

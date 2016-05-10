/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.manipulators;

import java.util.Collections;

import javax.inject.Inject;

import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISegment;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.impl.DisconnectedSegment;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IShortCargoReturnElementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * A {@link ISequencesManipulator} to insert return elements into a sequence of Load and Discharge slots on {@link VesselInstanceType#CARGO_SHORTS} sequences.
 * 
 */
public class ShortCargoSequenceManipulator implements ISequencesManipulator {

	@Inject
	private IPortTypeProvider portTypeProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IShortCargoReturnElementProvider shortCargoReturnElementProvider;

	@Override
	public void manipulate(final IModifiableSequences sequences) {

		for (final IResource resource : sequences.getResources()) {
			final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
			if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
				final IModifiableSequence seq = sequences.getModifiableSequence(resource);
				final int size = seq.size();
				// Loop backwards to avoid needing to update index by inserted item count
				for (int i = size - 1; i >= 0; --i) {
					final ISequenceElement element = seq.get(i);
					final PortType portType = portTypeProvider.getPortType(element);
					if (portType == PortType.Load) {
						final ISequenceElement returnElement = shortCargoReturnElementProvider.getReturnElement(element);

						// If element is null we expect that a constraint checker will mark the solution as invalid.... (if not, then expect some sort of failure in the PortTimesPlanner/VoyagePlanner
						// classes)
						// The sequence manipulator API does not have a way to mark solution failed.
						// If it is null, then we have tried to put a load on a nominal cargo vessel that was not expected to be placed on a nominal vessel.
						if (returnElement != null) {

							final ISegment segment = new DisconnectedSegment(Collections.singletonList(returnElement));
							if (i + 2 <= seq.size()) {
								seq.insert(i + 2, segment);
							} else {
								seq.add(returnElement);
							}
						}
					}
				}
			}
		}
	}
}

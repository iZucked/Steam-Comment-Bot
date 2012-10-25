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
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IShortCargoReturnElementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * A {@link ISequencesManipulator} to insert return elements into a sequence of Load and Discharge slots on {@link VesselInstanceType#CARGO_SHORTS} sequences.
 * 
 * @since 2.0
 */
public class ShortCargoSequenceManipulator implements ISequencesManipulator {

	@Inject
	private IPortTypeProvider portTypeProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IShortCargoReturnElementProvider shortCargoReturnElementProvider;

	@Override
	public void init(final IOptimisationData data) {

	}

	@Override
	public void manipulate(final IModifiableSequences sequences) {

		for (final IResource resource : sequences.getResources()) {
			final IVessel vessel = vesselProvider.getVessel(resource);
			if (vessel.getVesselInstanceType() == VesselInstanceType.CARGO_SHORTS) {
				final IModifiableSequence seq = sequences.getModifiableSequence(resource);
				final int size = seq.size();
				// Loop backwards to avoid needing to update index by inserted item count
				for (int i = size - 1; i >= 0; --i) {
					final ISequenceElement element = seq.get(i);
					final PortType portType = portTypeProvider.getPortType(element);
					if (portType == PortType.Load) {
						final ISequenceElement returnElement = shortCargoReturnElementProvider.getReturnElement(element);
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

	@Override
	public void dispose() {
		portTypeProvider = null;
		vesselProvider = null;
		shortCargoReturnElementProvider = null;
	}

}

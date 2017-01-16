/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.initialsequencebuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * A simple {@link IInitialSequenceBuilder} which just creates a random schedule for its input.
 * 
 * @author hinton
 * 
 */
public class RandomInitialSequenceBuilder implements IInitialSequenceBuilder {

	@Inject
	private IPortSlotProvider slotProvider;

	@Inject
	private IStartEndRequirementProvider startEndProvider;

	@Inject
	private IPortTypeProvider portTypeProvider;

	@Override
	public ISequences createInitialSequences(final IOptimisationData data, final ISequences suggestion, final Map<ISequenceElement, IResource> resourceSuggestion,
			final Map<ISequenceElement, ISequenceElement> pairingHints) {

		final List<ISequenceElement> sequenceElements = new ArrayList<ISequenceElement>(data.getSequenceElements());

		// sort sequence elements by their load time
		Collections.sort(sequenceElements, new Comparator<ISequenceElement>() {
			@Override
			public int compare(final ISequenceElement o1, final ISequenceElement o2) {
				final int s1 = slotProvider.getPortSlot(o1).getTimeWindow().getInclusiveStart();
				final int s2 = slotProvider.getPortSlot(o2).getTimeWindow().getInclusiveStart();
				if (s1 < s2) {
					return -1;
				} else if (s1 > s2) {
					return 1;
				}
				return 0;
			}
		});

		final List<IResource> resources = data.getResources();

		final IModifiableSequences sequences = new ModifiableSequences(resources);

		// Add each sequence element to a sequence; at the moment just rotate
		// through the sequences
		// one at a time. Perhaps it would be better to randomise, or do
		// something even more fancy.
		int seq = 0;
		for (final ISequenceElement elt : sequenceElements) {
			// Skip start and end points
			final PortType portType = portTypeProvider.getPortType(elt);
			if (portType.equals(PortType.Start) || portType.equals(PortType.End)) {
				continue;
			}
			// add stuff to sequences in rotating order
			// TODO consider time windows
			sequences.getModifiableSequence(seq).add(elt);
			seq = (seq + 1) % sequences.size();
		}

		for (final IResource resource : resources) {
			final IModifiableSequence sequence = sequences.getModifiableSequence(resource);

			final ISequenceElement startLocation = startEndProvider.getStartElement(resource);
			final ISequenceElement endLocation = startEndProvider.getEndElement(resource);

			sequence.insert(0, startLocation);
			sequence.add(endLocation);
		}

		return sequences;
	}

}

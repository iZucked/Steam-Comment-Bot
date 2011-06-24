/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.initialsequencebuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * A simple {@link IInitialSequenceBuilder} which just creates a random schedule
 * for its input.
 * 
 * @author hinton
 * 
 */
public class RandomInitialSequenceBuilder<T> implements
		IInitialSequenceBuilder<T> {
	private Random random;

	public RandomInitialSequenceBuilder(Random random) {
		this.random = random;
	}

	public RandomInitialSequenceBuilder() {
		this(new Random(1));
	}

	@Override
	public ISequences<T> createInitialSequences(
			final IOptimisationData<T> data, final ISequences<T> suggestion,
			final Map<T, IResource> resourceSuggestion) {

		List<T> sequenceElements = new ArrayList<T>(data.getSequenceElements());

		// TODO fix type parameterisation here, or disable warning
		final IPortSlotProvider<T> slotProvider = data
				.getDataComponentProvider(
						SchedulerConstants.DCP_portSlotsProvider,
						IPortSlotProvider.class);

		// sort sequence elements by their load time
		Collections.sort(sequenceElements, new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				final int s1 = slotProvider.getPortSlot(o1).getTimeWindow()
						.getStart();
				final int s2 = slotProvider.getPortSlot(o2).getTimeWindow()
						.getStart();
				if (s1 < s2)
					return -1;
				else if (s1 > s2)
					return 1;
				return 0;
			}
		});

		final List<IResource> resources = data.getResources();

		final IModifiableSequences<T> sequences = new ModifiableSequences<T>(
				resources);

		IPortTypeProvider<T> portTypeProvider = data.getDataComponentProvider(
				SchedulerConstants.DCP_portTypeProvider,
				IPortTypeProvider.class);

		// Add each sequence element to a sequence; at the moment just rotate
		// through the sequences
		// one at a time. Perhaps it would be better to randomise, or do
		// something even more fancy.
		int seq = 0;
		for (T elt : sequenceElements) {
			// Skip start and end points
			final PortType portType = portTypeProvider.getPortType(elt);
			if (portType.equals(portType.Start)
					|| portType.equals(portType.End))
				continue;
			// add stuff to sequences in rotating order
			// TODO consider time windows
			sequences.getModifiableSequence(seq).add(elt);
			seq = (seq + 1) % sequences.size();
		}

		// Handle start/end requirements.
		IStartEndRequirementProvider<T> startEndProvider = data
				.getDataComponentProvider(
						SchedulerConstants.DCP_startEndRequirementProvider,
						IStartEndRequirementProvider.class);

		for (IResource resource : resources) {
			final IModifiableSequence<T> sequence = sequences
					.getModifiableSequence(resource);

			T startLocation = startEndProvider.getStartElement(resource);
			T endLocation = startEndProvider.getEndElement(resource);

			sequence.insert(0, startLocation);
			sequence.add(endLocation);
		}

		return sequences;
	}

}

package com.mmxlabs.scheduler.optimiser.initialschedulebuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;

/**
 * A simple {@link IInitialScheduleBuilder} which just creates a random schedule for its input.
 * @author hinton
 *
 */
public class RandomInitialScheduleBuilder implements IInitialScheduleBuilder<ISequenceElement> {
	private Random random;
	public RandomInitialScheduleBuilder(Random random) {
		this.random = random;
	}
	@Override
	public ISequences<ISequenceElement> createInitialSequences(IOptimisationData<ISequenceElement> data) {
		
		List<ISequenceElement> sequenceElements = new ArrayList<ISequenceElement>(data.getSequenceElements());
		
		//sort sequence elements by their load time
		Collections.sort(sequenceElements,
				new Comparator<ISequenceElement>() 
		{
			@Override
			public int compare(ISequenceElement o1, ISequenceElement o2) {
				final int s1 = o1.getPortSlot().getTimeWindow().getStart();
				final int s2 = o2.getPortSlot().getTimeWindow().getStart();
				if (s1 < s2) return -1;
				else if (s1 > s2) return 1;
				return 0;
			}
		}
		);
		
		final List<IResource> resources = data.getResources();
		
		final IModifiableSequences<ISequenceElement> sequences = new ModifiableSequences<ISequenceElement>(resources);
		
		//Deal with start/end ports somehow. This should be specified in the scenario for each resource?
		
		//Add each sequence element to a sequence; at the moment just rotate through the sequences
		//one at a time. Perhaps it would be better to randomise, or do something even more fancy.
		int seq = 0;
		for (ISequenceElement elt : sequenceElements) {
			sequences.getModifiableSequence(seq).add(elt);
			seq = (seq + 1) % sequences.size();
		}
		
		return sequences;
	}

}

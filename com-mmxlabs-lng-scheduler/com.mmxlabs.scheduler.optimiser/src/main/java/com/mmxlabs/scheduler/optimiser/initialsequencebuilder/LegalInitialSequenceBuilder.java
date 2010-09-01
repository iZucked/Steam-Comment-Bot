package com.mmxlabs.scheduler.optimiser.initialsequencebuilder;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.lso.LegalSequencingChecker;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * Construct some initial sequences using a {@link LegalSequencingChecker} to make sure
 * that all the elements are sequenced in a manner that is in-principle feasible.
 * @author hinton
 *
 * @param <T>
 */
public class LegalInitialSequenceBuilder<T> implements
		IInitialSequenceBuilder<T> {

	@Override
	public ISequences<T> createInitialSequences(IOptimisationData<T> data) {
		LegalSequencingChecker<T> checker = new LegalSequencingChecker<T>(data);
		
		/*
		 * What I should do here is build a directed graph here and then run some covering algorithm
		 * but instead I'm going to do something grim with loops 
		 */
		final List<IResource> resources = data.getResources();
		IModifiableSequences<T> result = new ModifiableSequences<T>(resources);
		
		/*
		 * First, add all the start elements.
		 */
		
		@SuppressWarnings("unchecked")
		IStartEndRequirementProvider<T> serp = data.getDataComponentProvider(SchedulerConstants.DCP_startEndRequirementProvider, IStartEndRequirementProvider.class);
		
		for (IResource r : resources) {
			result.getModifiableSequence(r).add(serp.getStartElement(r));
		}
		
		/*
		 * Now the horrible; might be worth doing some caching here.
		 */
		@SuppressWarnings("unchecked")
		IPortTypeProvider<T> ptp = data.getDataComponentProvider(SchedulerConstants.DCP_portTypeProvider, IPortTypeProvider.class);
		Set<T> elementsToProcess = new HashSet<T>();
		for (T element : data.getSequenceElements()) {
			if (ptp.getPortType(element).equals(PortType.Start)) continue;
			elementsToProcess.add(element);
		}
		
		int currentSize = Integer.MAX_VALUE;
		while (elementsToProcess.isEmpty() == false) {
			if (currentSize == elementsToProcess.size()) {
				return null; // we have reached a fixed point, and the constraints are too hard.
							// perhaps we should do something else here instead of nulling
			} else {
				currentSize = elementsToProcess.size();
			}
			Iterator<T> iter = elementsToProcess.iterator();
			while (iter.hasNext()) {
				final T here = iter.next();
				top:
				for (int i = 0; i<result.size(); i++) {
					final IModifiableSequence<T> seq = result.getModifiableSequence(i);
					for (int j = 0; j<seq.size(); j++) {
						if (checker.allowSequence(seq.get(j), here)) {
							//sequence this
							seq.insert(j+1, here);
							iter.remove();
							break top;
						}
					}
					
				}
			}
			
		}
		
		return result;
	}

}

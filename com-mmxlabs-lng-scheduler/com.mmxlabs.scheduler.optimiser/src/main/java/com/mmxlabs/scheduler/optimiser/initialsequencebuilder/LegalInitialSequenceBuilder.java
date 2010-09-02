package com.mmxlabs.scheduler.optimiser.initialsequencebuilder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.mmxlabs.optimiser.common.dcproviders.IResourceAllocationConstraintDataComponentProvider;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.events.IPortVisitEvent;
import com.mmxlabs.scheduler.optimiser.lso.LegalSequencingChecker;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * Construct some initial sequences using a {@link LegalSequencingChecker} to make sure
 * that all the elements are sequenced in a manner that is in-principle feasible.
 * 
 * The approach in this class cannot work well; something else needs to be done that's better.
 * 
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
			result.getModifiableSequence(r).add(serp.getEndElement(r));
		}
		
		/*
		 * Now the horrible; might be worth doing some caching here.
		 */
		@SuppressWarnings("unchecked")
		IPortTypeProvider<T> ptp = data.getDataComponentProvider(SchedulerConstants.DCP_portTypeProvider, IPortTypeProvider.class);
		Set<T> elementsToProcess = new HashSet<T>();
		for (T element : data.getSequenceElements()) {
			if (ptp.getPortType(element).equals(PortType.Start) 
					|| ptp.getPortType(element).equals(PortType.End)
					) continue;
			elementsToProcess.add(element);
		}
		
		final IResourceAllocationConstraintDataComponentProvider allocationProvider = 
			data.getDataComponentProvider(SchedulerConstants.DCP_resourceAllocationProvider,
					IResourceAllocationConstraintDataComponentProvider.class);
		
		final List<IResource> allResources = data.getResources();
		
		int currentSize = Integer.MAX_VALUE;
		while (elementsToProcess.isEmpty() == false) {
			if (currentSize == elementsToProcess.size()) {
				break;
			} else {
				currentSize = elementsToProcess.size();
			}
			Iterator<T> iter = elementsToProcess.iterator();
			while (iter.hasNext()) {
				final T here = iter.next();
				Collection<IResource> validResources = 
					allocationProvider.getAllowedResources(here);
				
				if (validResources == null) {
					validResources = allResources;
				}
				
				top:
				for (IResource resource : validResources) {
					final IModifiableSequence<T> seq = result.getModifiableSequence(resource);
					for (int j = 0; j<seq.size()-1; j++) {
						final T elementj = seq.get(j);
						
						//ignore final element, because otherwise we have problems
						//TODO this is wrong when end times are meaningful :(
						final T elementAfterElementj = 
							(j < seq.size() - 2) ? seq.get(j+1) : null;
						
						if (checker.allowSequence(elementj, here)) {
							if (elementAfterElementj == null || checker.allowSequence(here, elementAfterElementj)) {
								seq.insert(j+1, here);
								iter.remove();
								break top;
							}
						}
					}
				}
			}
		}
		
		if (elementsToProcess.size() != 0) {
			System.err.println("Unable to schedule all elements! " + elementsToProcess.size() + " of " + data.getSequenceElements().size() + " remain!");
			IPortSlotProvider<T> slotProvider = data.getDataComponentProvider(SchedulerConstants.DCP_portSlotsProvider, IPortSlotProvider.class);
			for (T t : elementsToProcess) {
				System.err.print( slotProvider.getPortSlot(t) + " " );
			}
			System.err.println();
			return result;
		}
		
		return result;
	}

}

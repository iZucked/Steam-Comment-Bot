package com.mmxlabs.scheduler.optimiser.initialsequencebuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.lso.LegalSequencingChecker;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * A pairwise constraint respecting initial sequence builder. This sorts elements by their earliest arrival time
 * and then constructs a sequence for each resource by picking the earliest element which can be appended
 * to the route without violating the consequent pairwise constraint.
 * 
 * @author hinton
 *
 * @param <T>
 */
public class ConstrainedInitialSequenceBuilder<T> implements
		IInitialSequenceBuilder<T> {
	private List<IPairwiseConstraintChecker<T>> pairwiseCheckers;

	public ConstrainedInitialSequenceBuilder(Collection<IConstraintCheckerFactory> factories) {
		this.pairwiseCheckers = new ArrayList<IPairwiseConstraintChecker<T>>();
		for (IConstraintCheckerFactory factory : factories) {
			IConstraintChecker<T> checker = factory.instantiate();
			if (checker instanceof IPairwiseConstraintChecker) {
				pairwiseCheckers.add((IPairwiseConstraintChecker<T>) checker);
			}
		}
	}
	
	public ConstrainedInitialSequenceBuilder(List<IPairwiseConstraintChecker<T>> pairwiseCheckers) {
		this.pairwiseCheckers = pairwiseCheckers;
	}
	
	@Override
	public ISequences<T> createInitialSequences(IOptimisationData<T> data) {	
		@SuppressWarnings("unchecked")
		final IPortTypeProvider<T> portTypeProvider = data.getDataComponentProvider(
				SchedulerConstants.DCP_portTypeProvider, IPortTypeProvider.class);
		
		@SuppressWarnings("unchecked")
		final IPortSlotProvider<T> portSlotProvider = data.getDataComponentProvider(
				SchedulerConstants.DCP_portSlotsProvider, IPortSlotProvider.class);
		
		@SuppressWarnings("unchecked")
		final IStartEndRequirementProvider<T> startEndRequirementProvider = data.getDataComponentProvider(
				SchedulerConstants.DCP_startEndRequirementProvider, IStartEndRequirementProvider.class);
		
		LegalSequencingChecker<T> checker = new LegalSequencingChecker<T>(data, pairwiseCheckers);
		final Comparator<T> comparator = new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				final IPortSlot slot1 = portSlotProvider.getPortSlot(o1);
				final IPortSlot slot2 = portSlotProvider.getPortSlot(o2);
				//sort by start time
				final int s1 = slot1.getTimeWindow().getStart();
				final int s2 = slot2.getTimeWindow().getStart();
				
				if (s1 < s2) {
					return -1;
				} else if (s1 > s2) {
					return 1;
				} else {
					return 0;
				}
			}
		};
		LinkedList<T> orderedElements = new LinkedList<T>();
		
		for (T element : data.getSequenceElements()) {
			if (portTypeProvider.getPortType(element).equals(PortType.Start)) continue;
			orderedElements.add(element);
		}
		
		Collections.sort(orderedElements, comparator);
		
		//now construct routes
		
		//First sort resources to prefer fleet vessels
		//Fortunately the enum is already in order of goodness, barring unknown type vessels
		List<IResource> resources = new ArrayList<IResource>(data.getResources());
		{
			final IVesselProvider vesselProvider = data.getDataComponentProvider(SchedulerConstants.DCP_vesselProvider, IVesselProvider.class);
			Collections.sort(resources, new Comparator<IResource>(){
				@Override
				public int compare(IResource o1, IResource o2) {
					final VesselInstanceType vit1 = vesselProvider.getVessel(o1).getVesselInstanceType();
					final VesselInstanceType vit2 = vesselProvider.getVessel(o2).getVesselInstanceType();
					
					if (vit1.ordinal() < vit2.ordinal()) {
						return -1;
					} else if (vit1.ordinal() > vit2.ordinal()) {
						return 1;
					} else {
						return 0;
					}
				}});
		}
		ModifiableSequences<T> result = new ModifiableSequences<T>(resources);
		
		for (final IResource resource : resources) {
			final IModifiableSequence<T> sequence = result.getModifiableSequence(resource);
			
			//add start element
			T currentElement = startEndRequirementProvider.getStartElement(resource);
			sequence.add(currentElement);
			
			//pass through orderedElements and add to sequences
			Iterator<T> iterator = orderedElements.iterator();
			while (iterator.hasNext()) {
				T here = iterator.next();
				//check whether here can go on this resource
//				Collection<IResource> allocation = resourceAllocationProvider.getAllowedResources(here);
//				if (allocation == null || allocation.contains(resource)) {
					if (checker.allowSequence(currentElement, here, resource)) {
						//sequence this element
						sequence.add(here);
						currentElement = here;
						iterator.remove(); //remove from queue
					}
//				}
			}
		}
		
		if (orderedElements.isEmpty() == false) {
			System.err.println("Warning: "  + orderedElements.size() + " elements remain unscheduled - this scenario may not be feasible.");
			for (T t : orderedElements) {
				System.err.println("Did not schedule: " + portSlotProvider.getPortSlot(t));
			}
			
			return result;
		}
		
		return result;
	}

}

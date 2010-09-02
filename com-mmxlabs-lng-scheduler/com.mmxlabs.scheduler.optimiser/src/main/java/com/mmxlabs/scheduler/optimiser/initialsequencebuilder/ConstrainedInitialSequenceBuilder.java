package com.mmxlabs.scheduler.optimiser.initialsequencebuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.mmxlabs.optimiser.common.dcproviders.IResourceAllocationConstraintDataComponentProvider;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.lso.LegalSequencingChecker;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * A second constraint-respecting initial sequence builder,
 * which should work better; it constructs the graph of valid following relationships, picks a resource,
 * and then chooses the longest path through that graph which connects its start and end elements
 * 
 * is this hard to do?
 * 
 * @author hinton
 *
 * @param <T>
 */
public class ConstrainedInitialSequenceBuilder<T> implements
		IInitialSequenceBuilder<T> {

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
		
		@SuppressWarnings("unchecked")
		final IResourceAllocationConstraintDataComponentProvider resourceAllocationProvider = data.getDataComponentProvider(
				SchedulerConstants.DCP_resourceAllocationProvider, IResourceAllocationConstraintDataComponentProvider.class);
		
		LegalSequencingChecker<T> checker = new LegalSequencingChecker<T>(data);
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
			System.err.println("Scheduling " + portSlotProvider.getPortSlot(element));
		}
		
		System.err.println("Sorting...");
		Collections.sort(orderedElements, comparator);
		
		//now construct routes
		final List<IResource> resources = data.getResources();
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
				Collection<IResource> allocation = resourceAllocationProvider.getAllowedResources(here);
				if (allocation == null || allocation.contains(resource)) {
					if (checker.allowSequence(currentElement, here)) {
						//sequence this element
						sequence.add(here);
						currentElement = here;
						System.err.println("Scheduled " + portSlotProvider.getPortSlot(here));
						iterator.remove(); //remove from queue
					}
				}
			}
			
			if (sequence.size() == 1) {
				System.err.println("That's weird, I couldn't schedule anything on " + resource.getName() + ". Let's have a look:");
				iterator = orderedElements.iterator();
				while (iterator.hasNext()) {
					T here = iterator.next();
					//check whether here can go on this resource
					Collection<IResource> allocation = resourceAllocationProvider.getAllowedResources(here);
					System.err.println("Allocation = " + allocation);
					if (allocation != null) {
						System.err.println("Contains resource: " + allocation.contains(resource));
					}
					if (allocation == null || allocation.contains(resource)) {
						if (checker.allowSequence(currentElement, here)) {
							//sequence this element
							sequence.add(here);
							currentElement = here;
							System.err.println("Scheduled " + portSlotProvider.getPortSlot(here));
							iterator.remove(); //remove from queue
						} else {
							System.err.println("Cannot schedule " + portSlotProvider.getPortSlot(here) + " after " + portSlotProvider.getPortSlot(currentElement));
							System.err.println(checker.allowPortTypes(portTypeProvider.getPortType(currentElement), portTypeProvider.getPortType(here)));
							System.err.println(checker.reachableFrom(currentElement, here, resource));
							System.err.println(checker.canFollow(currentElement, here));
						}
					}
				}
			}
		}
		
		if (orderedElements.isEmpty() == false) {
			System.err.println("Error: "  + orderedElements.size() + " elements not scheduled anywhere!");
			
			
			for (T t : orderedElements) {
				System.err.println("Unscheduled: " + portSlotProvider.getPortSlot(t));
			}
			
			return result;
		}
		
		return result;
	}

}

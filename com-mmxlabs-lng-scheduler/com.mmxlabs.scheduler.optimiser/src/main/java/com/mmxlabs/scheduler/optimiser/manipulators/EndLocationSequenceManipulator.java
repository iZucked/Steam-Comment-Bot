/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.manipulators;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IReturnElementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * The {@link EndLocationSequenceManipulator} replaces the end location with another location in two possible ways; either the vessel's end location is adjust to be the same as its first load port, or
 * the same as its last load port. These two cases are intended for spot and fleet vessels respectively.
 * 
 * This class uses an {@link IReturnElementProvider} to get a unique new sequence element for each vessel / port combination. The end element is then swapped out for an appropriate one of these
 * elements.
 * 
 * @author Simon Goodall, significantly modified by Tom Hinton
 * 
 */
public final class EndLocationSequenceManipulator implements ISequencesManipulator {
	/**
	 * An enum of the different end location rules that can be applied.
	 * 
	 * @author Simon Goodall
	 * 
	 */
	public static enum EndLocationRule {
		/**
		 * No special rules to apply.
		 */
		NONE,

		/**
		 * Return to the first load port.
		 */
		RETURN_TO_FIRST_LOAD,

		/**
		 * Return to the last load port visited.
		 */
		RETURN_TO_LAST_LOAD, RETURN_TO_CLOSEST_IN_SET,
	}

	private IPortTypeProvider portTypeProvider;

	private IPortProvider portProvider;

	private final Map<IResource, EndLocationRule> ruleMap = new HashMap<IResource, EndLocationSequenceManipulator.EndLocationRule>();

	private IReturnElementProvider returnElementProvider;

	private IStartEndRequirementProvider startEndRequirementProvider;

	private IMultiMatrixProvider<IPort, Integer> distanceProvider;

	public EndLocationSequenceManipulator() {

	}

	@Override
	public void manipulate(final IModifiableSequences sequences) {
		assert portTypeProvider != null;

		// Loop through each sequence in turn and manipulate
		for (final Map.Entry<IResource, IModifiableSequence> entry : sequences.getModifiableSequences().entrySet()) {
			manipulate(entry.getKey(), entry.getValue());
		}
	}

	public void manipulate(final IResource resource, final IModifiableSequence sequence) {

		final EndLocationRule rule = ruleMap.get(resource);
		if (rule == null) {
			return;
		}
		switch (rule) {
		case RETURN_TO_FIRST_LOAD:
			adjustLastElement(resource, sequence, getFirstLoadElement(sequence));
			break;
		case RETURN_TO_LAST_LOAD:
			adjustLastElement(resource, sequence, getLastLoadElement(sequence));
			break;
		case RETURN_TO_CLOSEST_IN_SET:
			returnToClosestInSet(resource, sequence);
			break;
		}
	}

	/**
	 * Find and return the last element in the sequence with {@link PortType} PortType.Load
	 * 
	 * @param sequence
	 */
	public ISequenceElement getLastLoadElement(final IModifiableSequence sequence) {
		/*
		 * Search for the last load port
		 */
		ISequenceElement t = null;
		for (final ISequenceElement element : sequence) {
			final PortType type = portTypeProvider.getPortType(element);
			// Record last load port
			if (type == PortType.Load) {
				t = element;
			}
		}
		return t;
		// adjustLastElement(resource, sequence, t);
	}

	/**
	 * Swap the dummy element in for the given sequence, and set its location to the given port.
	 * 
	 * @param resource
	 * 
	 * @param sequence
	 * @param location
	 */
	private final void returnToClosestInSet(final IResource resource, final IModifiableSequence sequence) {
		IStartEndRequirement endRequirement = startEndRequirementProvider.getEndRequirement(resource);

		ISequenceElement lastVisit = sequence.get(sequence.size() - 2);
		IPort fromPort = portProvider.getPortForElement(lastVisit);

		IPort closestPort = null;
		int closestPortDistance = Integer.MAX_VALUE;
		for (IPort toPort : endRequirement.getLocations()) {
			if (fromPort == toPort) {
				closestPort = toPort;
				break;
			}
			int distance = distanceProvider.getMinimumValue(fromPort, toPort);
			if (distance < closestPortDistance) {
				closestPort = toPort;
				closestPortDistance = distance;
			}
		}

		if (closestPort != null) {
			sequence.set(sequence.size() - 1, returnElementProvider.getReturnElement(resource, closestPort));
		}
	}

	/**
	 * Swap the dummy element in for the given sequence, and set its location to the given port.
	 * 
	 * @param resource
	 * 
	 * @param sequence
	 * @param location
	 */
	private final void adjustLastElement(final IResource resource, final IModifiableSequence sequence, final ISequenceElement returnElement) {
		if (returnElement == null) {
			return;
		}
		/*
		 * Look up the port we are returning to, and then set that as the port for the dummy element.
		 */
		final IPort returnPort = portProvider.getPortForElement(returnElement);

		/*
		 * Replace the final sequence element with the dummy
		 * 
		 * TODO consider merging this with the start-end-requirement stuff
		 */
		sequence.set(sequence.size() - 1, returnElementProvider.getReturnElement(resource, returnPort));
	}

	/**
	 * Find and return the first element in the sequence with {@link PortType} PortType.Load
	 * 
	 * @param sequence
	 */
	public ISequenceElement getFirstLoadElement(final IModifiableSequence sequence) {
		ISequenceElement t = null;
		for (final ISequenceElement element : sequence) {
			final PortType type = portTypeProvider.getPortType(element);
			// Record first load port
			if (type == PortType.Load) {
				t = element;
				break;
			}
		}
		return t;
	}

	public IPortTypeProvider getPortTypeProvider() {
		return portTypeProvider;
	}

	public void setPortTypeProvider(final IPortTypeProvider portTypeProvider) {
		this.portTypeProvider = portTypeProvider;
	}

	public IPortProvider getPortProvider() {
		return portProvider;
	}

	public void setPortProvider(final IPortProvider portProvider) {
		this.portProvider = portProvider;
	}

	public IReturnElementProvider getReturnElementProvider() {
		return returnElementProvider;
	}

	public void setReturnElementProvider(final IReturnElementProvider returnElementProvider) {
		this.returnElementProvider = returnElementProvider;
	}

	/**
	 * Specify the {@link EndLocationRule} for this {@link IResource}
	 * 
	 * @param resource
	 * @param rule
	 */
	public void setEndLocationRule(final IResource resource, final EndLocationRule rule) {
		ruleMap.put(resource, rule);
	}

	/**
	 * Returns the {@link EndLocationRule} set for this {@link IResource}. Returns {@link EndLocationRule#NONE} if nothing has been set.
	 * 
	 * @param resource
	 * @return
	 */
	public EndLocationRule getEndLocationRule(final IResource resource) {
		if (ruleMap.containsKey(resource)) {
			return ruleMap.get(resource);
		}
		return EndLocationRule.NONE;
	}

	@Override
	public void dispose() {
		ruleMap.clear();
	}

	public IStartEndRequirementProvider getStartEndRequirementProvider() {
		return startEndRequirementProvider;
	}

	public void setStartEndRequirementProvider(IStartEndRequirementProvider startEndRequirementProvider) {
		this.startEndRequirementProvider = startEndRequirementProvider;
	}

	public IMultiMatrixProvider<IPort, Integer> getDistanceProvider() {
		return distanceProvider;
	}

	public void setDistanceProvider(IMultiMatrixProvider<IPort, Integer> distanceProvider) {
		this.distanceProvider = distanceProvider;
	}
}

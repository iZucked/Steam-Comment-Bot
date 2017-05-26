/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.manipulators;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.ICharterMarketProvider;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IElementPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IReturnElementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.charterout.IGeneratedCharterOutEvaluator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.charterout.impl.NullGeneratedCharterOutEvaluator;
import com.mmxlabs.scheduler.optimiser.shared.port.DistanceMatrixEntry;

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
public class EndLocationSequenceManipulator implements ISequencesManipulator {
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

		/**
		 * Return to the closest charter out port
		 */

		RETURN_TO_CLOSEST_CHARTER_OUT_PORT,

		/**
		 */
		REMOVE,
	}

	@Inject
	private IPortTypeProvider portTypeProvider;

	@Inject
	private IElementPortProvider portProvider;

	private final Map<IResource, EndLocationRule> ruleMap = new HashMap<IResource, EndLocationSequenceManipulator.EndLocationRule>();

	@Inject
	private IReturnElementProvider returnElementProvider;

	@Inject
	private IStartEndRequirementProvider startEndRequirementProvider;

	@Inject
	private IDistanceProvider distanceProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IElementDurationProvider durationsProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject(optional = true)
	private IGeneratedCharterOutEvaluator charterOutEvaluator;

	@Inject
	private ICharterMarketProvider charterMarketProvider;

	public EndLocationSequenceManipulator() {

	}

	/**
	 */
	@Inject
	public void init(final IOptimisationData data) {

		for (final IResource resource : data.getResources()) {
			final VesselInstanceType vesselInstanceType = vesselProvider.getVesselAvailability(resource).getVesselInstanceType();
			if (vesselInstanceType == VesselInstanceType.DES_PURCHASE || vesselInstanceType == VesselInstanceType.FOB_SALE) {
				setEndLocationRule(resource, EndLocationRule.NONE);
			} else if (vesselInstanceType == VesselInstanceType.ROUND_TRIP) {
				setEndLocationRule(resource, EndLocationRule.REMOVE);
			} else if (vesselInstanceType.equals(VesselInstanceType.SPOT_CHARTER) && vesselProvider.getVesselAvailability(resource).getBallastBonusContract() == null) {
				setEndLocationRule(resource, EndLocationRule.RETURN_TO_FIRST_LOAD);
			} else {
				// Some fleet vessels will have an existing end location
				// requirement;
				// return to last load does not apply there
				// however, fleet vessels which do not have an end location requirement
				// should return to their last load port.

				final IStartEndRequirement endRequirement = startEndRequirementProvider.getEndRequirement(resource);
				if (!endRequirement.hasPortRequirement()) {
					// choose between returning to last load port or a charter out port
					setNoRequirementEndLocationRule(resource);
				} else if (endRequirement.hasPortRequirement() && endRequirement.getLocation() == null) {
					setEndLocationRule(resource, EndLocationRule.RETURN_TO_CLOSEST_IN_SET);
				}
			}
		}

	}

	private void setNoRequirementEndLocationRule(final @NonNull IResource resource) {
		// TODO: Remove NullGeneratedCharterOutEvaluator at some point
		boolean returnToLastPort = true;
		if (charterOutEvaluator != null && !(charterOutEvaluator instanceof NullGeneratedCharterOutEvaluator)) {
			final Set<@NonNull IPort> charteringPorts = getCharterMarketPortsForResource(resource);
			if (charteringPorts.size() > 0) {
				returnToLastPort = false;
			}
		}
		if (returnToLastPort) {
			setEndLocationRule(resource, EndLocationRule.RETURN_TO_LAST_LOAD);
		} else {
			setEndLocationRule(resource, EndLocationRule.RETURN_TO_CLOSEST_CHARTER_OUT_PORT);
		}
	}

	private @NonNull Set<@NonNull IPort> getCharterMarketPortsForResource(final @NonNull IResource resource) {
		final IVesselClass resourceVesselClass = getVesselClass(resource);
		return charterMarketProvider.getCharteringPortsForVesselClass(resourceVesselClass);
	}

	private @NonNull IVesselClass getVesselClass(final @NonNull IResource resource) {
		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		final IVessel vessel = vesselAvailability.getVessel();
		return vessel.getVesselClass();
	}

	@Override
	public void manipulate(final @NonNull IModifiableSequences sequences) {
		assert portTypeProvider != null;

		// Loop through each sequence in turn and manipulate
		for (final Map.Entry<@NonNull IResource, @NonNull IModifiableSequence> entry : sequences.getModifiableSequences().entrySet()) {
			manipulate(entry.getKey(), entry.getValue());
		}
	}

	public void manipulate(final @NonNull IResource resource, final @NonNull IModifiableSequence sequence) {

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
		case RETURN_TO_CLOSEST_CHARTER_OUT_PORT:
			returnToClosestCharterOutPort(resource, sequence);
			break;
		case REMOVE:
			sequence.remove(sequence.size() - 1);
			break;
		default:
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
	 * Return to the closest port to the last in the user defined set of ports
	 * 
	 * @param resource
	 * @param sequence
	 */
	private final void returnToClosestInSet(final @NonNull IResource resource, final @NonNull IModifiableSequence sequence) {
		final IStartEndRequirement endRequirement = startEndRequirementProvider.getEndRequirement(resource);
		returnToClosestInSet(resource, sequence, endRequirement.getLocations());
	}

	/**
	 * Return to the closest charter out port to the last in the port in the sequence
	 * 
	 * @param resource
	 * @param sequence
	 */
	private final void returnToClosestCharterOutPort(final @NonNull IResource resource, final @NonNull IModifiableSequence sequence) {
		returnToClosestInSet(resource, sequence, getCharterMarketPortsForResource(resource));
	}

	/**
	 * Swap the dummy element in for the given sequence, and set its location to the given port.
	 * 
	 * @param resource
	 * @param sequence
	 * @param ports
	 */
	private final void returnToClosestInSet(final @NonNull IResource resource, final @NonNull IModifiableSequence sequence, final @NonNull Collection<@NonNull IPort> ports) {
		if (sequence.size() < 2) {
			return;
		}

		final ISequenceElement lastVisit = sequence.get(sequence.size() - 2);
		final IPort fromPort = portProvider.getPortForElement(lastVisit);
		// The time window should not be null for most sequences. However a "bad" sequence (one which the constraint checkers will throw out) may well present a null time window here, so be lenient.
		final @Nullable ITimeWindow timeWindow = portSlotProvider.getPortSlot(lastVisit).getTimeWindow();
		final int visitDuration = durationsProvider.getElementDuration(lastVisit, resource);
		final int lastVoyageStartTime = (timeWindow == null ? 0 : timeWindow.getInclusiveStart()) + visitDuration;

		IPort closestPort = null;
		int closestPortDistance = Integer.MAX_VALUE;
		for (final IPort toPort : ports) {
			if (fromPort == toPort) {
				closestPort = toPort;
				break;
			}

			final List<DistanceMatrixEntry> distanceValues = distanceProvider.getDistanceValues(fromPort, toPort, lastVoyageStartTime, vesselProvider.getVesselAvailability(resource).getVessel());
			int distance = Integer.MAX_VALUE;
			for (final DistanceMatrixEntry distanceOption : distanceValues) {
				final int routeDistance = distanceOption.getDistance();
				if (routeDistance < distance) {
					distance = routeDistance;
				}
			}

			if (distance < closestPortDistance) {
				closestPort = toPort;
				closestPortDistance = distance;
			}
		}

		if (closestPort != null) {
			final @Nullable ISequenceElement elementToReturn = returnElementProvider.getReturnElement(resource, closestPort);
			assert elementToReturn != null;

			sequence.set(sequence.size() - 1, elementToReturn);
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
	private final void adjustLastElement(final @NonNull IResource resource, final @NonNull IModifiableSequence sequence, final @Nullable ISequenceElement returnElement) {
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
		@Nullable
		final ISequenceElement elementToReturn = returnElementProvider.getReturnElement(resource, returnPort);

		assert elementToReturn != null;

		sequence.set(sequence.size() - 1, elementToReturn);
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
}

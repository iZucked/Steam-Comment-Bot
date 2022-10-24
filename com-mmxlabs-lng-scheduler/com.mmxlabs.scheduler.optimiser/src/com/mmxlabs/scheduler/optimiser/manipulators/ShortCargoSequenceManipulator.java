/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.manipulators;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISegment;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.impl.DisconnectedSegment;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IDistanceProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IShortCargoReturnElementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.shared.port.DistanceMatrixEntry;

/**
 * A {@link ISequencesManipulator} to insert return elements into a sequence of
 * Load and Discharge slots on {@link VesselInstanceType#ROUND_TRIP} sequences.
 * 
 */
public class ShortCargoSequenceManipulator implements ISequencesManipulator {

	@Inject
	private IPortTypeProvider portTypeProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IStartEndRequirementProvider startEndRequirementProvider;

	@Inject
	private IShortCargoReturnElementProvider shortCargoReturnElementProvider;

	@Inject
	private IDistanceProvider distanceProvider;

	@Override
	public void manipulate(final IModifiableSequences sequences) {

		for (final IResource resource : sequences.getResources()) {
			final IVesselCharter vesselCharter = vesselProvider.getVesselCharter(resource);
			if (vesselCharter.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
				final IEndRequirement endRequirement = startEndRequirementProvider.getEndRequirement(resource);

				// Is there a specific port to return to?
				final IPort specificPort = endRequirement.hasPortRequirement() ? endRequirement.getLocation() : null;
				// Else are we returning to first load?
				final boolean returnToLoad = !(endRequirement.hasPortRequirement() && endRequirement.getLocation() == null);

				final IModifiableSequence seq = sequences.getModifiableSequence(resource);
				final int size = seq.size();
				// Loop backwards to avoid needing to update index by inserted item count
				for (int i = size - 1; i >= 0; --i) {
					final ISequenceElement element = seq.get(i);
					final PortType portType = portTypeProvider.getPortType(element);
					if (portType == PortType.Load) {
						final ISequenceElement returnElement;
						if (specificPort != null) {
							returnElement = shortCargoReturnElementProvider.getReturnElement(resource, element, specificPort);
						} else if (returnToLoad) {
							final IPort loadPort = portSlotProvider.getPortSlot(element).getPort();
							returnElement = shortCargoReturnElementProvider.getReturnElement(resource, element, loadPort);
						} else {
							// Find last discharge at the D -> L transition
							ISequenceElement lastDischarge = null;
							// Find the insertion location at the D -> L transition
							boolean foundDischarge = false;
							for (int j = i + 1; j < seq.size(); ++j) {
								final ISequenceElement nextE = seq.get(j);
								final PortType nextPortType = portTypeProvider.getPortType(nextE);
								if (nextPortType == PortType.Discharge) {
									lastDischarge = nextE;
									foundDischarge = true;
								} else {
									if (foundDischarge) {
										break;
									}
								}
							}
							assert lastDischarge != null;
							final IPort dischargePort = portSlotProvider.getPortSlot(lastDischarge).getPort();
							final IPort destPort = returnToClosestInSet(resource, dischargePort, endRequirement.getLocations());
							returnElement = shortCargoReturnElementProvider.getReturnElement(resource, element, destPort);
						}
						assert returnElement != null;

						// If element is null we expect that a constraint checker will mark the solution
						// as invalid.... (if not, then expect some sort of failure in the
						// PortTimesPlanner/VoyagePlanner
						// classes)
						// The sequence manipulator API does not have a way to mark solution failed.
						// If it is null, then we have tried to put a load on a nominal cargo vessel
						// that was not expected to be placed on a nominal vessel.
						if (returnElement != null) {

							final ISegment segment = new DisconnectedSegment(Collections.singletonList(returnElement));

							int insertPoint = -1;
							// Find the insertion location at the D -> L transition
							boolean foundDischarge = false;
							for (int j = i + 1; j < seq.size(); ++j) {
								final ISequenceElement nextE = seq.get(j);
								final PortType nextPortType = portTypeProvider.getPortType(nextE);
								if (!foundDischarge) {
									if (nextPortType == PortType.Discharge) {
										foundDischarge = true;
									}
								} else {
									if (nextPortType == PortType.Load) {
										insertPoint = j;
										break;
									}
								}
							}
							if (insertPoint != -1) {
								seq.insert(insertPoint, segment);
							} else {
								// We didn't find a D to L transition, so assume this is the last cargo and we
								// can append to the list
								seq.add(returnElement);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Swap the dummy element in for the given sequence, and set its location to the
	 * given port.
	 * 
	 * @param resource
	 * @param sequence
	 * @param ports
	 */
	private final IPort returnToClosestInSet(final @NonNull IResource resource, final IPort fromPort, final @NonNull Collection<@NonNull IPort> ports) {
		IPort closestPort = null;
		int closestPortDistance = Integer.MAX_VALUE;
		for (final IPort toPort : ports) {
			if (fromPort == toPort) {
				closestPort = toPort;
				break;
			}

			final List<DistanceMatrixEntry> distanceValues = distanceProvider.getDistanceValues(fromPort, toPort, vesselProvider.getVesselCharter(resource).getVessel());
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
		return closestPort;
	}
}

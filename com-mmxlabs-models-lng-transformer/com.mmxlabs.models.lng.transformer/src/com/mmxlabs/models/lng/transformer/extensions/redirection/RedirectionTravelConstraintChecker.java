/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.redirection;

import java.util.List;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.INominatedVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IShippingHoursRestrictionProvider;

public class RedirectionTravelConstraintChecker implements IPairwiseConstraintChecker {

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IMultiMatrixProvider<IPort, Integer> distanceProvider;

	@Inject
	private INominatedVesselProvider nominatedVesselProvider;

	@Inject
	private IShippingHoursRestrictionProvider shippingHoursRestrictionProvider;

	@Inject
	private IElementDurationProvider durationProvider;

	@Override
	public String getName() {
		return "RedirectionTravelConstraintChecker";
	}

	@Override
	public boolean checkConstraints(final ISequences sequences) {
		return checkConstraints(sequences, null);
	}

	@Override
	public boolean checkConstraints(final ISequences sequences, final List<String> messages) {
		final ISequenceElement prevElement = null;
		for (final IResource resource : sequences.getResources()) {
			final ISequence sequence = sequences.getSequence(resource);
			for (final ISequenceElement element : sequence) {
				if (prevElement != null) {
					if (!checkPairwiseConstraint(prevElement, element, resource)) {
						return false;
					}
				}
			}
		}

		return true;
	}

	@Override
	public void setOptimisationData(final IOptimisationData optimisationData) {

	}

	@Override
	public boolean checkPairwiseConstraint(final ISequenceElement first, final ISequenceElement second, final IResource resource) {

		final IPortSlot firstSlot = portSlotProvider.getPortSlot(first);
		final IPortSlot secondSlot = portSlotProvider.getPortSlot(second);

		// Check if FOB/DES
		// Check if ports are different
		// Check distances using injected provider, or actual/max speed.

		if (firstSlot instanceof ILoadOption && secondSlot instanceof IDischargeSlot) {
			// DES Purchase
			final ILoadOption desPurchase = (ILoadOption) firstSlot;
			final IDischargeSlot desSlot = (IDischargeSlot) secondSlot;

			if (desPurchase.getPort() != desSlot.getPort()) {

				// TODO: Take time windows into account
				final int shippingHours = shippingHoursRestrictionProvider.getShippingHoursRestriction(first);
				if (shippingHours == IShippingHoursRestrictionProvider.RESTRICTION_UNDEFINED) {
					// No
					return false;
				}
				final ITimeWindow fobLoadDate = shippingHoursRestrictionProvider.getBaseTime(first);
				if (fobLoadDate == null) {
					// Should have a date!
					return false;
				}

				// Check laden leg is within available time -> Add to lateness constraint checker
				// Check load Time + laden leg + discharge time + ballast leg is <= shippingHours

				final int ladenDistance = distanceProvider.getMinimumValue(desPurchase.getPort(), desSlot.getPort());
				if (ladenDistance < 0 || ladenDistance == Integer.MAX_VALUE) {
					// Bad distance
					return false;
				}

				final int ballastDistance = distanceProvider.getMinimumValue(desSlot.getPort(), desPurchase.getPort());
				if (ballastDistance < 0 || ballastDistance == Integer.MAX_VALUE) {
					// Bad distance
					return false;
				}

				final IVessel nominatedVessel = nominatedVesselProvider.getNominatedVessel(first);
				if (nominatedVessel == null) {
					return false;
				}
				// Get notional speed
				final int maxSpeed = nominatedVessel.getVesselClass().getMaxSpeed();

				final int loadDuration = durationProvider.getElementDuration(first, nominatedVessel);
				final int dischargeDuration = durationProvider.getElementDuration(second, nominatedVessel);
				final int ballastSailingTime = Calculator.getTimeFromSpeedDistance(maxSpeed, ballastDistance);

				// This is the upper bound on laden travel time
				final int availableLadenTime = shippingHours - loadDuration + dischargeDuration + ballastSailingTime;

				// It will take at least this amount of time to get between ports - check shipping days is big enough
				final int minLadenSailingTime = Calculator.getTimeFromSpeedDistance(maxSpeed, ladenDistance);
				if (minLadenSailingTime > availableLadenTime) {
					// Can't go fast enough
					return false;
				}
				
				// It will take at least this amount of time to get between ports - check time windows
				final int maxWindowLength = desSlot.getTimeWindow().getEnd()  - fobLoadDate.getStart();
				if (minLadenSailingTime > maxWindowLength) {
					// Lateness!
					return false;
				}
				// Laden leg is at least this amount of time due to windows
				final int minWindowLength = desSlot.getTimeWindow().getStart()  - fobLoadDate.getEnd();
				if (minWindowLength > availableLadenTime) {
					// Windows too far apart for shipping restriction
					return false;
				}
				
				return true;
			}
			// TODO: FOB Sale
		}

		return true;
	}

	@Override
	public String explain(final ISequenceElement first, final ISequenceElement second, final IResource resource) {
		return null;
	}

}

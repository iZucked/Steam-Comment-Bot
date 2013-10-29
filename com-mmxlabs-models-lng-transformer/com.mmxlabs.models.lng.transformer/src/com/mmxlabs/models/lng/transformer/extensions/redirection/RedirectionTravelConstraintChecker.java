/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.redirection;

import java.util.List;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;

public class RedirectionTravelConstraintChecker implements IPairwiseConstraintChecker {

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IMultiMatrixProvider<IPort, Integer> distanceProvider;

	@Inject
	private RedirectionInformationProvider redirectionGroupProvider;

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
		ISequenceElement prevElement = null;
		for (IResource resource : sequences.getResources()) {
			ISequence sequence = sequences.getSequence(resource);
			for (ISequenceElement element : sequence) {
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

		final IPortSlot portSlot = portSlotProvider.getPortSlot(first);
		if (portSlot instanceof ILoadOption) {
			final ILoadOption loadOption = (ILoadOption) portSlot;
			if (loadOption.getLoadPriceCalculator() instanceof RedirectionContract) {

				final RedirectionContract redirectionContract = (RedirectionContract) loadOption.getLoadPriceCalculator();

				final IPortSlot nextSlot = portSlotProvider.getPortSlot(second);
				if (nextSlot instanceof IDischargeOption) {
					final IDischargeOption dischargeOption = (IDischargeOption) nextSlot;

					// TODO: Take time windows into account
					final Integer shippingHours = redirectionGroupProvider.getShippingHours(loadOption);
					final Integer fobLoadDate = redirectionGroupProvider.getFOBLoadTime(loadOption);

					if (fobLoadDate == null) {
						throw new IllegalStateException("Redirection LoadSlot must have FOB Load date");
					}

					if (shippingHours == null) {
						throw new IllegalStateException("Redirection LoadSlot must have shipping days limit");
					}

					// Amount of time between load and discharge windows.
					final int availableTime = dischargeOption.getTimeWindow().getStart() - fobLoadDate.intValue();
					if (availableTime > shippingHours) {
						// The slots are too far apart to be compatible.
						return false;
					}

					final int distance = distanceProvider.getMinimumValue(loadOption.getPort(), dischargeOption.getPort());
					if (distance < 0 || distance == Integer.MAX_VALUE) {
						// Bad distance
						return false;
					}

					// Get notional speed
					final int speed = redirectionContract.getNotionalSpeed();

					// Calculate sailing time.
					final int sailingTime = Calculator.getTimeFromSpeedDistance(speed, distance);
					final int sailingTimeLimit;
					// Accept if travel time is less than or equal to available time
					return sailingTime <= availableTime;
				} else {
					// Unexpected combination
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public String explain(final ISequenceElement first, final ISequenceElement second, final IResource resource) {
		return null;
	}

}

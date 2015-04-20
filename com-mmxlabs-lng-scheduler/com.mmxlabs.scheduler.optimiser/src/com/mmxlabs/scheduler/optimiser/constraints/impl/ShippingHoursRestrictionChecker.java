/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.List;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.calculators.IDivertableDESShippingTimesCalculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.INominatedVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortVisitDurationProvider;
import com.mmxlabs.scheduler.optimiser.providers.IShippingHoursRestrictionProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 */
public class ShippingHoursRestrictionChecker implements IPairwiseConstraintChecker {

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IMultiMatrixProvider<IPort, Integer> distanceProvider;

	@Inject
	private INominatedVesselProvider nominatedVesselProvider;

	@Inject
	private IShippingHoursRestrictionProvider shippingHoursRestrictionProvider;

	@Inject
	private IPortVisitDurationProvider portVisitDurationProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IActualsDataProvider actualsDataProvider;

	@Inject IDivertableDESShippingTimesCalculator dischargeTimeCalculator;
	
	@Override
	public String getName() {
		return "ShippingHoursRestrictionChecker";
	}

	@Override
	public boolean checkConstraints(final ISequences sequences) {
		return checkConstraints(sequences, null);
	}

	@Override
	public boolean checkConstraints(final ISequences sequences, final List<String> messages) {

		for (final IResource resource : sequences.getResources()) {
			final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
			if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {

				final ISequence sequence = sequences.getSequence(resource);
				ISequenceElement prevElement = null;
				for (final ISequenceElement element : sequence) {
					if (prevElement != null) {
						if (!checkPairwiseConstraint(prevElement, element, resource)) {
							return false;
						}
					}
					prevElement = element;
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

		final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
		if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {

			final IPortSlot firstSlot = portSlotProvider.getPortSlot(first);
			final IPortSlot secondSlot = portSlotProvider.getPortSlot(second);

			// If data is actualised, we do not care
			if (actualsDataProvider.hasActuals(firstSlot) && actualsDataProvider.hasActuals(secondSlot)) {
				return true;
			}

			// Check if FOB/DES
			// Check if ports are different
			// Check distances using injected provider, or actual/max speed.

			if (firstSlot instanceof ILoadOption && secondSlot instanceof IDischargeSlot) {
				if (firstSlot.getId().equals("S2")) {
					int i = 0;
				}
				// DES Purchase
				final ILoadOption desPurchase = (ILoadOption) firstSlot;
				final IDischargeSlot desSale = (IDischargeSlot) secondSlot;

				if (shippingHoursRestrictionProvider.isDivertable(first)) {
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

					final Pair<Integer, Integer> desTimes = dischargeTimeCalculator.getDivertableDESTimes(desPurchase, desSale, nominatedVesselProvider.getNominatedVessel(first), resource);
					int dischargeTime = desTimes.getFirst();
					int returnTime = desTimes.getSecond();
					if (returnTime - dischargeTime > shippingHours) {
						return false;
					}
					return true;
				}
				// TODO: FOB Sale
			}
		}
		return true;
	}

	@Override
	public String explain(final ISequenceElement first, final ISequenceElement second, final IResource resource) {
		return null;
	}

}

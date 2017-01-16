/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.calculators.IDivertableDESShippingTimesCalculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.INominatedVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IShippingHoursRestrictionProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 */
public class ShippingHoursRestrictionChecker implements IPairwiseConstraintChecker {

	@Inject
	@NonNull
	private IPortSlotProvider portSlotProvider;

	@Inject
	@NonNull
	private INominatedVesselProvider nominatedVesselProvider;

	@Inject
	@NonNull
	private IShippingHoursRestrictionProvider shippingHoursRestrictionProvider;

	@Inject
	@NonNull
	private IVesselProvider vesselProvider;

	@Inject
	@NonNull
	private IActualsDataProvider actualsDataProvider;

	@Inject
	@NonNull
	private IDivertableDESShippingTimesCalculator dischargeTimeCalculator;

	@Override
	@NonNull
	public String getName() {
		return "ShippingHoursRestrictionChecker";
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources) {
		return checkConstraints(sequences, changedResources, null);
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources, @Nullable final List<String> messages) {
		final Collection<@NonNull IResource> loopResources;
		if (changedResources == null) {
			loopResources = sequences.getResources();
		} else {
			loopResources = changedResources;
		}

		for (final IResource resource : loopResources) {
			final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
			if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {

				final ISequence sequence = sequences.getSequence(resource);
				ISequenceElement prevElement = null;
				for (final ISequenceElement element : sequence) {
					assert element != null;
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
	public void setOptimisationData(@NonNull final IOptimisationData optimisationData) {

	}

	@Override
	public boolean checkPairwiseConstraint(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource) {

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

					@Nullable
					final IVessel nominatedVessel = nominatedVesselProvider.getNominatedVessel(first);
					if (nominatedVessel == null) {
						return false;
					}
					final Pair<Integer, Integer> desTimes = dischargeTimeCalculator.getDivertableDESTimes(desPurchase, desSale, nominatedVessel, resource);
					if (desTimes == null) {
						return false;
					}
					final int returnTime = desTimes.getSecond();
					if (returnTime - fobLoadDate.getInclusiveStart() > shippingHours) {
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
	public String explain(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource) {
		return null;
	}

}

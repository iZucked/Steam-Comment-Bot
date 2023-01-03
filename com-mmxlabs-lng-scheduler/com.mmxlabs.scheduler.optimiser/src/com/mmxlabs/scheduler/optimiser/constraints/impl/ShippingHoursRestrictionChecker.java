/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.scheduler.optimiser.calculators.IDivertibleDESShippingTimesCalculator;
import com.mmxlabs.scheduler.optimiser.calculators.IDivertibleFOBShippingTimesCalculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
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
	private IPortSlotProvider portSlotProvider;

	@Inject
	private INominatedVesselProvider nominatedVesselProvider;

	@Inject
	private IShippingHoursRestrictionProvider shippingHoursRestrictionProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IActualsDataProvider actualsDataProvider;

	@Inject
	private IDivertibleDESShippingTimesCalculator dischargeTimeCalculator;

	@Inject
	private IDivertibleFOBShippingTimesCalculator fobSaleTimeCalculator;

	@Override
	@NonNull
	public String getName() {
		return "ShippingHoursRestrictionChecker";
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources, final List<String> messages) {
		final Collection<@NonNull IResource> loopResources;
		if (changedResources == null) {
			loopResources = sequences.getResources();
		} else {
			loopResources = changedResources;
		}

		for (final IResource resource : loopResources) {
			final IVesselCharter vesselCharter = vesselProvider.getVesselCharter(resource);
			if (vesselCharter.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselCharter.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {

				final ISequence sequence = sequences.getSequence(resource);
				ISequenceElement prevElement = null;
				for (final ISequenceElement element : sequence) {
					assert element != null;
					if (prevElement != null) {
						if (!checkPairwiseConstraint(prevElement, element, resource, messages)) {
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
	public boolean checkPairwiseConstraint(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource, final List<String> messages) {

		final IVesselCharter vesselCharter = vesselProvider.getVesselCharter(resource);
		if (vesselCharter.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselCharter.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {

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

				if (shippingHoursRestrictionProvider.isDivertible(first)) {
					final int shippingHours = shippingHoursRestrictionProvider.getShippingHoursRestriction(first);
					if (shippingHours == IShippingHoursRestrictionProvider.RESTRICTION_UNDEFINED) {
						// No
						if (messages != null)
							messages.add(String.format("%s: DES purchase %s and DES sale %s have no shipping hours restriction set!", 
								this.getName(), desPurchase.getId(), desSale.getId()));
						return false;
					}
					final ITimeWindow fobLoadDate = shippingHoursRestrictionProvider.getBaseTime(first);
					if (fobLoadDate == null) {
						// Should have a date!
						if (messages != null)
							messages.add(String.format("%s: DES purchase %s have no load date set!", 
								this.getName(), desPurchase.getId()));
						return false;
					}

					@Nullable
					final IVessel nominatedVessel = nominatedVesselProvider.getNominatedVessel(first);
					if (nominatedVessel == null) {
						if (messages != null)
							messages.add(String.format("%s: DES purchase %s and DES sale %s have no nominated vessel set!", 
								this.getName(), desPurchase.getId(), desSale.getId()));
						return false;
					}
					final Pair<Integer, Integer> desTimes = dischargeTimeCalculator.getDivertibleDESTimes(desPurchase, desSale, nominatedVessel, resource);
					if (desTimes == null) {
						if (messages != null)
							messages.add(String.format("%s: DES purchase %s and DES sale %s have no DES times!", 
								this.getName(), desPurchase.getId(), desSale.getId()));
						return false;
					}
					final int returnTime = desTimes.getSecond();
					if (returnTime - fobLoadDate.getInclusiveStart() > shippingHours) {
						if (messages != null)
							messages.add(String.format("%s: Travel time between DES purchase %s and DES sale %s is too long!", 
								this.getName(), desPurchase.getId(), desSale.getId()));
						return false;
					}
					return true;
				}

			} else if (firstSlot instanceof ILoadSlot && secondSlot instanceof IDischargeOption) {
				// FOB Sale
				final ILoadSlot fobPurchase = (ILoadSlot) firstSlot;
				final IDischargeOption fobSale = (IDischargeOption) secondSlot;

				if (shippingHoursRestrictionProvider.isDivertible(second)) {
					final int shippingHours = shippingHoursRestrictionProvider.getShippingHoursRestriction(second);
					if (shippingHours == IShippingHoursRestrictionProvider.RESTRICTION_UNDEFINED) {
						// Assume that we have a windows overlap case
						ITimeWindow tw1 = fobPurchase.getTimeWindow();
						ITimeWindow tw2 = fobSale.getTimeWindow();
						if (tw1 != null && tw2 != null) {
							if (!tw1.overlaps(tw2) && messages != null)
								messages.add(String.format("%s: FOB purchase %s and FOB sale %s time windows don't overlap!", 
										this.getName(), fobPurchase.getId(), fobSale.getId()));
							return tw1.overlaps(tw2);
						}
						if (messages != null)
							messages.add(String.format("%s: FOB purchase %s or FOB sale %s has no time window!", 
								this.getName(), fobPurchase.getId(), fobSale.getId()));
						return false;
					}
					final ITimeWindow fobSaleWindow = shippingHoursRestrictionProvider.getBaseTime(second);
					if (fobSaleWindow == null) {
						// Should have a date!
						if (messages != null)
							messages.add(String.format("%s: FOB purchase %s has no load date!", 
								this.getName(), fobPurchase.getId(), fobSale.getId()));
						return false;
					}

					@Nullable
					final IVessel nominatedVessel = nominatedVesselProvider.getNominatedVessel(second);
					if (nominatedVessel == null) {
						if (messages != null)
							messages.add(String.format("%s: FOB purchase %s and FOB sale %s have no nominated vessel set!", 
								this.getName(), fobPurchase.getId(), fobSale.getId()));
						return false;
					}
					final Triple<Integer, Integer, Integer> desTimes = fobSaleTimeCalculator.getDivertibleFOBTimes(fobPurchase, fobSale, nominatedVessel, resource);
					if (desTimes == null) {
						if (messages != null)
							messages.add(String.format("%s: FOB purchase %s and FOB sale %s have no DES times!", 
								this.getName(), fobPurchase.getId(), fobSale.getId()));
						return false;
					}
					final int returnTime = desTimes.getThird();
					if (returnTime - desTimes.getFirst() > shippingHours) {
						if (messages != null)
							messages.add(String.format("%s: Travel from FOB purchase %s to FOB sale %s is too long!", 
								this.getName(), fobPurchase.getId(), fobSale.getId()));
						return false;
					}
					return true;
				}
			}
		}
		return true;

	}
}

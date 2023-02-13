/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.calculators.IDivertibleDESShippingTimesCalculator;
import com.mmxlabs.scheduler.optimiser.calculators.IDivertibleFOBShippingTimesCalculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.util.CargoTypeUtil;
import com.mmxlabs.scheduler.optimiser.components.util.CargoTypeUtil.SimpleCargoType;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.INominatedVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IShippingHoursRestrictionProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortTimesRecord;

/**
 * 
 */
public class DefaultCustomNonShippedScheduler implements ICustomNonShippedScheduler {

	@Inject
	private IElementDurationProvider durationProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IShippingHoursRestrictionProvider shippingHoursRestrictionProvider;

	@Inject
	private INominatedVesselProvider nominatedVesselProvider;

	@Inject
	private IDivertibleDESShippingTimesCalculator dischargeTimeCalculator;

	@Inject
	private IDivertibleFOBShippingTimesCalculator loadTimeCalculator;

	@Inject
	private IActualsDataProvider actualsDataProvider;

	@Override
	public void modifyArrivalTimes(final @NonNull IResource resource, final @NonNull PortTimesRecord portTimesRecord) {

		final List<@NonNull IPortSlot> slots = new ArrayList<>(portTimesRecord.getSlots());

		// These handle current special cases around FOB/DES
		@Nullable
		IVessel nominatedVessel = null;
		boolean hasActuals = true;

		for (final IPortSlot slot : slots) {

			if (slot instanceof ILoadOption) {
				// loadIdx = i;
				final ILoadOption loadOption = (ILoadOption) slot;
				if (!(loadOption instanceof ILoadSlot)) {
					nominatedVessel = nominatedVesselProvider.getNominatedVessel(portSlotProvider.getElement(loadOption));
				}
				hasActuals &= actualsDataProvider.hasActuals(slot);

			} else if (slot instanceof IDischargeOption) {
				final IDischargeOption dischargeOption = (IDischargeOption) slot;
				if (!(dischargeOption instanceof IDischargeSlot)) {
					nominatedVessel = nominatedVesselProvider.getNominatedVessel(portSlotProvider.getElement(dischargeOption));
				}
				hasActuals &= actualsDataProvider.hasActuals(slot);
			}
		}

		if (nominatedVessel != null) {
			final ILoadOption buyOption = (ILoadOption) slots.get(0);
			assert buyOption != null;

			final IDischargeOption sellOption = (IDischargeOption) slots.get(1);
			assert sellOption != null;

			SimpleCargoType cargoType = CargoTypeUtil.getSimpleCargoType(buyOption, sellOption);
			if (cargoType != SimpleCargoType.SHIPPED) {

				// Only consider divertible FOB or DES
				if (cargoType == SimpleCargoType.FOB_SALE && !shippingHoursRestrictionProvider.isDivertible(portSlotProvider.getElement(sellOption))) {
					return;
				} else if (cargoType == SimpleCargoType.DES_PURCHASE && !shippingHoursRestrictionProvider.isDivertible(portSlotProvider.getElement(buyOption))) {
					return;
				}

				if (hasActuals) {
					final int loadTime = actualsDataProvider.getArrivalTime(buyOption);
					final int dischargeTime = actualsDataProvider.getArrivalTime(sellOption);

					portTimesRecord.setSlotTime(buyOption, loadTime);
					portTimesRecord.setSlotTime(sellOption, dischargeTime);
					final List<@NonNull IPortSlot> recordSlots = portTimesRecord.getSlots();
					portTimesRecord.setSlotTime(recordSlots.get(0), loadTime);
					portTimesRecord.setSlotTime(recordSlots.get(recordSlots.size() - 1), dischargeTime);

					portTimesRecord.setSlotDuration(buyOption, actualsDataProvider.getVisitDuration(buyOption));
					portTimesRecord.setSlotDuration(sellOption, actualsDataProvider.getVisitDuration(sellOption));

//					portTimesRecord.setSlotExtraIdleTime(buyOption, 0);
//					portTimesRecord.setSlotExtraIdleTime(sellOption, 0);
				} else {
					// Adjust for notional speed
					assert nominatedVessel != null;
					final int loadTime;
					final int dischargeTime;
					if (cargoType == SimpleCargoType.DES_PURCHASE) {
						final ISequenceElement buyElement = portSlotProvider.getElement(buyOption);
						loadTime = shippingHoursRestrictionProvider.getBaseTime(buyElement).getInclusiveStart();
						dischargeTime = dischargeTimeCalculator.getDivertibleDESTimes(buyOption, sellOption, nominatedVessel, resource).getFirst();
					} else {

						final ISequenceElement sellElement = portSlotProvider.getElement(sellOption);
						Triple<@NonNull Integer, @NonNull Integer, @NonNull Integer> divertibleFOBTimes = loadTimeCalculator.getDivertibleFOBTimes(buyOption, sellOption, nominatedVessel, resource);
						loadTime = divertibleFOBTimes.getFirst();
						final int dischargeTimeIdeal = shippingHoursRestrictionProvider.getBaseTime(sellElement).getInclusiveStart();
						dischargeTime = divertibleFOBTimes.getSecond();
					}

					portTimesRecord.setSlotTime(buyOption, loadTime);
					portTimesRecord.setSlotTime(sellOption, dischargeTime);
					final List<@NonNull IPortSlot> recordSlots = portTimesRecord.getSlots();
					portTimesRecord.setSlotTime(recordSlots.get(0), loadTime);
					portTimesRecord.setSlotTime(recordSlots.get(recordSlots.size() - 1), dischargeTime);

					portTimesRecord.setSlotDuration(buyOption, durationProvider.getElementDuration(portSlotProvider.getElement(buyOption), resource));
					portTimesRecord.setSlotDuration(sellOption, durationProvider.getElementDuration(portSlotProvider.getElement(sellOption), resource));

//					portTimesRecord.setSlotExtraIdleTime(buyOption, 0);
//					portTimesRecord.setSlotExtraIdleTime(sellOption, 0);
				}
			}
		}
	}
}
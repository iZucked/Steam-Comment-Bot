/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.algorithm.IMullDischargeWrapper;
import com.mmxlabs.models.lng.adp.mull.algorithm.MullDesSalesMarketWrapper;
import com.mmxlabs.models.lng.adp.mull.algorithm.MullSalesContractWrapper;
import com.mmxlabs.models.lng.adp.mull.container.DesMarketTracker;
import com.mmxlabs.models.lng.adp.mull.container.IAllocationTracker;
import com.mmxlabs.models.lng.adp.mull.container.SalesContractTracker;
import com.mmxlabs.models.lng.adp.mull.profile.DesSalesMarketAllocationRow;
import com.mmxlabs.models.lng.adp.mull.profile.IAllocationRow;
import com.mmxlabs.models.lng.adp.mull.profile.SalesContractAllocationRow;
import com.mmxlabs.models.lng.fleet.Vessel;

@NonNullByDefault
public class MullUtil {
	private MullUtil() {
	}

	public static boolean isAtStartHourOfMonth(final LocalDateTime localDateTime) {
		return localDateTime.getDayOfMonth() == 1 && localDateTime.getHour() == 0;
	}

	public static boolean isAtEndHourOfMonth(final LocalDateTime localDateTime) {
		final YearMonth ym = YearMonth.from(localDateTime);
		final LocalDate lastDateOfMonth = ym.atEndOfMonth();
		return localDateTime.getDayOfMonth() == lastDateOfMonth.getDayOfMonth() && localDateTime.getHour() == 23;
	}

	public static int calculateVolumeLiftedBarSafetyHeel(final Vessel vessel, final int loadDuration) {
		final int expectedBoiloff = (int) (loadDuration * (vessel.getLadenAttributes().getVesselOrDelegateInPortNBORate() / 24.0));
		return expectedBoiloff + (int) (vessel.getVesselOrDelegateCapacity() * vessel.getVesselOrDelegateFillCapacity() - vessel.getVesselOrDelegateSafetyHeel());
	}

	public static IMullDischargeWrapper buildDischargeWrapper(final IAllocationRow allocationRow) {
		if (allocationRow instanceof final SalesContractAllocationRow salesContractAllocationRow) {
			return new MullSalesContractWrapper(salesContractAllocationRow.getSalesContract());
		} else if (allocationRow instanceof final DesSalesMarketAllocationRow desSalesMarketAllocationRow) {
			return new MullDesSalesMarketWrapper(desSalesMarketAllocationRow.getDesSalesMarket());
		} else {
			throw new IllegalStateException("Unexpected allocation row type");
		}
	}

	public static IMullDischargeWrapper buildDischargeWrapper(final IAllocationTracker allocationTracker) {
		if (allocationTracker instanceof final SalesContractTracker salesContractTracker) {
			return new MullSalesContractWrapper(salesContractTracker.getSalesContract());
		} else if (allocationTracker instanceof final DesMarketTracker desMarketTracker) {
			return new MullDesSalesMarketWrapper(desMarketTracker.getSalesMarket());
		} else {
			throw new IllegalStateException("Unexpected allocation tracker type");
		}
	}
}

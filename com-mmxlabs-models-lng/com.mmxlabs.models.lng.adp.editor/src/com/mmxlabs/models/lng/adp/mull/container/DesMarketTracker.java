/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.container;

import java.time.YearMonth;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.container.manipulation.IAllocationTrackerManipulationStrategy;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;

@NonNullByDefault
public class DesMarketTracker extends AbstractAllocationTracker {

	final DESSalesMarket salesMarket;

	public DesMarketTracker(final DESSalesMarket desSalesMarket, final int aacq, final List<Vessel> vesselList, final double relativeEntitlement,
			final IAllocationTrackerManipulationStrategy manipulationStrategy) {
		super(aacq, vesselList, relativeEntitlement, manipulationStrategy);
		this.salesMarket = desSalesMarket;
	}

	@Override
	public void updateCurrentMonthAllocations(final YearMonth nextMonth) {
		if (this.monthlyAllocations != null) {
			final int newMonthlyAllocation = this.monthlyAllocations.get(nextMonth);
			final int rollForwardAllocation = this.monthlyAllocation - this.currentMonthLifted;
			this.monthlyAllocation = newMonthlyAllocation - rollForwardAllocation;
			this.currentMonthLifted = 0;
		}
	}

	@Override
	public boolean matches(final Cargo cargo) {
		return (cargo.getSortedSlots().get(1) instanceof final SpotDischargeSlot spotDischargeSlot) && this.salesMarket.equals(spotDischargeSlot.getMarket());
	}

	@Override
	public boolean satisfiedMonthlyAllocation() {
		return this.monthlyAllocation <= this.currentMonthLifted;
	}

	@Override
	public boolean satisfiesAacq() {
		return manipulationStrategy.bucketSatisfiesAacq(this);
	}

	public DESSalesMarket getSalesMarket() {
		return this.salesMarket;
	}

}

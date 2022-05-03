/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.container;

import java.time.YearMonth;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.container.manipulation.IAllocationTrackerManipulationStrategy;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.Vessel;

@NonNullByDefault
public class SalesContractTracker extends AbstractAllocationTracker {

	private final SalesContract salesContract;

	public SalesContractTracker(final SalesContract salesContract, final int aacq, final List<Vessel> vesselList, final double relativeEntitlement,
			final IAllocationTrackerManipulationStrategy manipulationStrategy) {
		super(aacq, vesselList, relativeEntitlement, manipulationStrategy);
		this.salesContract = salesContract;
	}

	@Override
	public void updateCurrentMonthAllocations(final YearMonth nextMonth) {
		if (this.monthlyAllocations != null) {
			final int newMonthlyAllocation = this.monthlyAllocations.get(nextMonth);
			final int rollForwardAllocation = this.monthlyAllocation - this.currentMonthLifted;
			this.monthlyAllocation = newMonthlyAllocation + rollForwardAllocation;
			this.currentMonthLifted = 0;
		}
	}

	@Override
	public boolean matches(final Cargo cargo) {
		final Slot<?> dischargeSlot = cargo.getSlots().get(1);
		return !(dischargeSlot instanceof SpotDischargeSlot) && this.salesContract == dischargeSlot.getContract();
	}

	@Override
	public boolean satisfiedMonthlyAllocation() {
		return this.monthlyAllocation <= this.currentMonthLifted;
	}

	@Override
	public boolean satisfiesAacq() {
		return manipulationStrategy.satisfiesAacq(this);
	}

	public SalesContract getSalesContract() {
		return this.salesContract;
	}

}

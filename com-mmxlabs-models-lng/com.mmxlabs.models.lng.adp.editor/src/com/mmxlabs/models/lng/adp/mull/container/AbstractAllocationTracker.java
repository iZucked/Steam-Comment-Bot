/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.container;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.IAllocationTrackerManipulationStrategy;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.fleet.Vessel;

@NonNullByDefault
public abstract class AbstractAllocationTracker implements IAllocationTracker {

	protected final double relativeEntitlement;
	protected long runningAllocation = 0L;
	protected final List<Vessel> vesselList;
	protected boolean sharesVessels;
	protected final int aacq;
	protected int monthlyAllocation = 0;
	protected int currentAllocatedAacq = 0;
	protected int currentMonthLifted = 0;
	@Nullable
	protected Map<YearMonth, Integer> monthlyAllocations = null;

	protected final IAllocationTrackerManipulationStrategy manipulationStrategy;

	protected AbstractAllocationTracker(final int aacq, final List<Vessel> vesselList, final double relativeEntitlement, final IAllocationTrackerManipulationStrategy manipulationStrategy) {
		this.aacq = aacq;
		this.vesselList = vesselList;
		this.relativeEntitlement = relativeEntitlement;
		this.manipulationStrategy = manipulationStrategy;
	}

	@Override
	public void setMonthlyAllocations(final Map<YearMonth, Integer> monthlyAllocations) {
		this.monthlyAllocations = monthlyAllocations;
	}

	@Override
	public void setAllocatedAacq(final int currentAllocatedAacq) {
		this.currentAllocatedAacq = currentAllocatedAacq;
	}

	@Override
	public int getAacq() {
		return this.aacq;
	}

	@Override
	public int getCurrentAllocatedAacq() {
		return this.currentAllocatedAacq;
	}

	@Override
	public void updateRunningAllocation(long allocationToShare) {
		this.runningAllocation += ((Double) (allocationToShare * this.relativeEntitlement)).longValue();
	}

	@Override
	public long getRunningAllocation() {
		return this.runningAllocation;
	}



	@Override
	public int calculateExpectedAllocationDrop(final Map<Vessel, LocalDateTime> vesselToMostRecentUseDateTime,
			final Map<Vessel, @Nullable LocalDateTime> vesselToNextForwardUseTime, final int defaultAllocationDrop, final int loadDuration, final Set<Vessel> firstPartyVessels,
			final LocalDateTime currentDateTime) {
		if (vesselList.isEmpty()) {
			return defaultAllocationDrop;
		}
		final Vessel expectedVessel = this.vesselList.stream() //
				.max((v1, v2) -> {
					final LocalDateTime lookahead1 = vesselToNextForwardUseTime.get(v1);
					final LocalDateTime lookahead2 = vesselToNextForwardUseTime.get(v2);
					final LocalDateTime lookback1 = vesselToMostRecentUseDateTime.get(v1);
					final LocalDateTime lookback2 = vesselToMostRecentUseDateTime.get(v2);
					// Get minimum difference in hours
					final int hoursDiff1;
					final int hoursDiff2;
					if (lookahead1 != null) {
						hoursDiff1 = Math.min(Hours.between(lookback1, currentDateTime), Hours.between(currentDateTime, lookahead1));
					} else {
						hoursDiff1 = Hours.between(lookback1, currentDateTime);
					}
					if (lookahead2 != null) {
						hoursDiff2 = Math.min(Hours.between(lookback2, currentDateTime), Hours.between(currentDateTime, lookahead2));
					} else {
						hoursDiff2 = Hours.between(lookback2, currentDateTime);
					}
					// Compare hour differences - largest is vessel furthest from being used
					return Integer.compare(hoursDiff1, hoursDiff2);
				}) //
				.get();
		return calculateExpectedAllocationDrop(expectedVessel, loadDuration, firstPartyVessels.contains(expectedVessel));
	}

	@Override
	public int calculateExpectedAllocationDrop(final Vessel vessel, final int loadDuration, final boolean isSharedVessel) {
		final int expectedBoiloff = this.sharesVessels ? calculateExpectedBoiloff(vessel, loadDuration, isSharedVessel) : 0;
		return expectedBoiloff + (int) (vessel.getVesselOrDelegateCapacity() * vessel.getVesselOrDelegateFillCapacity() - vessel.getVesselOrDelegateSafetyHeel());
	}

	@Override
	public int calculateExpectedBoiloff(final Vessel vessel, final int loadDuration, final boolean isSharedVessel) {
		return manipulationStrategy.calculateExpectedBoiloff(vessel, loadDuration, this);
//		return (int) (loadDuration * (vessel.getLadenAttributes().getVesselOrDelegateInPortNBORate() / 24.0));
	}

	@Override
	public List<Vessel> getVessels() {
		return this.vesselList;
	}

	@Override
	public boolean isSharingVessels() {
		return this.sharesVessels;
	}

	@Override
	public void setVesselSharing(final Set<Vessel> vesselsToIntersect) {
		this.sharesVessels = this.vesselList.stream().anyMatch(vesselsToIntersect::contains);
	}

	@Override
	public void setVesselSharing(final boolean sharesVessels) {
		this.sharesVessels = sharesVessels;
	}

	@Override
	public void incrementCurrentAllocatedAacq() {
		++this.currentAllocatedAacq;
	}

	@Override
	public void decrementCurrentAllocatedAacq() {
		--this.currentAllocatedAacq;
	}

	@Override
	public void increaseRunningAllocation(final int amountToIncreaseBy) {
		this.runningAllocation += amountToIncreaseBy;
	}

	@Override
	public void decreaseRunningAllocation(int amountToDecreaseBy) {
		this.runningAllocation -= amountToDecreaseBy;
	}

	@Override
	public void dropFixedLoad(final int volumeLoaded) {
		this.runningAllocation -= volumeLoaded;
	}


	@Override
	public void incrementCurrentMonthLifted() {
		++this.currentMonthLifted;
	}

	@Override
	public void decrementCurrentMonthLifted() {
		--this.currentMonthLifted;
	}

	@Override
	public void dropAllocation(final long allocationDrop) {
		this.manipulationStrategy.dropAllocation(allocationDrop, this);
	}

	@Override
	public void undo(final ICargoBlueprint cargoBlueprint) {
		this.manipulationStrategy.undo(cargoBlueprint, this);
	}

	@Override
	public void dropFixedLoad(final Cargo cargo) {
		this.manipulationStrategy.dropFixedLoad(cargo, this);
	}

}

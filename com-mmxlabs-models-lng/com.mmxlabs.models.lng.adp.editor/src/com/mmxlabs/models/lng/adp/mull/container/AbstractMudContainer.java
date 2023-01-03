/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.container;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.common.collect.ImmutableList;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.IMudContainerManipulationStrategy;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.fleet.Vessel;

@NonNullByDefault
public abstract class AbstractMudContainer implements IMudContainer {

	protected final BaseLegalEntity entity;
	protected final double relativeEntitlement;
	protected final long initialAllocation;
	protected long runningAllocation;
	protected int currentMonthAbsoluteEntitlement;
	protected final IMudContainerManipulationStrategy mudContainerManipulationStrategy;
	protected final List<IAllocationTracker> allocationTrackers;
	protected final DesMarketTracker desMarketTracker;
	protected boolean metAllHardAacqs = false;

	protected AbstractMudContainer(final BaseLegalEntity entity, final double relativeEntitlement, final long initialAllocation, final List<IAllocationTracker> allocationTrackers, final IMudContainerManipulationStrategy mudContainerManipulationStrategy) {
		this.entity = entity;
		this.relativeEntitlement = relativeEntitlement;
		this.initialAllocation = initialAllocation;
		this.runningAllocation = this.initialAllocation;
		this.currentMonthAbsoluteEntitlement = (int) this.runningAllocation;
		this.mudContainerManipulationStrategy = mudContainerManipulationStrategy;
		if (allocationTrackers.isEmpty()) {
			throw new IllegalStateException("Allocation trackers list must be non-empty");
		}
		this.allocationTrackers = ImmutableList.copyOf(allocationTrackers);
		final Iterator<DesMarketTracker> iterDesMarketTrackers = this.allocationTrackers.stream().filter(DesMarketTracker.class::isInstance).map(DesMarketTracker.class::cast).iterator();
		if (!iterDesMarketTrackers.hasNext()) {
			throw new IllegalStateException("Allocation trackers must contain a DES market tracker");
		}
		desMarketTracker = iterDesMarketTrackers.next();
		if (iterDesMarketTrackers.hasNext()) {
			throw new IllegalStateException("Allocation trackers must contain at most one DES market tracker");
		}
	}
	
	@Override
	public BaseLegalEntity getEntity() {
		return this.entity;
	}

	@Override
	public double getRelativeEntitlement() {
		return this.relativeEntitlement;
	}

	@Override
	public long getInitialAllocation() {
		return this.initialAllocation;
	}

	@Override
	public void increaseAllocation(final int amountToIncreaseBy) {
		this.runningAllocation += amountToIncreaseBy;
		this.currentMonthAbsoluteEntitlement += amountToIncreaseBy;
	}

	@Override
	public void decreaseAllocation(int amountToDecreaseBy) {
		this.runningAllocation -= amountToDecreaseBy;
		this.currentMonthAbsoluteEntitlement -= amountToDecreaseBy;
	}

	@Override
	public void increaseRunningAllocation(final long allocationIncrease) {
		this.runningAllocation += allocationIncrease;
	}

	public void updateRunningAllocation(final Long additionalAllocation) {
		final long localAbsoluteEntitlement = ((Double) (this.relativeEntitlement * additionalAllocation)).longValue();
		this.runningAllocation += localAbsoluteEntitlement;
		this.allocationTrackers.forEach(tracker -> tracker.updateRunningAllocation(localAbsoluteEntitlement));
	};

	public void updateCurrentMonthAbsoluteEntitlement(final int monthInToShare) {
		this.currentMonthAbsoluteEntitlement += ((Double) (monthInToShare * this.relativeEntitlement)).intValue();
	};

	@Override
	public void dropAllocation(final long allocationDrop) {
		this.runningAllocation -= allocationDrop;
		this.currentMonthAbsoluteEntitlement -= (int) allocationDrop;
	}

	@Override
	public long getRunningAllocation() {
		return this.runningAllocation;
	}
	
	public int getCurrentMonthAbsoluteEntitlement() {
		return this.currentMonthAbsoluteEntitlement;
	};
	
	@Override
	public void undo(final ICargoBlueprint cargoBlueprint) {
		mudContainerManipulationStrategy.undo(cargoBlueprint, this);
	}

	@Override
	public void dropFixedLoad(final Cargo cargo) {
		mudContainerManipulationStrategy.dropFixedLoad(cargo, this);
	}

	@Override
	public void dropFixedLoad(final int loadedVolume) {
		this.runningAllocation -= loadedVolume;
		this.currentMonthAbsoluteEntitlement -= loadedVolume;
	};

	@Override
	public int calculateExpectedAllocationDrop(final Map<Vessel, LocalDateTime> vesselToMostRecentUseDateTime,
			final Map<Vessel, LocalDateTime> vesselToNextForwardUseTime, final int defaultAllocationDrop, final int loadDuration, final Set< Vessel> firstPartyVessels,
			final LocalDateTime currentDateTime) {
		final IAllocationTracker mudTracker = this.calculateMudAllocationTracker();
		return mudTracker.calculateExpectedAllocationDrop(vesselToMostRecentUseDateTime, vesselToNextForwardUseTime, defaultAllocationDrop, loadDuration, firstPartyVessels, currentDateTime);
	}

	@Override
	public void reassessAACQSatisfaction() {
		this.metAllHardAacqs = this.allocationTrackers.stream().allMatch(IAllocationTracker::satisfiesAacq);
	}

	@Override
	public List<IAllocationTracker> getAllocationTrackers() {
		return this.allocationTrackers;
	}

	@Override
	public boolean satisfiedMonthlyAllocation() {
		return allocationTrackers.stream().allMatch(IAllocationTracker::satisfiedMonthlyAllocation);
	}

	@Override
	public void updateCurrentMonthAllocations(final YearMonth nextMonth) {
		this.allocationTrackers.forEach(at -> at.updateCurrentMonthAllocations(nextMonth));
	}

	@Override
	public boolean hasMetAllHardAacqs() {
		return metAllHardAacqs;
	}

	@Override
	public DesMarketTracker getDesMarketTracker() {
		return this.desMarketTracker;
	}

	@Override
	public IAllocationTracker calculateMudAllocationTracker() {
		return this.mudContainerManipulationStrategy.calculateMudAllocationTracker(this);
	}
}

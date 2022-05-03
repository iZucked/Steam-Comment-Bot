/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.container;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.fleet.Vessel;

@NonNullByDefault
public interface IMudContainer {
	
	public BaseLegalEntity getEntity();
	
	public double getRelativeEntitlement();
	
	public long getInitialAllocation();
	
	public void updateRunningAllocation(final Long additionalAllocation);

	public void increaseAllocation(final int amountToIncreaseBy);

	public void decreaseAllocation(final int amountToDecreaseBy);

	public void increaseRunningAllocation(final long allocationIncrease);
	
	public void updateCurrentMonthAbsoluteEntitlement(final int monthInToShare);
	
	public void dropAllocation(final long allocationDrop);

	public int calculateExpectedAllocationDrop(final Map<Vessel, LocalDateTime> vesselToMostRecentUseDateTime, final Map<Vessel, LocalDateTime> vesselToNextForwardUseTime,
			final int defaultAllocationDrop, final int loadDuration, final Set<Vessel> firstPartyVessels, final LocalDateTime currentDateTime);
	
	public long getRunningAllocation();
	
	public int getCurrentMonthAbsoluteEntitlement();
	
	public void undo(final ICargoBlueprint cargoBlueprint);
	
	public void dropFixedLoad(final Cargo cargo);

	public void dropFixedLoad(final int loadedVolume);

	public IAllocationTracker calculateMudAllocationTracker();

	public void reassessAACQSatisfaction();

	public List<IAllocationTracker> getAllocationTrackers();

	public boolean satisfiedMonthlyAllocation();

	public boolean hasMetAllHardAacqs();

	public void updateCurrentMonthAllocations(final YearMonth nextMonth);

	public DesMarketTracker getDesMarketTracker();
}

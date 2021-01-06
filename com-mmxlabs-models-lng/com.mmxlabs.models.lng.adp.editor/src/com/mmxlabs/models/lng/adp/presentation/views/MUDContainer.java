package com.mmxlabs.models.lng.adp.presentation.views;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import com.mmxlabs.models.lng.adp.DESSalesMarketAllocationRow;
import com.mmxlabs.models.lng.adp.MullEntityRow;
import com.mmxlabs.models.lng.adp.SalesContractAllocationRow;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.fleet.Vessel;


public class MUDContainer {
	private final BaseLegalEntity entity;
	private long runningAllocation;
	private final double relativeEntitlement;
	private int currentMonthAbsoluteEntitlement;
	private final List<AllocationTracker> allocationTrackers = new LinkedList<>();
	
	public MUDContainer(final MullEntityRow entityRow, final double allEntitiesWeight) {
		this.relativeEntitlement = entityRow.getRelativeEntitlement()/allEntitiesWeight;
		this.runningAllocation = Long.parseLong(entityRow.getInitialAllocation());
		this.currentMonthAbsoluteEntitlement = (int) this.runningAllocation;
		this.entity = entityRow.getEntity();
		double totalWeight = IntStream.concat(
				entityRow.getSalesContractAllocationRows().stream().mapToInt(SalesContractAllocationRow::getWeight),
				entityRow.getDesSalesMarketAllocationRows().stream().mapToInt(DESSalesMarketAllocationRow::getWeight)
			).sum();
		
		entityRow.getSalesContractAllocationRows().forEach(row -> allocationTrackers.add(new SalesContractTracker(row, totalWeight)));
		entityRow.getDesSalesMarketAllocationRows().forEach(row -> allocationTrackers.add(new DESMarketTracker(row, totalWeight)));
	}
	
	public BaseLegalEntity getEntity() {
		return this.entity;
	}
	
	public void updateRunningAllocation(final Long additionalAllocation) {
		final long localAbsoluteEntitlement = ((Double) (this.relativeEntitlement*additionalAllocation)).longValue();
		this.runningAllocation += localAbsoluteEntitlement;
		this.allocationTrackers.forEach(tracker -> tracker.updateRunningAllocation(localAbsoluteEntitlement));
	}
	
	public void updateCurrentMonthAbsoluteEntitlement(final int monthInToShare) {
		this.currentMonthAbsoluteEntitlement += ((Double) (monthInToShare*this.relativeEntitlement)).intValue();
	}
	
	public AllocationTracker calculateMUDAllocationTracker() {
		return this.allocationTrackers.stream().max((t1, t2) -> Long.compare(t1.getRunningAllocation(), t2.getRunningAllocation())).get();
	}
	
	public void dropAllocation(final long allocationDrop) {
		this.runningAllocation -= allocationDrop;
		this.currentMonthAbsoluteEntitlement -= (int) allocationDrop;
	}
	
	public int calculateExpectedAllocationDrop(final Map<Vessel, LocalDateTime> vesselToMostRecentUseDateTime, final int defaultAllocationDrop, final int loadDuration) {
		final AllocationTracker mudTracker = this.calculateMUDAllocationTracker();
		return mudTracker.calculateExpectedAllocationDrop(vesselToMostRecentUseDateTime, defaultAllocationDrop, loadDuration);
	}
	
	public List<AllocationTracker> getAllocationTrackers() {
		return this.allocationTrackers;
	}
	
	public long getRunningAllocation() {
		return this.runningAllocation;
	}
	
	public int getCurrentMonthAbsoluteEntitlement() {
		return this.currentMonthAbsoluteEntitlement;
	}
}
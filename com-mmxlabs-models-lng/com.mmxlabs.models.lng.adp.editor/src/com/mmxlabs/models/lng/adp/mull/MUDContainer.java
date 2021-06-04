/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import com.mmxlabs.models.lng.adp.MullAllocationRow;
import com.mmxlabs.models.lng.adp.MullEntityRow;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.fleet.Vessel;

public class MUDContainer {
	private final BaseLegalEntity entity;
	private long runningAllocation;
	private final long initialAllocation;
	private final double relativeEntitlement;
	private int currentMonthAbsoluteEntitlement;
	private final List<AllocationTracker> allocationTrackers = new LinkedList<>();
	private boolean metAllHardAACQs = false;
	private final DESMarketTracker desMarketTracker;

	public MUDContainer(final MullEntityRow entityRow, final double allEntitiesWeight) {
		this.relativeEntitlement = entityRow.getRelativeEntitlement() / allEntitiesWeight;
		this.initialAllocation = Long.parseLong(entityRow.getInitialAllocation());
		this.runningAllocation = this.initialAllocation;
		this.currentMonthAbsoluteEntitlement = (int) this.runningAllocation;
		this.entity = entityRow.getEntity();

		double totalWeight = Stream
				.concat(entityRow.getSalesContractAllocationRows().stream().map(MullAllocationRow.class::cast), entityRow.getDesSalesMarketAllocationRows().stream().map(MullAllocationRow.class::cast)) //
				.mapToDouble(row -> row.getWeight() * row.getVessels().stream().mapToInt(Vessel::getVesselOrDelegateCapacity).sum() / ((double) row.getVessels().size())) //
				.sum();

		entityRow.getSalesContractAllocationRows().forEach(row -> allocationTrackers.add(new SalesContractTracker(row, totalWeight)));
		entityRow.getDesSalesMarketAllocationRows().forEach(row -> allocationTrackers.add(new DESMarketTracker(row, totalWeight)));
		final Optional<DESMarketTracker> optDesMarketTracker = allocationTrackers.stream().filter(DESMarketTracker.class::isInstance).map(DESMarketTracker.class::cast).findAny();
		if (optDesMarketTracker.isPresent()) {
			desMarketTracker = optDesMarketTracker.get();
		} else {
			throw new IllegalStateException("All entity data must contain a DES Sales Market entry");
		}
		reassessAACQSatisfaction();
	}

	public BaseLegalEntity getEntity() {
		return this.entity;
	}

	public double getRelativeEntitlement() {
		return this.relativeEntitlement;
	}

	public long getInitialAllocation() {
		return this.initialAllocation;
	}

	public void updateRunningAllocation(final Long additionalAllocation) {
		final long localAbsoluteEntitlement = ((Double) (this.relativeEntitlement * additionalAllocation)).longValue();
		this.runningAllocation += localAbsoluteEntitlement;
		this.allocationTrackers.forEach(tracker -> tracker.updateRunningAllocation(localAbsoluteEntitlement));
	}

	public void updateCurrentMonthAbsoluteEntitlement(final int monthInToShare) {
		this.currentMonthAbsoluteEntitlement += ((Double) (monthInToShare * this.relativeEntitlement)).intValue();
	}

	public AllocationTracker calculateMUDAllocationTracker() {
		return this.allocationTrackers.stream().max((t1, t2) -> Long.compare(t1.getRunningAllocation(), t2.getRunningAllocation())).get();
	}

	public AllocationTracker phase1CalculateMUDAllocationTracker() {
		return metAllHardAACQs ? this.desMarketTracker : this.allocationTrackers.stream().max((t1, t2) -> {
			if (t1.satisfiedAACQ()) {
				return -1;
			} else if (t2.satisfiedAACQ()) {
				return 1;
			} else {
				return Long.compare(t1.getRunningAllocation(), t2.getRunningAllocation());
			}
		}).get();
	}

	public AllocationTracker phase2CalculateMUDAllocationTracker() {
		return metAllHardAACQs ? this.desMarketTracker : this.allocationTrackers.stream().max((t1, t2) -> {
			if (t1.phase2SatisfiedAACQ()) {
				return -1;
			} else if (t2.phase2SatisfiedAACQ()) {
				return 1;
			} else {
				return Long.compare(t1.getRunningAllocation(), t2.getRunningAllocation());
			}
		}).get();
	}

	public void dropAllocation(final long allocationDrop) {
		this.runningAllocation -= allocationDrop;
		this.currentMonthAbsoluteEntitlement -= (int) allocationDrop;
	}

	public void reassessAACQSatisfaction() {
		this.metAllHardAACQs = allocationTrackers.stream().allMatch(AllocationTracker::satisfiedAACQ);
	}

	public int calculateExpectedAllocationDrop(final Map<Vessel, LocalDateTime> vesselToMostRecentUseDateTime, final int defaultAllocationDrop, final int loadDuration, final Set<Vessel> firstPartyVessels) {
		final AllocationTracker mudTracker = this.calculateMUDAllocationTracker();
		return mudTracker.calculateExpectedAllocationDrop(vesselToMostRecentUseDateTime, defaultAllocationDrop, loadDuration, firstPartyVessels);
	}

	public int phase1CalculateExpectedAllocationDrop(final Map<Vessel, LocalDateTime> vesselToMostRecentUseDateTime, final int defaultAllocationDrop, final int loadDuration, final Set<Vessel> firstPartyVessels) {
		final AllocationTracker mudTracker = this.phase1CalculateMUDAllocationTracker();
		return mudTracker.calculateExpectedAllocationDrop(vesselToMostRecentUseDateTime, defaultAllocationDrop, loadDuration, firstPartyVessels);
	}

	public int phase2CalculateExpectedAllocationDrop(final Map<Vessel, LocalDateTime> vesselToMostRecentUseDateTime, final int defaultAllocationDrop, final int loadDuration, final Set<Vessel> firstPartyVessels) {
		final AllocationTracker mudTracker = this.phase2CalculateMUDAllocationTracker();
		return mudTracker.calculateExpectedAllocationDrop(vesselToMostRecentUseDateTime, defaultAllocationDrop, loadDuration, firstPartyVessels);
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

	public void undo(final CargoBlueprint cargoBlueprint) {
		if (cargoBlueprint.getEntity().equals(this.entity)) {
			this.runningAllocation += cargoBlueprint.getAllocatedVolume();
			this.currentMonthAbsoluteEntitlement += cargoBlueprint.getAllocatedVolume();
			for (final AllocationTracker allocationTracker : allocationTrackers) {
				allocationTracker.undo(cargoBlueprint);
			}
		}
	}

	public void phase1Undo(final CargoBlueprint cargoBlueprint) {
		if (cargoBlueprint.getEntity().equals(this.entity)) {
			this.runningAllocation += cargoBlueprint.getAllocatedVolume();
			this.currentMonthAbsoluteEntitlement += cargoBlueprint.getAllocatedVolume();
			for (final AllocationTracker allocationTracker : allocationTrackers) {
				allocationTracker.phase1Undo(cargoBlueprint);
			}
			reassessAACQSatisfaction();
		}
	}

	public void phase2Undo(final CargoBlueprint cargoBlueprint) {
		if (cargoBlueprint.getEntity().equals(this.entity)) {
			this.runningAllocation += cargoBlueprint.getAllocatedVolume();
			this.currentMonthAbsoluteEntitlement += cargoBlueprint.getAllocatedVolume();
			for (final AllocationTracker allocationTracker : allocationTrackers) {
				allocationTracker.phase2Undo(cargoBlueprint);
			}
			reassessAACQSatisfaction();
		}
	}

	public void dropFixedLoad(final Cargo cargo) {
		final Slot<?> loadSlot = cargo.getSlots().get(0);
		if (this.entity.equals(loadSlot.getEntity())) {
			final int expectedVolumeLoaded = loadSlot.getSlotOrDelegateMaxQuantity();
			this.runningAllocation -= expectedVolumeLoaded;
			this.currentMonthAbsoluteEntitlement -= expectedVolumeLoaded;
			for (final AllocationTracker allocationTracker : this.allocationTrackers) {
				allocationTracker.dropFixedLoad(cargo);
			}
		}
	}

	public void phase1DropFixedLoad(final Cargo cargo) {
		final Slot<?> loadSlot = cargo.getSlots().get(0);
		if (this.entity.equals(loadSlot.getEntity())) {
			final int expectedVolumeLoaded = loadSlot.getSlotOrDelegateMaxQuantity();
			this.runningAllocation -= expectedVolumeLoaded;
			this.currentMonthAbsoluteEntitlement -= expectedVolumeLoaded;
			for (final AllocationTracker allocationTracker : this.allocationTrackers) {
				allocationTracker.phase1DropFixedLoad(cargo);
			}
			reassessAACQSatisfaction();
		}
	}

	public void phase2DropFixedLoad(final Cargo cargo) {
		final Slot<?> loadSlot = cargo.getSlots().get(0);
		if (this.entity.equals(loadSlot.getEntity())) {
			final int expectedVolumeLoaded = loadSlot.getSlotOrDelegateMaxQuantity();
			this.runningAllocation -= expectedVolumeLoaded;
			this.currentMonthAbsoluteEntitlement -= expectedVolumeLoaded;
			for (final AllocationTracker allocationTracker : this.allocationTrackers) {
				allocationTracker.phase2DropFixedLoad(cargo);
			}
			reassessAACQSatisfaction();
		}
	}

	public void dropFixedLoad(final int loadedVolume) {
		this.runningAllocation -= loadedVolume;
		this.currentMonthAbsoluteEntitlement -= loadedVolume;
	}

}
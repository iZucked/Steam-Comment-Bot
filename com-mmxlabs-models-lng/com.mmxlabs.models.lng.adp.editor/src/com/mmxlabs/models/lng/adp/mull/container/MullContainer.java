/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.container;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.common.collect.ImmutableList;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.IMullComparatorWrapper;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Inventory;

@NonNullByDefault
public class MullContainer implements IMullContainer {

	private Inventory inventory;
	private final List<IMudContainer> mudContainers;
	private final IMullComparatorWrapper comparatorWrapper;

	public MullContainer(final Inventory inventory, final List<IMudContainer> mudContainers, final IMullComparatorWrapper comparatorWrapper) {
		this.inventory = inventory;
		if (mudContainers.isEmpty()) {
			throw new IllegalStateException("Mud containers must not be empty");
		}
		this.mudContainers = ImmutableList.copyOf(mudContainers);
		this.comparatorWrapper = comparatorWrapper;
	}

	@Override
	public void updateRunningAllocation(final int volumeIn) {
		this.mudContainers.stream().forEach(mc -> mc.updateRunningAllocation((long) volumeIn));
	}

	@Override
	public void updateCurrentMonthAbsoluteEntitlement(final int totalMonthIn) {
		this.mudContainers.stream().forEach(mc -> mc.updateCurrentMonthAbsoluteEntitlement(totalMonthIn));
	}

	@Override
	public void updateCurrentMonthAllocations(final YearMonth nextMonth) {
		this.mudContainers.forEach(mc -> mc.updateCurrentMonthAllocations(nextMonth));
	}

	@Override
	public void undo(final ICargoBlueprint cargoBlueprint) {
		this.mudContainers.forEach(mc -> mc.undo(cargoBlueprint));
	}

	@Override
	public void dropFixedLoad(final Cargo cargo) {
		this.mudContainers.forEach(mc -> mc.dropFixedLoad(cargo));
	}

	@Override
	public Inventory getInventory() {
		return this.inventory;
	}

	@Override
	public IMudContainer calculateMull(final LocalDateTime currentDateTime) {
		return this.mudContainers.stream().max(this.comparatorWrapper.getComparator(currentDateTime)).get();
	}

	@Override
	public List<IMudContainer> getMudContainers() {
		return this.mudContainers;
	}
}

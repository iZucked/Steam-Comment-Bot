/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.container;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Inventory;

@NonNullByDefault
public interface IMullContainer {

	public IMudContainer calculateMull(final LocalDateTime currentDateTime);

	public void updateRunningAllocation(final int volumeIn);

	public void updateCurrentMonthAbsoluteEntitlement(final int totalMonthIn);

	public void updateCurrentMonthAllocations(final YearMonth nextMonth);

	public List<IMudContainer> getMudContainers();

	public void undo(final ICargoBlueprint cargoBlueprint);

	public void dropFixedLoad(final Cargo cargo);

	public Inventory getInventory();
}

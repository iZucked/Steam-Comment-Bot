/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull.algorithm;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.adp.mull.InventoryDateTimeEvent;
import com.mmxlabs.models.lng.adp.mull.MullUtil;
import com.mmxlabs.models.lng.adp.mull.container.CargoBlueprint;
import com.mmxlabs.models.lng.adp.mull.container.IAllocationTracker;
import com.mmxlabs.models.lng.adp.mull.container.ICargoBlueprint;
import com.mmxlabs.models.lng.adp.mull.container.IMudContainer;
import com.mmxlabs.models.lng.adp.mull.container.IMullContainer;
import com.mmxlabs.models.lng.fleet.Vessel;

@NonNullByDefault
public abstract class MullAlgorithm implements IMullAlgorithm {

	protected final GlobalStatesContainer globalStatesContainer;
	protected final AlgorithmState algorithmState;
	protected final List<InventoryLocalState> inventoryLocalStates;

	protected MullAlgorithm(final GlobalStatesContainer globalStatesContainer, final AlgorithmState algorithmState, final List<InventoryLocalState> inventoryLocalStates) {
		this.globalStatesContainer = globalStatesContainer;
		this.algorithmState = algorithmState;
		this.inventoryLocalStates = inventoryLocalStates;
	}

	@Override
	public void run() {
		if (!inventoryLocalStates.stream().map(InventoryLocalState::getRollingLoadWindow).map(IRollingWindow::getStartDateTime)
				.allMatch(globalStatesContainer.getAdpGlobalState().getStartDateTime()::equals)) {
			throw new IllegalStateException("All inventories should start on ADP start date");
		}
		for (LocalDateTime currentDateTime = globalStatesContainer.getAdpGlobalState().getStartDateTime(); currentDateTime
				.isBefore(globalStatesContainer.getAdpGlobalState().getEndDateTimeExclusive()); currentDateTime = currentDateTime.plusHours(1L)) {
			for (final InventoryLocalState inventoryLocalState : inventoryLocalStates) {
				assert currentDateTime.equals(inventoryLocalState.getRollingLoadWindow().getStartDateTime());
				final InventoryDateTimeEvent currentEvent = inventoryLocalState.getRollingLoadWindow().getCurrentEvent();
				inventoryLocalState.getMullContainer().updateRunningAllocation(currentEvent.getNetVolumeIn());

				if (MullUtil.isAtStartHourOfMonth(currentDateTime)) {
					runStartOfMonthTasks(currentDateTime, inventoryLocalState);
				}

				runFixedCargoHandlingTasks(currentDateTime, inventoryLocalState);

				if (!inventoryLocalState.getRollingLoadWindow().isLoading()) {
					final IMudContainer mudContainer = inventoryLocalState.getMullContainer().calculateMull(currentDateTime);
					final IAllocationTracker mudAllocationTracker = mudContainer.calculateMudAllocationTracker();
					final Pair<@Nullable Vessel, Integer> pair = getVesselAndAllocationDrop(currentDateTime, mudAllocationTracker, inventoryLocalState);
					final int allocationDrop = pair.getSecond();
					if (inventoryLocalState.getRollingLoadWindow().canLift(allocationDrop)) {
						@Nullable
						final Vessel assignedVessel = pair.getFirst();
						inventoryLocalState.getRollingLoadWindow().startLoad(allocationDrop);
						mudContainer.dropAllocation(allocationDrop);
						mudAllocationTracker.dropAllocation(allocationDrop);
						runPostAllocationDropTasks(inventoryLocalState.getMullContainer(), mudContainer, mudAllocationTracker);

						final CargoBlueprint cargoBlueprint = new CargoBlueprint(inventoryLocalState.getInventorySlotCounter(), inventoryLocalState.getMullContainer(), mudContainer,
								mudAllocationTracker, assignedVessel, allocationDrop, currentDateTime, globalStatesContainer.getMullGlobalState().getLoadWindowInHours());
						if (!inventoryLocalState.getCargoBlueprintsToGenerate().isEmpty()) {
							final ICargoBlueprint previousCargoBlueprint = inventoryLocalState.getCargoBlueprintsToGenerate().getLast();
							final LocalDateTime earliestPreviousStart = inventoryLocalState.getRollingLoadWindow().getStartDateTime()
									.minusHours(inventoryLocalState.getInventoryGlobalState().getLoadDuration());
							final int newWindowHours = Math.max(0, Hours.between(previousCargoBlueprint.getWindowStart(), earliestPreviousStart));
							previousCargoBlueprint.updateWindowSize(newWindowHours);
						}
						inventoryLocalState.getCargoBlueprintsToGenerate().add(cargoBlueprint);
						inventoryLocalState.incrementInventorySlotCounter();
						if (assignedVessel != null) {
							algorithmState.getVesselUsageLookBehind().put(assignedVessel, currentDateTime);
						}
					}
				}
				inventoryLocalState.getRollingLoadWindow().stepForward();
			}
		}
	}

	@Override
	public List<InventoryLocalState> getInventoryLocalStates() {
		return this.inventoryLocalStates;
	}

	@Override
	public AlgorithmState getAlgorithmState() {
		return this.algorithmState;
	}

	@Override
	public GlobalStatesContainer getGlobalStatesContainer() {
		return this.globalStatesContainer;
	}

	protected Pair<@Nullable Vessel, Integer> getVesselAndAllocationDrop(final LocalDateTime currentDateTime, final IAllocationTracker allocationTracker, final InventoryLocalState inventoryLocalState) {
		final List<Vessel> vessels = allocationTracker.getVessels();
		final int currentAllocationDrop;
		@Nullable
		final Vessel assignedVessel;
		if (!vessels.isEmpty()) {
			assignedVessel = calculateAssignedVessel(currentDateTime, vessels);
			currentAllocationDrop = allocationTracker.calculateExpectedAllocationDrop(assignedVessel, inventoryLocalState.getInventoryGlobalState().getLoadDuration(),
					algorithmState.getFirstPartyVessels().contains(assignedVessel));
		} else {
			assignedVessel = null;
			currentAllocationDrop = globalStatesContainer.mullGlobalState.getCargoVolume();
		}
		return Pair.of(assignedVessel, currentAllocationDrop);
	}

	protected void runPostAllocationDropTasks(final IMullContainer mullContainer, final IMudContainer mudContainer, final IAllocationTracker allocationTracker) {
	}

	protected void runPostInnerLoopTasks(final LocalDateTime currentDateTime, final IMullContainer mullContainer) {
	}

	protected void runPostMonthStartTasks(final LocalDateTime currentDateTime, final IMullContainer mullContainer) {
	}

	protected void runFixedCargoHandlingTasks(final LocalDateTime currentDateTime, final InventoryLocalState inventoryLocalState) {
	}

	protected Vessel calculateAssignedVessel(final LocalDateTime currentDateTime, final List<Vessel> vessels) {
		final Map<Vessel, LocalDateTime> vesselLookBehind = algorithmState.getVesselUsageLookBehind();
		return vessels.stream() //
				.min((v1, v2) -> vesselLookBehind.get(v1).compareTo(vesselLookBehind.get(v2))) //
				.get();
	}
	
	
	protected void runStartOfMonthTasks(final LocalDateTime currentDateTime, final InventoryLocalState inventoryLocalState) {
		final int monthIn = inventoryLocalState.getInventoryGlobalState().getProductionInMonth(YearMonth.from(currentDateTime));
		inventoryLocalState.getMullContainer().updateCurrentMonthAbsoluteEntitlement(monthIn);
		runPostMonthStartTasks(currentDateTime, inventoryLocalState.getMullContainer());
	}
}
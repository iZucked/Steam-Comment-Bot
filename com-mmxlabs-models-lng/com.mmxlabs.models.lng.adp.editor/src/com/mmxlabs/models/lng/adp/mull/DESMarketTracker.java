/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;

import com.mmxlabs.models.lng.adp.DESSalesMarketAllocationRow;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.CargoEditingCommands;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.types.FOBSaleDealType;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class DESMarketTracker extends AllocationTracker {
	final DESSalesMarket salesMarket;

	public DESMarketTracker(final DESSalesMarketAllocationRow allocationRow, final double totalWeight) {
		super(allocationRow, totalWeight);
		this.salesMarket = allocationRow.getDesSalesMarket();
	}

	@Override
	public int calculateExpectedBoiloff(final Vessel vessel, final int loadDuration, final boolean isSharedVessel) {
		return this.sharesVessels ? super.calculateExpectedBoiloff(vessel, loadDuration, isSharedVessel) : 0;
	}

	public DESSalesMarket getDESSalesMarket() {
		return this.salesMarket;
	}

	@Override
	public void phase2DropAllocation(long allocationDrop) {
		super.phase2DropAllocation(allocationDrop);
		++currentAllocatedAACQ;
	}

	@Override
	public void finalPhaseDropAllocation(long allocationDrop) {
		super.finalPhaseDropAllocation(allocationDrop);
		++currentAllocatedAACQ;
	}

	@Override
	public void phase2Undo(CargoBlueprint cargoBlueprint) {
		super.phase2Undo(cargoBlueprint);
		if (this.equals(cargoBlueprint.getAllocationTracker())) {
			--currentAllocatedAACQ;
		}
	}

	@Override
	public void harmonisationPhaseUndo(CargoBlueprint cargoBlueprint) {
		super.harmonisationPhaseUndo(cargoBlueprint);
		if (this.equals(cargoBlueprint.getAllocationTracker())) {
			--currentAllocatedAACQ;
			--this.currentMonthLifted;
		}
	}

	@Override
	public boolean satisfiedMonthlyAllocation() {
		return this.monthlyAllocation <= this.currentMonthLifted;
	}

	@Override
	public DischargeSlot createDischargeSlot(final CargoEditingCommands cec, List<Command> setCommands, final CargoModel cargoModel, final IScenarioDataProvider sdp, final LoadSlot loadSlot,
			final Vessel vessel, final Map<Vessel, VesselAvailability> vesselToVA, final LNGScenarioModel sm, final Set<Vessel> firstPartyVessels) {
		final DischargeSlot dischargeSlot;
		if (firstPartyVessels.contains(vessel)) {
			dischargeSlot = cec.createNewSpotDischarge(setCommands, cargoModel, this.salesMarket);
			final LocalDate dischargeDate = AllocationTracker.calculateDischargeDate(loadSlot, dischargeSlot, vessel, sdp, vesselToVA, sm);
			dischargeSlot.setWindowStart(dischargeDate);
			dischargeSlot.setWindowStartTime(0);
			dischargeSlot.setWindowSize(1);
			dischargeSlot.setWindowSizeUnits(TimePeriod.MONTHS);
			// if (!this.vesselList.isEmpty()) {
			// dischargeSlot.setRestrictedVesselsArePermissive(true);
			// dischargeSlot.setRestrictedVesselsOverride(true);
			// dischargeSlot.getRestrictedVessels().addAll(this.vesselList);
			// }
			// dischargeSlot.setRestrictedPortsArePermissive(true);
			// dischargeSlot.setRestrictedPortsOverride(true);
			// dischargeSlot.getRestrictedPorts().add(loadSlot.getPort());
			final String id = String.format("market-%s", loadSlot.getName());
			dischargeSlot.setName(id);
		} else {
			dischargeSlot = cec.createNewDischarge(setCommands, cargoModel, true);
			dischargeSlot.setWindowStart(loadSlot.getWindowStart());
			dischargeSlot.setWindowStartTime(0);
			dischargeSlot.setWindowSize(1);
			dischargeSlot.setWindowSizeUnits(TimePeriod.MONTHS);
			final String id = String.format("fob-sale-%s", loadSlot.getName());
			dischargeSlot.setName(id);
			dischargeSlot.setPriceExpression("JKM");
			dischargeSlot.setPort(loadSlot.getPort());
			dischargeSlot.setFobSaleDealType(FOBSaleDealType.SOURCE_ONLY);
			dischargeSlot.setEntity(loadSlot.getEntity());
			dischargeSlot.setName(id);
			if (vessel != null)
				dischargeSlot.setNominatedVessel(vessel);
		}
		return dischargeSlot;
	}

	@Override
	public void dropFixedLoad(final Cargo cargo) {
		final Slot<?> dischargeSlot = cargo.getSlots().get(1);
		if (dischargeSlot instanceof SpotDischargeSlot) {
			final SpotDischargeSlot spotDischargeSlot = (SpotDischargeSlot) dischargeSlot;
			if (salesMarket.equals(spotDischargeSlot.getMarket())) {
				final int expectedVolumeLoaded = cargo.getSlots().get(0).getSlotOrDelegateMaxQuantity();
				this.runningAllocation -= expectedVolumeLoaded;
			}
		}
	}

	@Override
	public boolean satisfiedAACQ() {
		return true;
	}

	@Override
	public void phase1DropFixedLoad(Cargo cargo) {
		dropFixedLoad(cargo);
	}

	@Override
	public void phase2DropFixedLoad(Cargo cargo) {
		final Slot<?> dischargeSlot = cargo.getSlots().get(1);
		if (dischargeSlot instanceof SpotDischargeSlot) {
			final SpotDischargeSlot spotDischargeSlot = (SpotDischargeSlot) dischargeSlot;
			if (this.salesMarket.equals(spotDischargeSlot.getMarket())) {
				final int expectedVolumeLoaded = cargo.getSlots().get(0).getSlotOrDelegateMaxQuantity();
				this.runningAllocation -= expectedVolumeLoaded;
			}
		}
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
}

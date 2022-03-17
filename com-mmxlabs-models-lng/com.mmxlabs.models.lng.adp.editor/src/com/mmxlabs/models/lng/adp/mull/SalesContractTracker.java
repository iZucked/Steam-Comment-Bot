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
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.adp.SalesContractAllocationRow;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.CargoEditingCommands;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class SalesContractTracker extends AllocationTracker {
	private final SalesContract salesContract;

	public SalesContractTracker(final SalesContractAllocationRow allocationRow, final double totalWeight) {
		super(allocationRow, totalWeight);
		this.salesContract = allocationRow.getContract();
	}

	public SalesContract getSalesContract() {
		return this.salesContract;
	}

	@Override
	public void phase1DropAllocation(long allocationDrop) {
		super.phase1DropAllocation(allocationDrop);
		++currentAllocatedAACQ;
	}

	@Override
	public void phase1Undo(CargoBlueprint cargoBlueprint) {
		super.phase1Undo(cargoBlueprint);
		if (this.equals(cargoBlueprint.getAllocationTracker())) {
			--currentAllocatedAACQ;
		}
	}

	@Override
	public void phase2DropAllocation(long allocationDrop) {
		super.phase2DropAllocation(allocationDrop);
		++currentAllocatedAACQ;
	}

	@Override
	public void phase2Undo(CargoBlueprint cargoBlueprint) {
		super.phase1Undo(cargoBlueprint);
		if (this.equals(cargoBlueprint.getAllocationTracker())) {
			--currentAllocatedAACQ;
		}
	}

	@Override
	public void finalPhaseDropAllocation(long allocationDrop) {
		super.finalPhaseDropAllocation(allocationDrop);
		++currentAllocatedAACQ;
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
	public boolean satisfiedAACQ() {
		return this.aacq == currentAllocatedAACQ;
	}

	@Override
	public boolean satisfiedMonthlyAllocation() {
		return this.monthlyAllocation <= this.currentMonthLifted;
	}

	@Override
	public DischargeSlot createDischargeSlot(final CargoEditingCommands cec, List<Command> setCommands, final CargoModel cargoModel, @NonNull final IScenarioDataProvider sdp, final LoadSlot loadSlot,
			final Vessel vessel, final Map<Vessel, VesselAvailability> vesselToVA, final LNGScenarioModel sm, final Set<Vessel> firstPartyVessels) {
		final DischargeSlot dischargeSlot = cec.createNewDischarge(setCommands, cargoModel, false);
		dischargeSlot.setContract(this.salesContract);
		dischargeSlot.setEntity(loadSlot.getEntity());
		dischargeSlot.setPort(salesContract.getPreferredPort());
		final LocalDate dischargeDate = AllocationTracker.calculateDischargeDate(loadSlot, dischargeSlot, vessel, sdp, vesselToVA, sm);
		dischargeSlot.setWindowStart(dischargeDate);
		dischargeSlot.setWindowStartTime(0);
		dischargeSlot.setWindowSize(1);
		dischargeSlot.setWindowSizeUnits(TimePeriod.MONTHS);

		if (!this.vesselList.isEmpty()) {
			dischargeSlot.setRestrictedVesselsArePermissive(true);
			dischargeSlot.setRestrictedVesselsOverride(true);
			dischargeSlot.getRestrictedVessels().addAll(this.vesselList);
		}

		dischargeSlot.setRestrictedPortsArePermissive(true);
		dischargeSlot.setRestrictedPortsOverride(true);
		dischargeSlot.getRestrictedPorts().add(loadSlot.getPort());

		final String id = String.format("des-sale-%s", loadSlot.getName());
		dischargeSlot.setName(id);

		return dischargeSlot;
	}

	@Override
	public void dropFixedLoad(Cargo cargo) {
		final Slot<?> dischargeSlot = cargo.getSlots().get(1);
		if (!(dischargeSlot instanceof SpotDischargeSlot)) {
			if (this.salesContract.equals(dischargeSlot.getContract())) {
				final int expectedVolumeLoaded = cargo.getSlots().get(0).getSlotOrDelegateMaxQuantity();
				this.runningAllocation -= expectedVolumeLoaded;
			}
		}
	}

	@Override
	public void phase1DropFixedLoad(Cargo cargo) {
		final Slot<?> dischargeSlot = cargo.getSlots().get(1);
		if (!(dischargeSlot instanceof SpotDischargeSlot)) {
			if (this.salesContract.equals(dischargeSlot.getContract())) {
				final int expectedVolumeLoaded = cargo.getSlots().get(0).getSlotOrDelegateMaxQuantity();
				this.runningAllocation -= expectedVolumeLoaded;
				++currentAllocatedAACQ;
			}
		}
	}

	@Override
	public void phase2DropFixedLoad(Cargo cargo) {
		final Slot<?> dischargeSlot = cargo.getSlots().get(1);
		if (!(dischargeSlot instanceof SpotDischargeSlot)) {
			if (this.salesContract.equals(dischargeSlot.getContract())) {
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

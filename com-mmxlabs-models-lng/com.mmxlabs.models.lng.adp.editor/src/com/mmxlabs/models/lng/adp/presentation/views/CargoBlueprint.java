/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.views;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.CargoEditingCommands;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class CargoBlueprint {
	private final int loadCounter;
	private final Vessel assignedVessel;
	private final LocalDateTime windowStart;
	private int windowSizeHours;
	private final Inventory inventory;
	private final PurchaseContract inventoryPurchaseContract;
	private final AllocationTracker dischargeAllocation;
	private final int allocatedVolume;
	private final BaseLegalEntity entity;
	
	public CargoBlueprint(final Inventory inventory, final PurchaseContract purchaseContract, final int loadCounter, final Vessel assignedVessel, final LocalDateTime windowStart, final int windowSizeHours, final AllocationTracker allocationTracker, final int allocatedVolume, final BaseLegalEntity entity) {
		this.inventory = inventory;
		this.inventoryPurchaseContract = purchaseContract;
		this.loadCounter = loadCounter;
		this.assignedVessel = assignedVessel;
		this.windowStart = windowStart;
		this.allocatedVolume = allocatedVolume;
		this.entity = entity;
		final LocalDateTime endWindowDateTime = windowStart.plusHours(windowSizeHours);
		final LocalDateTime lastDateTimeOfMonth = LocalDateTime.of(YearMonth.from(windowStart).atEndOfMonth(), LocalTime.of(23, 00));
		this.windowSizeHours = lastDateTimeOfMonth.isBefore(endWindowDateTime) ? Hours.between(windowStart,lastDateTimeOfMonth) : windowSizeHours;
		this.dischargeAllocation = allocationTracker;
	}
	
	public void updateWindowSize(final int newWindowSizeHours) {
		if (this.windowSizeHours > newWindowSizeHours) {
			this.windowSizeHours = newWindowSizeHours;
		}
	}
	
	public LocalDateTime getWindowStart() {
		return this.windowStart;
	}
	
	public void constructCargoModelPopulationCommands(final CargoModel cargoModel, final CargoEditingCommands cec, @NonNull final EditingDomain editingDomain, final int volumeFlex, final IScenarioDataProvider sdp, final Map<Vessel, VesselAvailability> vesselToVA, final CompoundCommand compoundCommand) {
		final List<Command> commands = new LinkedList<>();
		
		final LoadSlot loadSlot = this.createLoadSlot(cec, commands, cargoModel, volumeFlex);
		final DischargeSlot dischargeSlot = this.dischargeAllocation.createDischargeSlot(cec, commands, cargoModel, sdp, loadSlot, this.assignedVessel);
		final Cargo cargo = CargoEditingCommands.createNewCargo(editingDomain, commands, cargoModel, null, 0);
		loadSlot.setCargo(cargo);
		dischargeSlot.setCargo(cargo);
		cargo.setAllowRewiring(this.entity.getName().equalsIgnoreCase("capl/tapl"));
		if (!dischargeSlot.isFOBSale() && this.assignedVessel != null) {
			final VesselAvailability vesselAvailability = vesselToVA.get(this.assignedVessel);
			if (vesselAvailability != null) {
				cargo.setVesselAssignmentType(vesselAvailability);
			}
		}
		for (final Command command : commands)
			compoundCommand.append(command);
	}
	
	private LoadSlot createLoadSlot(final CargoEditingCommands cec, final List<Command> commands, final CargoModel cargoModel, final int volumeFlex) {
		final LoadSlot loadSlot = cec.createNewLoad(commands, cargoModel, false);
		loadSlot.setPort(this.inventory.getPort());
		loadSlot.setContract(inventoryPurchaseContract);
		loadSlot.setVolumeLimitsUnit(VolumeUnits.M3);
		
		loadSlot.setMinQuantity(this.allocatedVolume-volumeFlex);
		loadSlot.setMaxQuantity(this.allocatedVolume+volumeFlex);
		final String loadSlotName = String.format("%s-%03d", this.inventory.getName(), this.loadCounter);
		loadSlot.setName(loadSlotName);
		loadSlot.setWindowStart(this.windowStart.toLocalDate());
		loadSlot.setWindowStartTime(this.windowStart.getHour());
		loadSlot.setWindowSize(this.windowSizeHours);
		loadSlot.setWindowSizeUnits(TimePeriod.HOURS);
		loadSlot.setEntity(this.entity);
		return loadSlot;
	}

	public Vessel getAssignedVessel() {
		return this.assignedVessel;
	}

	public int getAllocatedVolume() {
		return this.allocatedVolume;
	}
}

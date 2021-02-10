/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull;

import java.time.LocalDate;
import java.util.List;

import org.eclipse.emf.common.command.Command;

import com.mmxlabs.models.lng.adp.DESSalesMarketAllocationRow;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.CargoEditingCommands;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.types.FOBSaleDealType;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class DESMarketTracker extends AllocationTracker {
	final DESSalesMarket salesMarket;

	public DESMarketTracker(final DESSalesMarketAllocationRow allocationRow, final double totalWeight) {
		super(allocationRow.getWeight()/totalWeight, allocationRow.getVessels());
		this.salesMarket = allocationRow.getDesSalesMarket();
	}

	@Override
	public int calculateExpectedBoiloff(final Vessel vessel, final int loadDuration) {
		return this.sharesVessels ? super.calculateExpectedBoiloff(vessel, loadDuration) : 0;
	}

	@Override
	public DischargeSlot createDischargeSlot(final CargoEditingCommands cec, List<Command> setCommands, final CargoModel cargoModel, final IScenarioDataProvider sdp, final LoadSlot loadSlot, final Vessel vessel) {
		final DischargeSlot dischargeSlot;
		if (this.sharesVessels) {
			dischargeSlot = cec.createNewSpotDischarge(setCommands, cargoModel, this.salesMarket);
			final LocalDate dischargeDate = AllocationTracker.calculateDischargeDate(loadSlot, dischargeSlot, vessel, sdp);
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
}

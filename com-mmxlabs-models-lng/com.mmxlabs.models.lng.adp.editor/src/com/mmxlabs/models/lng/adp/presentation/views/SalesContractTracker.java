package com.mmxlabs.models.lng.adp.presentation.views;

import java.time.LocalDate;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.adp.SalesContractAllocationRow;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.CargoEditingCommands;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class SalesContractTracker extends AllocationTracker {
	private final SalesContract salesContract;
	
	public SalesContractTracker(final SalesContractAllocationRow allocationRow, final double totalWeight) {
		super(allocationRow.getWeight()/totalWeight, allocationRow.getVessels());
		this.salesContract = allocationRow.getContract();
	}
	
	@Override
	public DischargeSlot createDischargeSlot(final CargoEditingCommands cec, List<Command> setCommands, final CargoModel cargoModel, @NonNull final IScenarioDataProvider sdp, final LoadSlot loadSlot, final Vessel vessel) {
		final DischargeSlot dischargeSlot = cec.createNewDischarge(setCommands, cargoModel, false);
		dischargeSlot.setContract(this.salesContract);
		dischargeSlot.setEntity(loadSlot.getEntity());
		dischargeSlot.setPort(salesContract.getPreferredPort());
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
		
		final String id = String.format("des-sale-%s", loadSlot.getName());
		dischargeSlot.setName(id);
		
		return dischargeSlot;
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.utils;

import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.AllocationRecord;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * Used in the volume allocators to provide calculations relating to the inclusion of in port boil off rates.
 * 
 * @author NSteadman
 *
 */
public class InPortBoilOffHelper implements IBoilOffHelper {

	private boolean boilOffCompensation = false;

	@Override
	public boolean isBoilOffCompensation() {
		return boilOffCompensation;
	}

	@Override
	public void setBoilOffCompensation(boolean boilOffCompensation) {
		this.boilOffCompensation = boilOffCompensation;
	}

	public InPortBoilOffHelper(boolean boilOffCompensation) {
		this.boilOffCompensation = boilOffCompensation;
	}

	/**
	 * Returns the in port NBO rate associated with the vessel class of the vessel provided.
	 */
	@Override
	public long getNBORate(IVessel vessel, PortType portStatus) {

		if (portStatus == PortType.Load) {
			return vessel.getVesselClass().getInPortNBORate(VesselState.Laden);
		} else if (portStatus == PortType.Discharge) {
			return vessel.getVesselClass().getInPortNBORate(VesselState.Ballast);
		}
		return 0;
	}

	@Override
	public long calculatePortVisitNBOInM3(IVessel vessel, IPortSlot portSlot, AllocationRecord record) {

		PortType eventType = portSlot.getPortType();
		final long dailyRateInM3 = getNBORate(vessel, eventType);
		int eventDurationInHours = record.portTimesRecord.getSlotDuration(portSlot);
		return Calculator.quantityFromRateTime(dailyRateInM3, eventDurationInHours)/ 24L;
	}

}

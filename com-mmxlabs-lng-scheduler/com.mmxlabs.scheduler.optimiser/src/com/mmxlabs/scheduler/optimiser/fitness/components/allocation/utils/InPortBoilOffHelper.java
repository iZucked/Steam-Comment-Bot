package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.utils;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.AllocationRecord;
import com.mmxlabs.scheduler.optimiser.fitness.impl.PlanEvaluationData;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;
/**
 * Used in the volume allocators to provide calculations relating to the inclusion of in port boil off rates.
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


	public InPortBoilOffHelper(boolean boilOffCompensation){
		this.boilOffCompensation = boilOffCompensation;
	}
	
	
	/**
	 * Returns the in port NBO rate associated with the vessel class of the vessel provided.
	 */
	@Override
	public long getNBORate(IVessel vessel, PortType portStatus){
	
			if(portStatus == PortType.Load){
				return vessel.getVesselClass().getInPortNBORate(VesselState.Laden);
			} else if(portStatus == PortType.Discharge){
				return vessel.getVesselClass().getInPortNBORate(VesselState.Ballast);
			}
		return 0;
	}
	
	@Override
	public long getPortVisitNBOInM3(IVessel vessel, IPortSlot portSlot, AllocationRecord record){

		PortType eventType = portSlot.getPortType();
		final long dailyRateInM3 = getNBORate(vessel, eventType);
		int eventDurationInHours = record.portTimesRecord.getSlotDuration(portSlot);
		return (eventDurationInHours * dailyRateInM3) / 24L;
	}


}

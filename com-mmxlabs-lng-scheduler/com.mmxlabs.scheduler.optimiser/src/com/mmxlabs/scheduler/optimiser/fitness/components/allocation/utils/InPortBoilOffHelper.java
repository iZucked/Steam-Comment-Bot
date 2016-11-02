package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.utils;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.Calculator;
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
	
	public InPortBoilOffHelper(boolean boilOffCompensation){
		this.boilOffCompensation = boilOffCompensation;
	}
	
	/**
	 * Is boil off compensation on?
	 */
	@Override
	public boolean compensateForBoilOff(){
		return boilOffCompensation;
	}
	
	/**
	 *  Either deduct from or increase currentHeel by the physical or commercial volume dependent on the port visit type
	 *  and whether in port boil off is being compensated for.
	 */
	@Override
	public long adjustCurrentHeel(long currentHeel, PortType portStatus, IPortSlot portSlot, PlanEvaluationData planData){
		long adjustmentVolume;
		
		if(boilOffCompensation)
			adjustmentVolume = planData.getAllocation().getPhysicalSlotVolumeInM3(portSlot);
		else
			adjustmentVolume = planData.getAllocation().getCommercialSlotVolumeInM3(portSlot);
		
		if(portStatus == PortType.Load)
			return currentHeel + adjustmentVolume;
		else if (portStatus == PortType.Discharge)
			return currentHeel - adjustmentVolume;
		
		return currentHeel;		
	}
	
	/**
	 * Returns the in port NBO rate associated with the vessel class of the vessel provided.
	 */
	@Override
	public long getNBORate(IVessel vessel, PortType portStatus){
	
			if(portStatus == PortType.Load)
				return vessel.getVesselClass().getInPortNBORate(VesselState.Laden);
			else if(portStatus == PortType.Discharge)
				return vessel.getVesselClass().getInPortNBORate(VesselState.Ballast);
		
		return 0;
	}
	
	/**
	 *  Increases the passed volume by the appropriate value based on the in port NBO rate and the duration of the
	 *  port visit. Does not modify the value if 'boilOffCompensation' is false. Performs the calculations in terms
	 *  the FuelUnit passed. Volume units should correspond to 'units'.
	 */
	@Override
	public long compensateForBoilOff(IPortSlot slot, AllocationRecord record, long volume, FuelUnit units){
		
		int slotDuration  = record.portTimesRecord.getSlotDuration(slot);
		IVessel vessel = record.vesselAvailability.getVessel();
		long inPortNBORate = getNBORate(vessel,slot.getPortType());
		int slotCV = record.slotCV.get(record.slots.indexOf(slot));
		
		
		long NBOBoilOff = 0;
		if(units == FuelUnit.M3){
			NBOBoilOff = (inPortNBORate*slotDuration);
		} else if(units == FuelUnit.MMBTu){
			NBOBoilOff = Calculator.convertM3ToMMBTu((slotDuration*inPortNBORate), slotCV);
		}
		
		long answer = volume - NBOBoilOff;
		
		System.out.println("PORT: " + slot.getPortType() + " Vol: " + volume + " NBO: " + NBOBoilOff + " Ans: " + answer);
		
		return volume;
		
			
	}
	
	/**
	 *  Returns the physical volume by factoring in the in port boil off rate to the passed 'commercialVolume'. 'commercialVolume' should be in the same
	 *  units as the FuelUnit passed.
	 */
	@Override
	public long calculatePhysicalVolume(IPortSlot slot, AllocationRecord record, long commercialVolume, FuelUnit units){
		
		int slotDuration  = record.portTimesRecord.getSlotDuration(slot);
		IVessel vessel = record.vesselAvailability.getVessel();
		long inPortNBORate = getNBORate(vessel,slot.getPortType());
		int slotCV = record.slotCV.get(record.slots.indexOf(slot));
		
		long NBOBoilOff = 0;
		if(units == FuelUnit.MMBTu)
			NBOBoilOff = Calculator.convertM3ToMMBTu((slotDuration*inPortNBORate), slotCV);
		else if(units == FuelUnit.M3)
			NBOBoilOff = (slotDuration*inPortNBORate);
		
		if(slot.getPortType() == PortType.Load)	
			return commercialVolume  - NBOBoilOff ;
		else if(slot.getPortType() == PortType.Discharge)
			return commercialVolume; //  - Calculator.convertM3ToMMBTu((slotDuration*inPortNBORate), slotCV);
		else
			return commercialVolume;
	}

}

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
		
		long slotDuration  = record.portTimesRecord.getSlotDuration(slot)/24L;
		IVessel vessel = record.vesselAvailability.getVessel();
		long inPortNBORate = getNBORate(vessel,slot.getPortType());
		int slotCV = record.slotCV.get(record.slots.indexOf(slot));
		
		
		
		long NBOBoilOff = 0;
		if(units == FuelUnit.M3){
			NBOBoilOff = (inPortNBORate*slotDuration);
		} else if(units == FuelUnit.MMBTu){
			NBOBoilOff = Calculator.convertM3ToMMBTu((slotDuration*inPortNBORate), slotCV);
		}

		if(boilOffCompensation){
			if(slot.getPortType() == PortType.Load)	
				return volume  + NBOBoilOff ;
			else if(slot.getPortType() == PortType.Discharge)
				return volume - NBOBoilOff;
		}
		
		return volume;
	}
	
	/**
	 *  Returns the physical volume by factoring in the in port boil off rate to the passed 'commercialVolume'. 'commercialVolume' should be in the same
	 *  units as the FuelUnit passed.
	 */
	@Override
	public long calculatePhysicalVolume(IPortSlot slot, AllocationRecord record, long commercialVolume, FuelUnit units){
		
		long slotDuration  = record.portTimesRecord.getSlotDuration(slot)/24L;
		IVessel vessel = record.vesselAvailability.getVessel();
		long inPortNBORate = getNBORate(vessel,slot.getPortType());
		int slotCV = record.slotCV.get(record.slots.indexOf(slot));
		
		long NBOBoilOff = 0;
		
//		if(boilOffCompensation){
//			System.out.println("Calculate");
			if(units == FuelUnit.MMBTu){
				NBOBoilOff = Calculator.convertM3ToMMBTu((slotDuration*inPortNBORate), slotCV);
			}else if(units == FuelUnit.M3){
				NBOBoilOff = (slotDuration*inPortNBORate);
				System.out.println(" NBOR: " + NBOBoilOff);
			}
				//		}
			
		long volume = commercialVolume - NBOBoilOff;
		
//		if( volume < 0)
//			volume = 1;
//		
//		if(slot.getPortType() == PortType.Load)	
//			return commercialVolume  - NBOBoilOff ;
//		else if(slot.getPortType() == PortType.Discharge)
//			return commercialVolume - NBOBoilOff;
		
		return volume;
	}

}

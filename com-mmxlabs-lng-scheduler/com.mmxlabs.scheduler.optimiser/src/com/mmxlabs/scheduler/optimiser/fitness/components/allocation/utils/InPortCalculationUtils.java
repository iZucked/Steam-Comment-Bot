package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.utils;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.AllocationRecord;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;

public class InPortCalculationUtils {
	
	private static boolean boilOffCompensation = false;
	
	private static long getNBORate(IVessel vessel, PortType portStatus){
		if(portStatus == PortType.Load)
			return vessel.getVesselClass().getInPortNBORate(VesselState.Laden);
		else if(portStatus == PortType.Discharge)
			return vessel.getVesselClass().getInPortNBORate(VesselState.Ballast); 
		else
			return 0;
	}
	
	/**
	 *  Increases the passed volume by the appropriate value based on the in port NBO rate and the duration of the
	 *  port visit. Does not modify the value if 'boilOffCompensation' is false. Performs the calculations in terms
	 *  the FuelUnit passed. Volume units should correspond to 'units'.
	 */
	public static long compensateForBoilOff(IPortSlot slot, AllocationRecord record, long volume, FuelUnit units){
		
		int slotDuration  = record.portTimesRecord.getSlotDuration(slot);
		IVessel vessel = record.vesselAvailability.getVessel();
		long inPortNBORate = 0;
		int slotCV = record.slotCV.get(record.slots.indexOf(slot));
		
		if(boilOffCompensation)
			inPortNBORate = getNBORate(vessel,slot.getPortType());	

		if(units == FuelUnit.M3)
			return volume + (inPortNBORate*slotDuration);
		else if(units == FuelUnit.MMBTu)
			return volume + Calculator.convertM3ToMMBTu((slotDuration*inPortNBORate), slotCV);
		else
			return volume;
	}
	
	/**
	 *  Returns the physical volume by factoring in the in port boil off rate to the passed 'commercialVolume'. 'commercialVolume' should be in the same
	 *  units as the FuelUnit passed.
	 */
	public static long calculatePhysicalVolume(IPortSlot slot, AllocationRecord record, long commercialVolume, FuelUnit units){
		
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

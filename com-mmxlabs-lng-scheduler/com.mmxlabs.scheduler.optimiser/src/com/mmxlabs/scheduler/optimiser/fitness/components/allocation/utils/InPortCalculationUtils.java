package com.mmxlabs.scheduler.optimiser.fitness.components.allocation.utils;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.AllocationRecord;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class InPortCalculationUtils {
	
	private static boolean boilOffCompensation = false;
	
	public static long compensateForBoilOffInM3(IPortSlot slot, AllocationRecord record, long commercialVolumeInM3){
		
		int slotDuration  = record.portTimesRecord.getSlotDuration(slot);
		IVessel vessel = record.vesselAvailability.getVessel();
		long inPortNBORate = 0;
		
		if(boilOffCompensation){
			if(slot.getPortType() == PortType.Load){
				inPortNBORate = vessel.getVesselClass().getInPortNBORate(VesselState.Laden);
			}
			else if(slot.getPortType() == PortType.Discharge){
				inPortNBORate = vessel.getVesselClass().getInPortNBORate(VesselState.Ballast);
			}
		}
		return commercialVolumeInM3 + (inPortNBORate*slotDuration);	
	}
	
	public static long compensateForBoilOffInMMBTu(IPortSlot slot, AllocationRecord record, long commercialVolumeInMMBTu){
		
		int slotDuration  = record.portTimesRecord.getSlotDuration(slot);
		IVessel vessel = record.vesselAvailability.getVessel();
		long inPortNBORate = 0;
		int slotCV = record.slotCV.get(record.slots.indexOf(slot));
		
		if(boilOffCompensation){
			if(slot.getPortType() == PortType.Load){
				inPortNBORate = vessel.getVesselClass().getInPortNBORate(VesselState.Laden);
			}
			else if(slot.getPortType() == PortType.Discharge){
				inPortNBORate = vessel.getVesselClass().getInPortNBORate(VesselState.Ballast);
			}
		}
		return commercialVolumeInMMBTu + Calculator.convertM3ToMMBTu((slotDuration*inPortNBORate), slotCV);	
	}
	
	
	public static long calculatePhysicalVolumeInM3(IPortSlot slot, AllocationRecord record, long commercialVolumeInM3){
		
		long physicalVolume = 0;
		int slotDuration  = record.portTimesRecord.getSlotDuration(slot);
		IVessel vessel = record.vesselAvailability.getVessel();
		long inPortNBORate = 0;
		
		if(slot.getPortType() == PortType.Load){
			inPortNBORate = vessel.getVesselClass().getInPortNBORate(VesselState.Laden);
			return commercialVolumeInM3  + (slotDuration*inPortNBORate);
		}
		else if(slot.getPortType() == PortType.Discharge){
			inPortNBORate = vessel.getVesselClass().getInPortNBORate(VesselState.Ballast);
			return commercialVolumeInM3  - (slotDuration*inPortNBORate);
		}
		return commercialVolumeInM3;
	}
	
	public static long calculatePhysicalVolumeInMMBTu(IPortSlot slot, AllocationRecord record, long commercialVolumeInMMBTu){
			
		long physicalVolume = 0;
		int slotDuration  = record.portTimesRecord.getSlotDuration(slot);
		IVessel vessel = record.vesselAvailability.getVessel();
		long inPortNBORate = 0;
		int slotCV = record.slotCV.get(record.slots.indexOf(slot));
		
		if(slot.getPortType() == PortType.Load){
			inPortNBORate = vessel.getVesselClass().getInPortNBORate(VesselState.Laden);
			return commercialVolumeInMMBTu  + Calculator.convertM3ToMMBTu((slotDuration*inPortNBORate), slotCV);
		}
		else if(slot.getPortType() == PortType.Discharge){
			inPortNBORate = vessel.getVesselClass().getInPortNBORate(VesselState.Ballast);
			return commercialVolumeInMMBTu  - Calculator.convertM3ToMMBTu((slotDuration*inPortNBORate), slotCV);
		}
		return commercialVolumeInMMBTu;
	}
}

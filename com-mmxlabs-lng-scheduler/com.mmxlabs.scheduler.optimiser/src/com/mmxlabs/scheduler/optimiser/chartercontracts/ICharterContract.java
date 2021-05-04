package com.mmxlabs.scheduler.optimiser.chartercontracts;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

/**
 * Internal representation of the CommercialModel.GenericCharterContract
 * @author FM
 *
 */
public interface ICharterContract {

	ICharterContractAnnotation annotate(IPort firstLoad, IPortSlot lastSlot, IVesselAvailability vesselAvailability, int vesselStartTime, int vesselEndTime);
	
	long calculateCost(IPort firstLoad, IPortSlot lastSlot, IVesselAvailability vesselAvailability, int vesselStartTime, int vesselEndTime);
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.chartercontracts;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

/**
 * Internal representation of the CommercialModel.GenericCharterContract
 * 
 * @author FM
 *
 */
@NonNullByDefault
public interface ICharterContract {

	ICharterContractAnnotation annotateBB(IPortTimesRecord portTimesRecord, IPort firstLoad, IPortSlot lastSlot, IVesselAvailability vesselAvailability, int vesselStartTime, int vesselEndTime);

	long calculateBBCost(IPortTimesRecord portTimesRecord, IPort firstLoad, IPortSlot lastSlot, IVesselAvailability vesselAvailability, int vesselStartTime, int vesselEndTime);
	
	ICharterContractAnnotation annotateRF(IPortTimesRecord portTimesRecord, IPort firstLoad, IPortSlot lastSlot, IVesselAvailability vesselAvailability, int vesselStartTime, int vesselEndTime);

	long calculateRFRevenue(IPortTimesRecord portTimesRecord, IPort firstLoad, IPortSlot lastSlot, IVesselAvailability vesselAvailability, int vesselStartTime, int vesselEndTime);
}

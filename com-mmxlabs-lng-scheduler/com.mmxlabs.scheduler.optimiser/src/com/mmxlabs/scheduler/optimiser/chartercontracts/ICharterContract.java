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

	ICharterContractAnnotation annotateBB(IPortTimesRecord portTimesRecord, IPortSlot portSlot, IVesselAvailability vesselAvailability, int vesselStartTime, final IPort firstLoadPort);

	long calculateBBCost(IPortTimesRecord portTimesRecord, IPortSlot portSlot, IVesselAvailability vesselAvailability, int vesselStartTime, final IPort firstLoadPort);
	
	ICharterContractAnnotation annotateRF(IPortTimesRecord portTimesRecord, IVesselAvailability vesselAvailability);

	long calculateRFRevenue(IPortTimesRecord portTimesRecord, IVesselAvailability vesselAvailability);
}

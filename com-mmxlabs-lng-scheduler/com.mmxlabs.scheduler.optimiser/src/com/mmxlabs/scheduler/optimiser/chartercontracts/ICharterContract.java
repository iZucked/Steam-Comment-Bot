/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.chartercontracts;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselStartState;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

/**
 * Internal representation of the CommercialModel.GenericCharterContract
 * 
 * @author FM
 *
 */
@NonNullByDefault
public interface ICharterContract {

	ICharterContractAnnotation annotateBB(IPortTimesRecord portTimesRecord, IPortSlot portSlot, IVesselCharter vesselCharter, VesselStartState vesselStartState);

	long calculateBBCost(IPortTimesRecord portTimesRecord, IPortSlot portSlot, IVesselCharter vesselCharter, VesselStartState vesselStartState);
	
	ICharterContractAnnotation annotateRF(IPortTimesRecord portTimesRecord, IVesselCharter vesselCharter);

	long calculateRFRevenue(IPortTimesRecord portTimesRecord, IVesselCharter vesselCharter);
}

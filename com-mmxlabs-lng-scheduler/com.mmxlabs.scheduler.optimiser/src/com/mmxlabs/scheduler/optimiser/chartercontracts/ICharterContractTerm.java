/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.chartercontracts;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

/**
 * A charter contract term. Internal representation of the CommercialModel.CharterContractTerm
 * 
 * @author FM
 *
 */
public interface ICharterContractTerm {
	/**
	 * Returns true if the rule should be activated
	 * 
	 * @param slot
	 * @param vesselAvailability
	 * @param time
	 * @return
	 */
	boolean match(final IPort loadPort, IPortSlot slot, IVesselAvailability vesselAvailability, int vesselStartTime, int vesselEndTime);

	long calculateCost(IPort firstLoad, IPortSlot lastSlot, IVesselAvailability vesselAvailability, int vesselStartTime, int vesselEndTime);

	ICharterContractTermAnnotation annotate(IPort firstLoad, IPortSlot lastSlot, IVesselAvailability vesselAvailability, int vesselStartTime, int vesselEndTime);
}

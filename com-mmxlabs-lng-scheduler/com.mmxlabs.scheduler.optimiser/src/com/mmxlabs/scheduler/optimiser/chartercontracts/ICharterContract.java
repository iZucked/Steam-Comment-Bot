/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.chartercontracts;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

/**
 * Internal representation of the CommercialModel.GenericCharterContract
 * 
 * @author FM
 *
 */
@NonNullByDefault
public interface ICharterContract {

	ICharterContractAnnotation annotate(IPort firstLoad, IPortSlot lastSlot, IVesselAvailability vesselAvailability, int vesselStartTime, int vesselEndTime);

	long calculateCost(IPort firstLoad, IPortSlot lastSlot, IVesselAvailability vesselAvailability, int vesselStartTime, int vesselEndTime);
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.chartercontracts;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

/**
 * A charter contract term. Internal representation of the
 * CommercialModel.CharterContractTerm
 * 
 * @author FM
 *
 */
@NonNullByDefault
public interface IRepositioningFeeTerm {
	/**
	 * Returns true if the rule should be activated
	 * 
	 * @param slot
	 * @param vesselAvailability
	 * @param time
	 * @return
	 */
	boolean match(IPortTimesRecord portTimesRecord, IVesselAvailability vesselAvailability);

	long calculateCost(IPortTimesRecord portTimesRecord, IVesselAvailability vesselAvailability);

	ICharterContractTermAnnotation annotate(IPortTimesRecord portTimesRecord, IVesselAvailability vesselAvailability);

}

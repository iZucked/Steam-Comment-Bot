/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.chartercontracts;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselStartState;
import com.mmxlabs.scheduler.optimiser.evaluation.PreviousHeelRecord;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

/**
 * A charter contract term. Internal representation of the
 * CommercialModel.CharterContractTerm
 * 
 * @author FM
 *
 */
@NonNullByDefault
public interface IBallastBonusTerm {
	/**
	 * Returns true if the rule should be activated
	 * 
	 * @param slot
	 * @param vesselCharter
	 * @param time
	 * @return
	 */
	boolean match(IPortTimesRecord portTimesRecord, IVesselCharter vesselCharter, VesselStartState vesselStartState);

	long calculateCost(IPortTimesRecord portTimesRecord, IVesselCharter vesselCharter, VesselStartState vesselStartState, PreviousHeelRecord heelRecord);

	@Nullable
	ICharterContractTermAnnotation annotate(IPortTimesRecord portTimesRecord, IVesselCharter vesselCharter, VesselStartState vesselStartState, PreviousHeelRecord heelRecord);
}

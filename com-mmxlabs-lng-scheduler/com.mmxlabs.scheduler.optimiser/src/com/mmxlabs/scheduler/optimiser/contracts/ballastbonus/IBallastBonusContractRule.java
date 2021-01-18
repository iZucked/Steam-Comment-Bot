/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.ballastbonus;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.impl.IBallastBonusRuleAnnotation;

/**
 * A ballast bonus contract rule
 * 
 * @author alex
 *
 */
public interface IBallastBonusContractRule {
	/**
	 * Returns true if the rule should be activated
	 * 
	 * @param slot
	 * @param vesselAvailability
	 * @param time
	 * @return
	 */
	boolean match(IPortSlot slot, IVesselAvailability vesselAvailability, int vesselStartTime, int vesselEndTime);
	
	
	/**
	 * Returns the dollar value of the ballast bonus
	 * 
	 * @param lastSlot
	 * @param vesselAvailability
	 * @param time
	 * @return
	 */
	long calculateBallastBonus(IPortSlot lastSlot, IVesselAvailability vesselAvailability, int vesselStartTime, int vesselEndTime);

	default long calculateBallastBonus(IPort firstLoad, IPortSlot lastSlot, IVesselAvailability vesselAvailability, int vesselStartTime, int vesselEndTime) {
		return calculateBallastBonus(lastSlot, vesselAvailability, vesselStartTime, vesselEndTime);
	}

	
	IBallastBonusRuleAnnotation annotate(IPortSlot lastSlot, IVesselAvailability vesselAvailability, int vesselStartTime, int vesselEndTime);

	default IBallastBonusRuleAnnotation annotate(IPort firstLoad, IPortSlot lastSlot, IVesselAvailability vesselAvailability, int vesselStartTime, int vesselEndTime) {
		return annotate(firstLoad, lastSlot, vesselAvailability, vesselEndTime, vesselEndTime);
	}
}

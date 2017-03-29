package com.mmxlabs.scheduler.optimiser.contracts.ballastbonus;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

/**
 * A ballast bonus contract rule
 * @author alex
 *
 */
public interface IBallastBonusContractRule {
	/**
	 * Returns true if the rule should be activated  
	 * @param slot
	 * @param vesselAvailability
	 * @param time
	 * @return
	 */
	boolean match(IPortSlot slot, IVesselAvailability vesselAvailability, int time);
	/**
	 * Returns the dollar value of the ballast bonus
	 * @param lastSlot
	 * @param vesselAvailability
	 * @param time
	 * @return
	 */
	long calculateBallastBonus(IPortSlot lastSlot, IVesselAvailability vesselAvailability, int time);
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.ballastbonus;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.impl.BallastBonusAnnotation;

/**
 * A ballast bonus contract defines a number of {@link IBallastBonusContractRule} which produce a ballast bonus in dollars if matched.
 * 
 * @author alex
 *
 */
public interface IBallastBonusContract {

	default long calculateBallastBonus(IPort firstLoad, IPortSlot lastSlot, IVesselAvailability vesselAvailability, int vesselStartTime, int vesselEndTime) {
		return calculateBallastBonus(lastSlot, vesselAvailability, vesselStartTime, vesselEndTime);
	}

	default BallastBonusAnnotation annotate(IPort firstLoad, IPortSlot lastSlot, IVesselAvailability vesselAvailability, int vesselStartTime, int vesselEndTime) {
		return annotate(lastSlot, vesselAvailability, vesselStartTime, vesselEndTime);
	}
	
	long calculateBallastBonus(IPortSlot lastSlot, IVesselAvailability vesselAvailability, int vesselStartTime, int vesselEndTime);

	BallastBonusAnnotation annotate(IPortSlot lastSlot, IVesselAvailability vesselAvailability, int vesselStartTime, int vesselEndTime);
}

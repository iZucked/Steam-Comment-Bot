/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.ballastbonus;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.impl.BallastBonusAnnotation;

/**
 * A ballast bonus contract defines a number of {@link IBallastBonusContractRule} which produce a ballast bonus in dollars
 * if matched.
 * @author alex
 *
 */
public interface IBallastBonusContract {
	long calculateBallastBonus(IPortSlot lastSlot, IVesselAvailability vesselAvailability, int time);

	BallastBonusAnnotation annotate(IPortSlot lastSlot, IVesselAvailability vesselAvailability, int time);
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.IBallastBonusContract;

public interface ISpotCharterInMarket {

	@NonNull
	String getName();

	@NonNull
	IVessel getVessel();

	@NonNull
	ILongCurve getDailyCharterInRateCurve();

	int getAvailabilityCount();

	IEndRequirement getEndRequirement();

	IBallastBonusContract getBallastBonusContract();

	/**
	 * Returns the repositioning fee of the vessel.
	 * 
	 * @return repositioning fee
	 */
	@NonNull
	ILongCurve getRepositioningFee();
}

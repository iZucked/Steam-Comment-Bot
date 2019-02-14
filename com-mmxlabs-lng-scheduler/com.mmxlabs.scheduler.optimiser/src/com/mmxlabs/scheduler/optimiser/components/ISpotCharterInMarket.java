/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
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

}

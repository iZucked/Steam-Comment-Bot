/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.scheduler.optimiser.chartercontracts.ICharterContract;

public interface ISpotCharterInMarket {

	@NonNull
	String getName();

	@NonNull
	IVessel getVessel();

	@NonNull
	ILongCurve getDailyCharterInRateCurve();

	int getAvailabilityCount();

	IEndRequirement getEndRequirement();

	ICharterContract getCharterContract();

	/**
	 * Returns the repositioning fee of the vessel.
	 * 
	 * @return repositioning fee
	 */
	@NonNull
	ILongCurve getRepositioningFee();
}

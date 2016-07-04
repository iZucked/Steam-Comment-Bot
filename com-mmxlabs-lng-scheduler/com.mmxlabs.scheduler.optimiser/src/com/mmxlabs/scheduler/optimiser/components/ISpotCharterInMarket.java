/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.curves.ILongCurve;

public interface ISpotCharterInMarket {

	@NonNull
	String getName();

	@NonNull
	IVesselClass getVesselClass();

	@NonNull
	ILongCurve getDailyCharterInRateCurve();

	int getAvailabilityCount();

}

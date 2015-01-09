package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.curves.ICurve;

public interface ISpotCharterInMarket {

	@NonNull
	String getName();

	@NonNull
	IVesselClass getVesselClass();

	@NonNull
	ICurve getDailyCharterInRateCurve();

	int getAvailabilityCount();

}

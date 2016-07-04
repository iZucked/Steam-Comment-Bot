/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.curves.ILongCurve;

public final class DefaultSpotCharterInMarket implements ISpotCharterInMarket {

	private final String name;
	private final IVesselClass vesselClass;
	private final ILongCurve dailyCharterInRateCurve;
	private final int availabilityCount;

	public DefaultSpotCharterInMarket(@NonNull final String name, @NonNull final IVesselClass vesselClass, @NonNull final ILongCurve dailyCharterInRateCurve, final int availabilityCount) {
		this.name = name;
		this.vesselClass = vesselClass;
		this.dailyCharterInRateCurve = dailyCharterInRateCurve;
		this.availabilityCount = availabilityCount;

	}

	@Override
	@NonNull
	@SuppressWarnings("null")
	public final String getName() {
		return name;
	}

	@Override
	@NonNull
	@SuppressWarnings("null")
	public final IVesselClass getVesselClass() {
		return vesselClass;
	}

	@Override
	@NonNull
	@SuppressWarnings("null")
	public final ILongCurve getDailyCharterInRateCurve() {
		return dailyCharterInRateCurve;
	}

	@Override
	public int getAvailabilityCount() {
		return availabilityCount;
	}

}

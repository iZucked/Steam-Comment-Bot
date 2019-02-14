/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.IBallastBonusContract;

public final class DefaultSpotCharterInMarket implements ISpotCharterInMarket {

	private final @NonNull String name;
	private final @NonNull IVessel vessel;
	private final @NonNull ILongCurve dailyCharterInRateCurve;
	private final int availabilityCount;
	private final IBallastBonusContract ballastBonusContract;
	private final IEndRequirement end;

	public DefaultSpotCharterInMarket(@NonNull final String name, @NonNull final IVessel vessel, @NonNull final ILongCurve dailyCharterInRateCurve, final int availabilityCount, IEndRequirement end,
			IBallastBonusContract ballastBonusContract) {
		this.name = name;
		this.vessel = vessel;
		this.dailyCharterInRateCurve = dailyCharterInRateCurve;
		this.availabilityCount = availabilityCount;
		this.end = end;
		this.ballastBonusContract = ballastBonusContract;
	}

	@Override
	@NonNull
	public final String getName() {
		return name;
	}

	@Override
	@NonNull
	public final IVessel getVessel() {
		return vessel;
	}

	@Override
	@NonNull
	public final ILongCurve getDailyCharterInRateCurve() {
		return dailyCharterInRateCurve;
	}

	@Override
	public int getAvailabilityCount() {
		return availabilityCount;
	}

	@Override
	public IBallastBonusContract getBallastBonusContract() {
		return ballastBonusContract;
	}

	@Override
	public IEndRequirement getEndRequirement() {
		return end;
	}
}

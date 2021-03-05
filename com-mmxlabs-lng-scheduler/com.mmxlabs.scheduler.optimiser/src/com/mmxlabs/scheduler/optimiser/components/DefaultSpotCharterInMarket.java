/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.scheduler.optimiser.chartercontracts.ICharterContract;

public final class DefaultSpotCharterInMarket implements ISpotCharterInMarket {

	private final @NonNull String name;
	private final @NonNull IVessel vessel;
	private final @NonNull ILongCurve dailyCharterInRateCurve;
	private final int availabilityCount;
	private final ICharterContract charterContract;
	private final IEndRequirement end;
	private final ILongCurve repositioningFee;
	
	public DefaultSpotCharterInMarket(@NonNull final String name, @NonNull final IVessel vessel, @NonNull final ILongCurve dailyCharterInRateCurve, final int availabilityCount, IEndRequirement end,
			ICharterContract charterContract, final ILongCurve repositioningFee) {
		this.name = name;
		this.vessel = vessel;
		this.dailyCharterInRateCurve = dailyCharterInRateCurve;
		this.availabilityCount = availabilityCount;
		this.end = end;
		this.charterContract = charterContract;
		this.repositioningFee = repositioningFee;
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
	public ICharterContract getCharterContract() {
		return charterContract;
	}

	@Override
	public IEndRequirement getEndRequirement() {
		return end;
	}

	@Override
	public ILongCurve getRepositioningFee() {
		return this.repositioningFee;
	}
}

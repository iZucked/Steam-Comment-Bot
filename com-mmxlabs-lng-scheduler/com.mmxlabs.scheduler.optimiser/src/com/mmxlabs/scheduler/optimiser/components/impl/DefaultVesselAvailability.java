/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;

/**
 * Default implementation of {@link IVessel}.
 * 
 * @author Simon Goodall
 * 
 */
public final class DefaultVesselAvailability implements IVesselAvailability {

	@NonNull
	private final IVessel vessel;

	@NonNull
	private final VesselInstanceType vesselInstanceType;

	private ILongCurve dailyCharterInRate;

	private ILongCurve repositioningFee;
	
	private ILongCurve ballastBonus;

	private IStartEndRequirement startRequirement;
	private IStartEndRequirement endRequirement;

	private ISpotCharterInMarket spotCharterInMarket;

	private int spotIndex;
	
	private boolean optional;

	public DefaultVesselAvailability(@NonNull final IVessel vessel, @NonNull final VesselInstanceType vesselInstanceType) {
		this.vessel = vessel;
		this.vesselInstanceType = vesselInstanceType;
	}

	@Override
	@NonNull
	public IVessel getVessel() {
		return vessel;
	}

	@Override
	@NonNull
	public VesselInstanceType getVesselInstanceType() {
		return vesselInstanceType;
	}

	@Override
	public String toString() {
		return vessel.getName();
	}

	@Override
	public ILongCurve getDailyCharterInRate() {
		return dailyCharterInRate;
	}

	public void setDailyCharterInRate(final ILongCurve dailyCharterInRate) {
		this.dailyCharterInRate = dailyCharterInRate;
	}

	@Override
	public IStartEndRequirement getStartRequirement() {
		return startRequirement;
	}

	public void setStartRequirement(final IStartEndRequirement startRequirement) {
		this.startRequirement = startRequirement;
	}

	@Override
	public IStartEndRequirement getEndRequirement() {
		return endRequirement;
	}

	public void setEndRequirement(final IStartEndRequirement endRequirement) {
		this.endRequirement = endRequirement;
	}

	@Nullable
	@Override
	public ISpotCharterInMarket getSpotCharterInMarket() {
		return spotCharterInMarket;
	}

	public void setSpotCharterInMarket(final ISpotCharterInMarket spotCharterInMarket) {
		this.spotCharterInMarket = spotCharterInMarket;
	}

	@Override
	public int getSpotIndex() {
		return spotIndex;
	}

	public void setSpotIndex(final int spotIndex) {
		this.spotIndex = spotIndex;

	}

	@Override
	public boolean isOptional() {
		return this.optional;
	}

	public void setOptional(boolean optional) {
		this.optional = optional;
	}

	@Override
	public ILongCurve getRepositioningFee() {
		return this.repositioningFee;
	}

	@Override
	public ILongCurve getBallastBonus() {
		return this.ballastBonus;
	}

	public void setRepositioningFee(ILongCurve repositioningFee) {
		this.repositioningFee = repositioningFee;
	}

	public void setBallastBonus(ILongCurve ballastBonus) {
		this.ballastBonus = ballastBonus;
	}
}

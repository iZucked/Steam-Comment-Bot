/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.curves.ICurve;
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

	private IVessel vessel;

	private VesselInstanceType vesselInstanceType = VesselInstanceType.UNKNOWN;

	private ICurve dailyCharterInRate;

	private IStartEndRequirement startRequirement;
	private IStartEndRequirement endRequirement;

	private ISpotCharterInMarket spotCharterInMarket;

	private int spotIndex;

	@Override
	public IVessel getVessel() {
		return vessel;
	}

	public void setVessel(final IVessel vessel) {
		this.vessel = vessel;
	}

	@Override
	public VesselInstanceType getVesselInstanceType() {
		return vesselInstanceType;
	}

	public void setVesselInstanceType(final VesselInstanceType vesselInstanceType) {
		this.vesselInstanceType = vesselInstanceType;
	}

	@Override
	public String toString() {
		return vessel.getName();
	}

	@Override
	public ICurve getDailyCharterInRate() {
		return dailyCharterInRate;
	}

	public void setDailyCharterInRate(final ICurve dailyCharterInRate) {
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
}

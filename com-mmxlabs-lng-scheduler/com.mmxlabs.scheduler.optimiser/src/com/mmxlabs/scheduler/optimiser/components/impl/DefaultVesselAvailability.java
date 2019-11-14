/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.IStartRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterCostCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ballastbonus.IBallastBonusContract;

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

	private ICharterCostCalculator charterCostCalculator;

	private ILongCurve repositioningFee;
	
	private ILongCurve ballastBonus;

	private IStartRequirement startRequirement;
	private IEndRequirement endRequirement;

	private ISpotCharterInMarket spotCharterInMarket;

	private int spotIndex;
	
	private boolean optional;
	
	private IBallastBonusContract ballastBonusContract;

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
	public ICharterCostCalculator getCharterCostCalculator() {
		return charterCostCalculator;
	}

	public void setCharterCostCalculator(final ICharterCostCalculator charterCostCalculator) {
		this.charterCostCalculator = charterCostCalculator;
	}

	@Override
	public IStartRequirement getStartRequirement() {
		return startRequirement;
	}

	public void setStartRequirement(final IStartRequirement startRequirement) {
		this.startRequirement = startRequirement;
	}

	@Override
	public IEndRequirement getEndRequirement() {
		return endRequirement;
	}

	public void setEndRequirement(final IEndRequirement endRequirement) {
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

	public void setRepositioningFee(ILongCurve repositioningFee) {
		this.repositioningFee = repositioningFee;
	}

	@Override
	public void setBallastBonusContract(IBallastBonusContract contract) {
		ballastBonusContract = contract;
	}

	@Override
	@Nullable
	public IBallastBonusContract getBallastBonusContract() {
		return ballastBonusContract;
	}
}

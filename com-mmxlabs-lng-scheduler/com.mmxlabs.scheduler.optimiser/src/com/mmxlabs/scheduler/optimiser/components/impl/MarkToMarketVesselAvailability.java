/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.curves.ConstantValueCurve;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IMarkToMarket;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;

/**
 */
public class MarkToMarketVesselAvailability implements IVesselAvailability {

	@NonNull
	private final IMarkToMarket markToMarket;

	@SuppressWarnings("unused")
	@NonNull
	private final IPortSlot portSlot;

	@NonNull
	private final VesselInstanceType vesselInstanceType;

	@NonNull
	private ICurve curve;

	public MarkToMarketVesselAvailability(@NonNull final IMarkToMarket markToMarket, @NonNull final IDischargeOption dischargeOption) {
		this.markToMarket = markToMarket;
		this.portSlot = dischargeOption;
		this.curve = new ConstantValueCurve(0);
		this.vesselInstanceType = VesselInstanceType.DES_PURCHASE;
	}

	public MarkToMarketVesselAvailability(@NonNull final IMarkToMarket markToMarket, @NonNull final ILoadOption loadOption) {
		this.markToMarket = markToMarket;
		this.portSlot = loadOption;
		this.curve = new ConstantValueCurve(0);
		this.vesselInstanceType = VesselInstanceType.FOB_SALE;
	}

	@Override
	@Nullable
	public IVessel getVessel() {
		return null;
	}

	@Override
	@NonNull
	public VesselInstanceType getVesselInstanceType() {
		return vesselInstanceType;
	}

	@Override
	@NonNull
	public ICurve getDailyCharterInRate() {
		return curve;
	}

	@NonNull
	public IMarkToMarket getMarkToMarket() {
		return markToMarket;
	}

	@Override
	@Nullable
	public IStartEndRequirement getStartRequirement() {
		return null;
	}

	@Override
	@Nullable
	public IStartEndRequirement getEndRequirement() {
		return null;
	}

	@Override
	@Nullable
	public ISpotCharterInMarket getSpotCharterInMarket() {
		return null;
	}

	@Override
	public int getSpotIndex() {
		return -1;
	}
}

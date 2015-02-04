/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.common.curves.ConstantValueCurve;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IMarkToMarket;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;

/**
 */
public class MarkToMarketVesselAvailability implements IVesselAvailability {
	private final IMarkToMarket markToMarket;
	private final IPortSlot portSlot;
	private final VesselInstanceType vesselInstanceType;
	private ICurve curve;

	public MarkToMarketVesselAvailability(final IMarkToMarket markToMarket, final IDischargeOption dischargeOption) {
		this.markToMarket = markToMarket;
		this.portSlot = dischargeOption;
		this.curve = new ConstantValueCurve(0);
		this.vesselInstanceType = VesselInstanceType.DES_PURCHASE;
	}

	public MarkToMarketVesselAvailability(final IMarkToMarket markToMarket, final ILoadOption loadOption) {
		this.markToMarket = markToMarket;
		this.portSlot = loadOption;
		this.curve = new ConstantValueCurve(0);
		this.vesselInstanceType = VesselInstanceType.FOB_SALE;
	}

	@Override
	public IVessel getVessel() {
		return null;
	}

	@Override
	public VesselInstanceType getVesselInstanceType() {
		return vesselInstanceType;
	}

	@Override
	public ICurve getDailyCharterInRate() {
		return curve;
	}

	public IMarkToMarket getMarkToMarket() {
		return markToMarket;
	}

	@Override
	public IStartEndRequirement getStartRequirement() {
		return null;
	}

	@Override
	public IStartEndRequirement getEndRequirement() {
		return null;
	}
}

package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.common.curves.ConstantValueCurve;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IMarkToMarket;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;

/**
 * @since 6.0
 */
public class MarkToMarketVessel implements IVessel {
	private final IMarkToMarket markToMarket;
	private final IPortSlot portSlot;
	private final VesselInstanceType vesselInstanceType;
	private final long capacity;
	private ICurve curve;

	public MarkToMarketVessel(final IMarkToMarket markToMarket, final IDischargeOption dischargeOption) {
		this.markToMarket = markToMarket;
		this.portSlot = dischargeOption;
		this.capacity = dischargeOption.getMaxDischargeVolume();
		this.curve = new ConstantValueCurve(0);
		this.vesselInstanceType = VesselInstanceType.DES_PURCHASE;
	}

	public MarkToMarketVessel(final IMarkToMarket markToMarket, final ILoadOption loadOption) {
		this.markToMarket = markToMarket;
		this.portSlot = loadOption;
		this.capacity = loadOption.getMaxLoadVolume();
		this.curve = new ConstantValueCurve(0);
		this.vesselInstanceType = VesselInstanceType.FOB_SALE;
	}

	@Override
	public String getName() {
		return "mtm" + portSlot.getId();
	}

	@Override
	public IVesselClass getVesselClass() {
		return null;
	}

	@Override
	public VesselInstanceType getVesselInstanceType() {
		return vesselInstanceType;
	}

	@Override
	public long getCargoCapacity() {
		return capacity;
	}

	@Override
	public ICurve getHourlyCharterInPrice() {
		return curve;
	}

	@Override
	public int getIndex() {
		return -1;
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IMarkToMarket;
import com.mmxlabs.scheduler.optimiser.components.IMarkToMarketOption;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * This is an {@link ILoadSlot} implementation for use with an {@link IMarkToMarket} instance.
 * 
 * @author Simon Goodall
 * 
 */
public class MarkToMarketLoadSlot implements ILoadSlot, IMarkToMarketOption {
	private final IMarkToMarket markToMarket;
	private final IDischargeOption dischargeOption;

	public MarkToMarketLoadSlot(final IMarkToMarket markToMarket, final IDischargeOption dischargeOption) {
		this.markToMarket = markToMarket;
		this.dischargeOption = dischargeOption;
	}

	@Override
	public String getId() {
		return "mtm-" + dischargeOption.getId();
	}

	@Override
	public IPort getPort() {
		return dischargeOption.getPort();
	}

	@Override
	public ITimeWindow getTimeWindow() {
		return dischargeOption.getTimeWindow();
	}

	@Override
	public PortType getPortType() {
		return PortType.Load;
	}

	@Override
	public long getMinLoadVolume() {
		return dischargeOption.getMinDischargeVolume(getCargoCVValue());
	}

	@Override
	public long getMaxLoadVolume() {
		return dischargeOption.getMaxDischargeVolume(getCargoCVValue());
	}

	@Override
	public int getCargoCVValue() {
		return markToMarket.getCVValue();
	}

	@Override
	public ILoadPriceCalculator getLoadPriceCalculator() {
		return markToMarket.getLoadPriceCalculator();
	}

	@Override
	public int getPricingDate() {
		return IPortSlot.NO_PRICING_DATE;
	}

	@Override
	public boolean isCooldownSet() {
		return false;
	}

	@Override
	public boolean isCooldownForbidden() {
		return false;
	}

	@Override
	public IMarkToMarket getMarkToMarket() {
		return markToMarket;
	}

	@Override
	public PricingEventType getPricingEvent() {
		return dischargeOption.getPricingEvent();
	}

	@Override
	public long getMinLoadVolumeMMBTU() {
		return dischargeOption.getMinDischargeVolumeMMBTU(getCargoCVValue());
	}

	@Override
	public long getMaxLoadVolumeMMBTU() {
		return dischargeOption.getMaxDischargeVolumeMMBTU(getCargoCVValue());
	}

	@Override
	public boolean isVolumeSetInM3() {
		return dischargeOption.isVolumeSetInM3();
	}

	@Override
	public void setVolumeLimits(final boolean volumeInM3, final long minVolume, final long maxVolume) {
		throw new UnsupportedOperationException();
	}
}

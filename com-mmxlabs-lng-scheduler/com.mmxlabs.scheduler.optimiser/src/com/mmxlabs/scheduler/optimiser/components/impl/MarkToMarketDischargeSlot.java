/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IMarkToMarket;
import com.mmxlabs.scheduler.optimiser.components.IMarkToMarketOption;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * This is a {@link IDischargeSlot} implementation for use with an {@link IMarkToMarket} instance.
 * 
 * @author Simon Goodall
 */
public class MarkToMarketDischargeSlot implements IDischargeSlot, IMarkToMarketOption {

	private final IMarkToMarket markToMarket;
	private ILoadOption loadOption;

	public MarkToMarketDischargeSlot(IMarkToMarket markToMarket, ILoadOption loadOption) {
		this.markToMarket = markToMarket;
		this.loadOption = loadOption;
	}

	@Override
	public String getId() {
		return "mtm" + loadOption.getId();
	}

	@Override
	public IPort getPort() {
		return loadOption.getPort();
	}

	@Override
	public ITimeWindow getTimeWindow() {
		return loadOption.getTimeWindow();
	}

	@Override
	public PortType getPortType() {
		return PortType.Discharge;
	}

	@Override
	public long getMinDischargeVolume(int cv) {
		return loadOption.getMinLoadVolume();
	}

	@Override
	public long getMaxDischargeVolume(int cv) {
		return loadOption.getMaxLoadVolume();
	}

	@Override
	public long getMinCvValue() {
		return loadOption.getCargoCVValue();
	}

	@Override
	public long getMaxCvValue() {
		return loadOption.getCargoCVValue();
	}

	@Override
	public ISalesPriceCalculator getDischargePriceCalculator() {
		return markToMarket.getSalesPriceCalculator();
	}

	@Override
	public int getPricingDate() {
		return IPortSlot.NO_PRICING_DATE;
	}

	@Override
	public IMarkToMarket getMarkToMarket() {
		return markToMarket;
	}

	@Override
	public PricingEventType getPricingEvent() {
		return loadOption.getPricingEvent();
	}

	@Override
	public long getMinDischargeVolumeMMBTU(int cv) {
		return loadOption.getMinLoadVolumeMMBTU();
	}

	@Override
	public long getMaxDischargeVolumeMMBTU(int cv) {
		return loadOption.getMaxLoadVolumeMMBTU();
	}

	@Override
	public boolean isVolumeSetInM3() {
		return loadOption.isVolumeSetInM3();
	}

	@Override
	public void setVolumeLimits(final boolean volumeInM3, final long minVolume, final long maxVolume) {
		throw new UnsupportedOperationException();
	}
}

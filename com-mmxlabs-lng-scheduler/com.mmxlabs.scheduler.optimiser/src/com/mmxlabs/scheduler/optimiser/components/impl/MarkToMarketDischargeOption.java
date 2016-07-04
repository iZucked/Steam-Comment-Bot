/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IMarkToMarket;
import com.mmxlabs.scheduler.optimiser.components.IMarkToMarketOption;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * This is a {@link IDischargeOption} implementation for use with an {@link IMarkToMarket} instance.
 * 
 * @author Simon Goodall
 */
public class MarkToMarketDischargeOption implements IDischargeOption, IMarkToMarketOption {

	private final IMarkToMarket markToMarket;
	private ILoadOption loadOption;

	public MarkToMarketDischargeOption(IMarkToMarket markToMarket, ILoadOption loadOption) {
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
	public long getMinDischargeVolume() {
		return loadOption.getMinLoadVolume();
	}

	@Override
	public long getMaxDischargeVolume() {
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
	public void setMinDischargeVolume(long volume) {
	}

	@Override
	public void setMaxDischargeVolume(long volume) {
	}

	@Override
	public long getMinDischargeVolumeMMBTU() {
		return Calculator.convertM3ToMMBTu(loadOption.getMinLoadVolume(), loadOption.getCargoCVValue());
	}

	@Override
	public long getMaxDischargeVolumeMMBTU() {
		return Calculator.convertM3ToMMBTu(loadOption.getMaxLoadVolume(), loadOption.getCargoCVValue());
	}

	@Override
	public boolean isVolumeSetInM3() {
		return loadOption.isVolumeSetInM3();
	}
}

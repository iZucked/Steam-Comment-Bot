/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IMarkToMarket;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * This is a {@link IDischargeOption} implementation for use with an {@link IMarkToMarket} instance.
 * 
 * @author Simon Goodall
 * @since 6.0
 */
public class MarkToMarketDischargeOption implements IDischargeOption {

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
		return  IPortSlot.NO_PRICING_DATE;
	}
}

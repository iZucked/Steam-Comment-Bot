package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IMarkToMarket;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * This is an {@link ILoadSlot} implementation for use with an {@link IMarkToMarket} instance.
 * 
 * @author Simon Goodall
 * @since 6.0
 * 
 */
public class MarkToMarketLoadSlot implements ILoadSlot {
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
		return dischargeOption.getMinDischargeVolume();
	}

	@Override
	public long getMaxLoadVolume() {
		return dischargeOption.getMaxDischargeVolume();
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
		return dischargeOption.getPricingDate();
	}

	@Override
	public boolean isCooldownSet() {
		return false;
	}

	@Override
	public boolean isCooldownForbidden() {
		return false;
	}

}

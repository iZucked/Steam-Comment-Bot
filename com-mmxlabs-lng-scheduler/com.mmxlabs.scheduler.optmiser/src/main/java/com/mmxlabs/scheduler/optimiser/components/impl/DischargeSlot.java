package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.optimiser.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;

/**
 * Default implementation of {@link ILoadSlot}.
 * 
 * @author Simon Goodall
 * 
 */
public final class DischargeSlot implements IDischargeSlot {

	private IPort dischargePort;

	private ITimeWindow dischargeWindow;

	private String dischargeID;

	private long minDischargeVolume;

	private long maxDischargeVolume;

	private long salesPrice;

	@Override
	public IPort getDischargePort() {
		return dischargePort;
	}

	public void setDischargePort(final IPort dischargePort) {
		this.dischargePort = dischargePort;
	}

	@Override
	public ITimeWindow getDischargeWindow() {
		return dischargeWindow;
	}

	public void setDischargeWindow(final ITimeWindow dischargeWindow) {
		this.dischargeWindow = dischargeWindow;
	}

	@Override
	public String getDischargeID() {
		return dischargeID;
	}

	public void setDischargeID(final String dischargeID) {
		this.dischargeID = dischargeID;
	}

	@Override
	public long getMinDischargeVolume() {
		return minDischargeVolume;
	}

	public void setMinDischargeVolume(final long minDischargeVolume) {
		this.minDischargeVolume = minDischargeVolume;
	}

	@Override
	public long getMaxDischargeVolume() {
		return maxDischargeVolume;
	}

	public void setMaxDischargeVolume(final long maxDischargeVolume) {
		this.maxDischargeVolume = maxDischargeVolume;
	}

	@Override
	public long getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(final long salesPrice) {
		this.salesPrice = salesPrice;
	}

}
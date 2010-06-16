package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.optimiser.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;

/**
 * Default implementation of {@link ILoadSlot}.
 * 
 * @author Simon Goodall
 * 
 */
public final class LoadSlot implements ILoadSlot {

	private IPort loadPort;

	private ITimeWindow loadWindow;

	private String loadID;

	private long minLoadVolume;

	private long maxLoadVolume;

	private long purchasePrice;

	@Override
	public IPort getLoadPort() {
		return loadPort;
	}

	public void setLoadPort(final IPort loadPort) {
		this.loadPort = loadPort;
	}

	@Override
	public ITimeWindow getLoadWindow() {
		return loadWindow;
	}

	public void setLoadWindow(final ITimeWindow loadWindow) {
		this.loadWindow = loadWindow;
	}

	@Override
	public String getLoadID() {
		return loadID;
	}

	public void setLoadID(final String loadID) {
		this.loadID = loadID;
	}

	@Override
	public long getMinLoadVolume() {
		return minLoadVolume;
	}

	public void setMinLoadVolume(final long minLoadVolume) {
		this.minLoadVolume = minLoadVolume;
	}

	@Override
	public long getMaxLoadVolume() {
		return maxLoadVolume;
	}

	public void setMaxLoadVolume(final long maxLoadVolume) {
		this.maxLoadVolume = maxLoadVolume;
	}

	@Override
	public long getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(final long purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
}

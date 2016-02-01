/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.events.impl;

import com.mmxlabs.scheduler.optimiser.events.ILoadEvent;

/**
 * Implementation of {@link ILoadEvent} extending {@link PortVisitEventImpl}.
 * 
 * @author Simon Goodall
 */
public final class LoadEventImpl extends PortVisitEventImpl implements ILoadEvent {

	private long loadVolume;
	private long purchasePrice;

	@Override
	public long getLoadVolume() {
		return loadVolume;
	}

	@Override
	public long getPurchasePrice() {
		return purchasePrice;
	}

	public void setLoadVolume(final long loadVolume) {
		this.loadVolume = loadVolume;
	}

	public void setPurchasePrice(final long purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
}

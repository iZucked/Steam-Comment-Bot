/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.events.impl;

import com.mmxlabs.scheduler.optimiser.events.ILoadEvent;

/**
 * Implementation of {@link ILoadEvent} extending {@link PortVisitEventImpl}.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public final class LoadEventImpl<T> extends PortVisitEventImpl<T> implements
		ILoadEvent<T> {

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

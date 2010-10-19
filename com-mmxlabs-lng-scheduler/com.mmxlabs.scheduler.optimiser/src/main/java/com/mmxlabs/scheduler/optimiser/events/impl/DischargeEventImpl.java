/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.events.impl;

import com.mmxlabs.scheduler.optimiser.events.IDischargeEvent;

/**
 * Implementation of {@link IDischargeEvent} extending {@link PortVisitEventImpl}.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public final class DischargeEventImpl<T> extends PortVisitEventImpl<T> implements
		IDischargeEvent<T> {

	private long dischargeVolume;
	private long salesPrice;

	@Override
	public long getDischargeVolume() {
		return dischargeVolume;
	}

	@Override
	public long getSalesPrice() {
		return salesPrice;
	}

	public void setDischargeVolume(final long dischargeVolume) {
		this.dischargeVolume = dischargeVolume;
	}

	public void setSalesPrice(final long salesPrice) {
		this.salesPrice = salesPrice;
	}
}

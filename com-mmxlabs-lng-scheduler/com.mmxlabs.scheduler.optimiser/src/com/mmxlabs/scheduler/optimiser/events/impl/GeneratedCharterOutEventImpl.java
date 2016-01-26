/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.events.impl;

import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.events.IGeneratedCharterOutEvent;
import com.mmxlabs.scheduler.optimiser.events.IIdleEvent;

/**
 * Implementation of {@link IIdleEvent}.
 * 
 * @author Simon Goodall
 */
public final class GeneratedCharterOutEventImpl extends AbstractScheduledEventImpl implements IGeneratedCharterOutEvent {

	private IPort port;

	private long charterOutRevenue;

	@Override
	public IPort getPort() {
		return port;
	}

	public void setPort(final IPort port) {
		this.port = port;
	}

	@Override
	public long getCharterOutRevenue() {
		return charterOutRevenue;
	}

	public void setCharterOutRevenue(long charterOutRevenue) {
		this.charterOutRevenue = charterOutRevenue;
	}
}

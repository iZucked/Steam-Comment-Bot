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
	private long startHeelInM3;
	private long endHeelInM3;

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

	@Override
	public long getStartHeelInM3() {
		return startHeelInM3;
	}

	public void setStartHeelInM3(long startHeelInM3) {
		this.startHeelInM3 = startHeelInM3;
	}

	@Override
	public long getEndHeelInM3() {
		return endHeelInM3;
	}

	public void setEndHeelInM3(long endHeelInM3) {
		this.endHeelInM3 = endHeelInM3;
	}
}

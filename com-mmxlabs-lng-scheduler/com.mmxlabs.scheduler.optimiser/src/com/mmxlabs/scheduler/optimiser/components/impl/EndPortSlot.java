/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public final class EndPortSlot extends PortSlot {

	private boolean endCold;
	private long targetEndHeelInM3;

	public EndPortSlot(final String id, final IPort port, final ITimeWindow timeWindow, final boolean endCold, final long targetEndHeelInM3) {
		super(id, port, timeWindow);
		this.endCold = endCold;
		this.targetEndHeelInM3 = targetEndHeelInM3;
		setPortType(PortType.End);
	}

	public boolean isEndCold() {
		return endCold;
	}

	public void setEndCold(final boolean endCold) {
		this.endCold = endCold;
	}

	public long getTargetEndHeelInM3() {
		return targetEndHeelInM3;
	}

	public void setTargetEndHeelInM3(final long targetEndHeelInM3) {
		this.targetEndHeelInM3 = targetEndHeelInM3;
	}
}

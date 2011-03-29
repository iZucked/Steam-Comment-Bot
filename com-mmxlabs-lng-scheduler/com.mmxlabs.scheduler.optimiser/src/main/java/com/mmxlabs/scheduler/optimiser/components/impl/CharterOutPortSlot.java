/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.ICharterOut;
import com.mmxlabs.scheduler.optimiser.components.ICharterOutPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;

public class CharterOutPortSlot extends PortSlot implements ICharterOutPortSlot {
	private ICharterOut charterOut;

	public CharterOutPortSlot(String id, IPort port, ITimeWindow timeWindow, ICharterOut charterOut) {
		super(id, port, timeWindow);
		this.charterOut = charterOut;
	}	
	
	@Override
	public ICharterOut getCharterOut() {
		return charterOut;
	}
}

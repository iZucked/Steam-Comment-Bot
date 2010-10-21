/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.ICharterOutPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;

public class CharterOutPortSlot extends PortSlot implements ICharterOutPortSlot {
	public CharterOutPortSlot(String id, IPort port, ITimeWindow timeWindow) {
		super(id, port, timeWindow);
	}	
}

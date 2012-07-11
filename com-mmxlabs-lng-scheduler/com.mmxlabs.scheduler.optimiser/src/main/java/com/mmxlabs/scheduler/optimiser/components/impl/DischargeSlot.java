/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;

/**
 * A physical discharge option; because there are no extra physical details here this just makes the superclass implement the extra interface.
 * 
 * @author Simon Goodall
 * 
 */
public final class DischargeSlot extends DischargeOption implements IDischargeSlot {
	public DischargeSlot() {
		super();
	}

	public DischargeSlot(final String id, final IPort port, final ITimeWindow timwWindow, final long minDischargeVolume, final long maxDischargeVolume, final ISalesPriceCalculator priceCalculator) {
		super(id, port, timwWindow, minDischargeVolume, maxDischargeVolume, priceCalculator);
	}
}
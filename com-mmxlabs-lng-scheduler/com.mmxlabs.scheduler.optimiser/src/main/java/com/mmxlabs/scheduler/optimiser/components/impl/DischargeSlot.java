/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components.impl;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.contracts.IShippingPriceCalculator;

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

	public DischargeSlot(String id, IPort port, ITimeWindow timwWindow, long minDischargeVolume, long maxDischargeVolume, IShippingPriceCalculator<?> priceCalculator) {
		super(id, port, timwWindow, minDischargeVolume, maxDischargeVolume, priceCalculator);
	}
}
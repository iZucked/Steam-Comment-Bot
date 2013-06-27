/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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

	public DischargeSlot(final String id, final IPort port, final ITimeWindow timeWindow, final long minDischargeVolume, final long maxDischargeVolume, final ISalesPriceCalculator priceCalculator, final long minCvValue, final long maxCvValue, final int pricingDate) {
		super(id, port, timeWindow, minDischargeVolume, maxDischargeVolume, minCvValue, maxCvValue, priceCalculator, pricingDate);
	}
}
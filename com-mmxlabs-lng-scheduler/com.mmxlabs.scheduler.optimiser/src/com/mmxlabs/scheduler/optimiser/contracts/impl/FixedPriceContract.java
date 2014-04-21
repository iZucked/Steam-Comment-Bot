/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts.impl;

import com.mmxlabs.scheduler.optimiser.components.IPort;

/**
 * @author hinton
 * 
 */
public class FixedPriceContract extends SimpleContract {
	private final int pricePerMMBTU;

	public FixedPriceContract(final int pricePerMMBTU) {
		this.pricePerMMBTU = pricePerMMBTU;
	}

	@Override
	public int calculateSimpleUnitPrice(final int loadTime, final IPort port) {
		return pricePerMMBTU;
	}
}

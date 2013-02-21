/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.trading.optimiser.impl;

/**
 * The entity which is everyone else
 * 
 * @author hinton
 * 
 */
public class OtherEntity extends Entity {
	public OtherEntity(final String name) {
		super(name);
	}

	@Override
	public int getDownstreamTransferPrice(final int dischargePricePerM3, final int cvValue) {
		return dischargePricePerM3;
	}

	@Override
	public int getUpstreamTransferPrice(final int loadPricePerM3, final int cvValue) {
		return loadPricePerM3;
	}

	@Override
	public long getTaxedProfit(final long downstreamTotalPretaxProfit, final int time) {
		return 0;
	}
}

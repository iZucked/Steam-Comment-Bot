/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
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
	public int getDownstreamTransferPrice(int dischargePricePerM3, int cvValue) {
		return dischargePricePerM3;
	}

	@Override
	public int getUpstreamTransferPrice(int loadPricePerM3, int cvValue) {
		return loadPricePerM3;
	}

	@Override
	public long getTaxedProfit(long downstreamTotalPretaxProfit, int time) {
		return 0;
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

public final class UnusedSlotDetails implements IProfitAndLossDetails {

	private long totalGroupProfitAndLoss;

	@Override
	public long getTotalGroupProfitAndLoss() {
		return totalGroupProfitAndLoss;
	}

	@Override
	public void setTotalGroupProfitAndLoss(long totalGroupProfitAndLoss) {
		this.totalGroupProfitAndLoss = totalGroupProfitAndLoss;
	}

}

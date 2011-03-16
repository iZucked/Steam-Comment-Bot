/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.pandl;

import scenario.schedule.Schedule;

/**
 * A device which takes a {@link Schedule}, and does some additional computation
 * to compute the profit and loss for the different corporate entities involved.
 * 
 * @author hinton
 * 
 */
public class ProfitAndLossCalculator {
	public ProfitAndLossCalculator() {
		
	}
	
	public void addProfitAndLoss(final Schedule schedule) {
		// P&L is computed for each cargo (what about orphan legs)
	}
}

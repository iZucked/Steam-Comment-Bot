/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

public interface IEndRequirement extends IStartEndRequirement {
	/**
	 * Returns true if there is no user defined end date and we are using the notional end time.
	 * 
	 * @return
	 */
	boolean isHireCostOnlyEndRule();

	boolean isEndCold();

	long getTargetHeelInM3();
}

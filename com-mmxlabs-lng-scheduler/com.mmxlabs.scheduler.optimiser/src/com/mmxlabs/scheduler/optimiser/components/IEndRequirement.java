/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.NonNull;

public interface IEndRequirement extends IStartEndRequirement {
	/**
	 * Returns true if there is no user defined end date and we are using the notional end time.
	 * 
	 * @return
	 */
	boolean isHireCostOnlyEndRule();

	@NonNull
	IHeelOptionConsumer getHeelOptions();
	
	int getMinDurationInHours();
	
	int getMaxDurationInHours();
	
	void setMinDurationInHours(int hours);
	
	void setMaxDurationInHours(int hours);

	boolean isMinDurationSet();
	
	boolean isMaxDurationSet();

}

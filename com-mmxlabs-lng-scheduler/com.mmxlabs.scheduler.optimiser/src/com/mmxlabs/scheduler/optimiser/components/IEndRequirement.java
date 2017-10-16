/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
	
	int getMinDurationInDays();
	
	int getMaxDurationInDays();
	
	int getMinDurationInHours();
	
	int getMaxDurationInHours();
	
	void setMinDurationInDays(int value);
	
	void setMaxDurationInDays(int value);

	boolean isMinDurationSet();
	
	boolean isMaxDurationSet();

}

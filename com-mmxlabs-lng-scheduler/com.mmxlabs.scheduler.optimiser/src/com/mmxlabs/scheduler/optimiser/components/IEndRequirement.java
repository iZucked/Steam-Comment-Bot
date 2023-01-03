/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.NonNull;

public interface IEndRequirement extends IStartEndRequirement {
	
	@NonNull
	IHeelOptionConsumer getHeelOptions();
	
	int getMinDurationInHours();
	
	int getMaxDurationInHours();
	
	void setMinDurationInHours(int hours);
	
	void setMaxDurationInHours(int hours);

	boolean isMinDurationSet();
	
	boolean isMaxDurationSet();

}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;

/**
 */
public interface IDateKeyProvider extends IDataComponentProvider {
	
	int getDateKeyFromHours(int hours);
}

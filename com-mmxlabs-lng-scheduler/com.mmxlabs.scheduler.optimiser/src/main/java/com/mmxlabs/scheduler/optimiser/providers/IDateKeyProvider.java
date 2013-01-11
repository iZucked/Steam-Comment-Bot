/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;

/**
 * @since 2.0
 */
public interface IDateKeyProvider extends IDataComponentProvider {
	
	int getDateKeyFromHours(int hours);
}

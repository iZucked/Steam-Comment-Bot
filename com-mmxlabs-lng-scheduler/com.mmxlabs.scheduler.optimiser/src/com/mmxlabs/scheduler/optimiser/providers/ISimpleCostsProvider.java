/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
* Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

/**
 * @author Alex
 *
 */
public interface ISimpleCostsProvider extends IDataComponentProvider {

	/**
	 * @param portSlot
	 * @return
	 */
	long getCostsValue(IPortSlot portSlot);
	
}

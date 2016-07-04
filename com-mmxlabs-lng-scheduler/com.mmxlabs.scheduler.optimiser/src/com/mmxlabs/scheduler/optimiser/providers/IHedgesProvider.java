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
 * @author berkan
 *
 */
public interface IHedgesProvider extends IDataComponentProvider {

	/**
	 * @param portSlot
	 * @return
	 */
	long getHedgeValue(IPortSlot portSlot);
	
}

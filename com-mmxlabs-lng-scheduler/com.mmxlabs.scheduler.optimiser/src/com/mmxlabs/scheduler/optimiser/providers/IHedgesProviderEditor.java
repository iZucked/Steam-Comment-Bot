/**
* Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

/**
 * @author berkan
 *
 */
public interface IHedgesProviderEditor extends IHedgesProvider {

	/**
	 * @param portSlot
	 * @param cost
	 */
	void setHedgeCost(IPortSlot portSlot, long cost);
	
}
